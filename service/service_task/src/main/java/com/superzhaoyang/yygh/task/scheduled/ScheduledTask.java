package com.superzhaoyang.yygh.task.scheduled;


import com.superzhaoyang.common.rabbit.service.RabbitService;
import com.superzhaoyang.common.rabbit.service.constant.MqConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.superzhaoyang.common.rabbit.service.constant.MqConst.EXCHANGE_DIRECT_TASK;

@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;
    // 每天8点执行发放，就医提醒
    //cron表达式，设置执行间隔
    @Scheduled(cron = "0 0 8 * * ? ")
    public void taskPatient() {
        rabbitService.sendMessage(EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }
}
