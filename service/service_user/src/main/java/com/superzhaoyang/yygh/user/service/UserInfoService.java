package com.superzhaoyang.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.user.UserInfo;
import com.superzhaoyang.yygh.vo.user.LoginVo;
import com.superzhaoyang.yygh.vo.user.UserAuthVo;
import com.superzhaoyang.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    UserInfo selectWxInfoOpenId(String openId);

    Map<String, Object> loginUser(LoginVo loginVo);

    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}
