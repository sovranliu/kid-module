package com.xyzq.kid.finance.service.entity;

/**
 * 已支付的订单信息
 */
public class PaidOrderEntity extends OrderEntity {
    /**
     * 微信支付订单号
     */
    public String transactionId;
    /**
     * 收款金额（分）
     */
    public int amount;
}
