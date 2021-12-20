package com.superzhaoyang.yygh.msm.service;

import com.superzhaoyang.yygh.vo.msm.MsmVo;

public interface MsmService {

    boolean send(String phone, String code);

    boolean send(MsmVo msmVo);
}
