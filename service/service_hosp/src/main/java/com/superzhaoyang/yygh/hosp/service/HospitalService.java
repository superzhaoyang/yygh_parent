package com.superzhaoyang.yygh.hosp.service;

import com.superzhaoyang.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);
}
