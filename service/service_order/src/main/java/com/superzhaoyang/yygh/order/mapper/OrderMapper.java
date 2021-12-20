package com.superzhaoyang.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.superzhaoyang.yygh.model.order.OrderInfo;
import com.superzhaoyang.yygh.vo.order.OrderCountQueryVo;
import com.superzhaoyang.yygh.vo.order.OrderCountVo;
import feign.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface OrderMapper extends BaseMapper<OrderInfo> {
    List<OrderCountVo> selectOrderCnt(OrderCountQueryVo orderCountQueryVo);
}

