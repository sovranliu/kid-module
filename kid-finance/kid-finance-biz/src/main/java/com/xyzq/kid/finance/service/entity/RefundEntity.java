package com.xyzq.kid.finance.service.entity;

/**
 * 退款信息
 */
public class RefundEntity {
    /**
     * 初始状态
     */
    public final static int STATE_INIT = 0;
    /**
     * 操作中
     */
    public final static int STATE_REFUNDING = 1;
    /**
     * 退款成功
     */
    public final static int STATE_SUCCESS = 2;
    /**
     * 退款失败
     */
    public final static int STATE_FAIL = 3;


    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 退款号
     */
    public String refundNo;
    /**
     * 退款金额（分）
     */
    public int refundFee;
    /**
     * 状态，0：初始，1：退款中，2：退款成功，3：退款失败
     */
    public int state;
}
