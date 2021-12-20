package com.superzhaoyang.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superzhaoyang.yygh.enums.RefundStatusEnum;
import com.superzhaoyang.yygh.model.order.PaymentInfo;
import com.superzhaoyang.yygh.model.order.RefundInfo;
import com.superzhaoyang.yygh.order.mapper.RefundMapper;
import com.superzhaoyang.yygh.order.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, RefundInfo> implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", paymentInfo.getOrderId());
        queryWrapper.eq("payment_type", paymentInfo.getPaymentType());
        RefundInfo refundInfo = refundMapper.selectOne(queryWrapper);
        if(null != refundInfo) return refundInfo;
        // 保存交易记录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        //paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        refundMapper.insert(refundInfo);
        return refundInfo;
    }

}
