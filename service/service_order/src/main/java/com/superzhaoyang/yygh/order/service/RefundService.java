package com.superzhaoyang.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superzhaoyang.yygh.model.order.PaymentInfo;
import com.superzhaoyang.yygh.model.order.RefundInfo;

public interface RefundService extends IService<RefundInfo> {
    /**
     * 保存退款记录
     * @param paymentInfo
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);

}
