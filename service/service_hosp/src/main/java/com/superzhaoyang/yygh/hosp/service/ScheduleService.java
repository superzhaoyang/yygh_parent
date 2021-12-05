package com.superzhaoyang.yygh.hosp.service;

import com.superzhaoyang.yygh.model.hosp.Schedule;
import com.superzhaoyang.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    //上传排班接口
    void save(Map<String, Object> paramMap);

    void remove(String hoscode, String hosScheduleId);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
}
