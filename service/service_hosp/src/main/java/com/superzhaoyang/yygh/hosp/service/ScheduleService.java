package com.superzhaoyang.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.hosp.Schedule;
import com.superzhaoyang.yygh.vo.hosp.ScheduleOrderVo;
import com.superzhaoyang.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    //上传排班接口
    void save(Map<String, Object> paramMap);

    void remove(String hoscode, String hosScheduleId);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getById(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * 修改排班
     */
    void update(Schedule schedule);

}
