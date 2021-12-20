package com.superzhaoyang.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.hosp.HospitalSet;
import com.superzhaoyang.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}
