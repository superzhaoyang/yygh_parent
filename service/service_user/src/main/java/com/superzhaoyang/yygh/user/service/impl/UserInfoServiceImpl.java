package com.superzhaoyang.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superzhaoyang.yygh.common.exception.YyghException;
import com.superzhaoyang.yygh.common.helper.JwtHelper;
import com.superzhaoyang.yygh.common.result.ResultCodeEnum;
import com.superzhaoyang.yygh.enums.AuthStatusEnum;
import com.superzhaoyang.yygh.model.user.Patient;
import com.superzhaoyang.yygh.model.user.UserInfo;
import com.superzhaoyang.yygh.user.mapper.UserInfoMapper;
import com.superzhaoyang.yygh.user.service.PatientService;
import com.superzhaoyang.yygh.user.service.UserInfoService;
import com.superzhaoyang.yygh.vo.user.LoginVo;
import com.superzhaoyang.yygh.vo.user.UserAuthVo;
import com.superzhaoyang.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PatientService patientService;

    @Override
    public UserInfo selectWxInfoOpenId(String openId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openId);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        return userInfo;
    }

    //用户手机号登录接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {

        // 从loginVo获取输入的手机号 和 验证ma
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        // 判断手机号和验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        // 判断手机验证码和输入验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone).trim();
        System.out.println(redisCode);
        System.out.println(phone);
        System.out.println(code);

        if (!redisCode.equals(code)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.selectWxInfoOpenId(loginVo.getOpenid());
            if (null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

        // 如果userinfo为空 进行正常手机登录
        if (userInfo == null) {
            // 判断是否是第一次登录；查询数据库， 如果不存在相同手机号就是第一次登录
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);

            if (userInfo == null) {
                // 添加到数据库
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
        }

        // 删除多余记录
        deleteDuplicateRecord(phone);
        // 检验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        /* 不是第一次，直接登录

         返回登录信息
         返回登录名
         返回token信息
        返回页面显示名称*/
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;

    }


    // 用户认证
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        // 根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        // 设置认证信息
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        // 进行信息更新
        baseMapper.updateById(userInfo);
    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        // UserInfoQueryVo获取条件值
        String name = userInfoQueryVo.getKeyword();
        Integer status = userInfoQueryVo.getStatus();
        Integer authStatus = userInfoQueryVo.getAuthStatus();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();

        // 对条件值进行非空判断
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }

        if (!StringUtils.isEmpty(authStatus)) {
            queryWrapper.eq("auth_status", authStatus);
        }

        if (!StringUtils.isEmpty(createTimeBegin)) {
            queryWrapper.ge("create_time", createTimeBegin);
        }

        if (!StringUtils.isEmpty(createTimeEnd)) {
            queryWrapper.le("end_time", createTimeEnd);
        }

        // 调用mapper的方法
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, queryWrapper);
        // 编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });

        return pages;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            UserInfo userInfo = this.getById(userId);
            userInfo.setStatus(status);
            this.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        Map<String, Object> map = new HashMap<>();
        //根据userid查询用户信息
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo", userInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAllUserId(userId);
        map.put("patientList", patientList);
        return map;

    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if (authStatus.intValue() == 2 || authStatus.intValue() == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    private UserInfo packageUserInfo(UserInfo userInfo) {
        // 处理认证状态编码
        userInfo.getParam().put("authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        // 处理用户状态 0 1
        String statusString = userInfo.getStatus().intValue() == 0 ? "锁定" : "正常";
        userInfo.getParam().put("statusString", statusString);
        return userInfo;
    }


    private void deleteDuplicateRecord(String phone) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        int count = baseMapper.selectCount(wrapper);
        System.out.println("-----------------------------" + count + "------------------------------");
        if (count > 1) {
            wrapper.eq("name", "");
            int res = baseMapper.delete(wrapper);
            System.out.println("-----------------------------" + res + "------------------------------");
        }
    }


}
