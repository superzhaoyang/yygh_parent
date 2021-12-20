package com.superzhaoyang.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.order.OrderInfo;
import com.superzhaoyang.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {

    void savePaymentInfo(OrderInfo order, Integer status);

    void paySuccess(String out_trade_no, Integer status, Map<String, String> resultMap);
    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
