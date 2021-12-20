package com.superzhaoyang.yygh.hosp.repository;

import com.superzhaoyang.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);

    Map<String, Object> getHospById(String id);

    List<Hospital> findHospitalByHosnameLike(String hosname);
}
