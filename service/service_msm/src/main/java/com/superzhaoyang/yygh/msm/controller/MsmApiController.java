package com.superzhaoyang.yygh.msm.controller;

import com.superzhaoyang.yygh.common.result.Result;
import com.superzhaoyang.yygh.msm.service.MsmService;
import com.superzhaoyang.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 发送手机验证码
    @GetMapping("send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        // 从redis获取验证码，返回ok
        String code = redisTemplate.opsForValue().get(phone);
        // 如果从redis获取不到，则生成验证码，通过整合短信服务进行发送
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }

        code = RandomUtil.getSixBitRandom();
//        boolean isSend = msmService.send(phone, code);
        boolean isSend = true;
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("发送短信失败");
        }

    }

}
