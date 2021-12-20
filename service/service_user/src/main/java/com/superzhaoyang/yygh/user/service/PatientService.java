package com.superzhaoyang.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {

    List<Patient> findAllUserId(Long userId);

    Patient getPatientId(Long id);
}
