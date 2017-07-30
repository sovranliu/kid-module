package com.xyzq.kid.finance.service.entity;

/**
 * 订单信息
 */
public class OrderEntity {
    /**
     * 初始状态
     */
    public final static int STATE_INIT = 0;
    /**
     * 待支付
     */
    public final static int STATE_PAYING = 1;
    /**
     * 已支付
     */
    public final static int STATE_PAID = 2;
    /**
     * 退款中
     */
    public final static int STATE_REFUNDING = 3;
    /**
     * 已退款
     */
    public final static int STATE_REFUNDED = 4;
    /**
     * 已关闭
     */
    public final static int STATE_CLOSED = 5;


    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 支付者微信开放ID
     */
    public String openId;
    /**
     * 订单金额（分）
     */
    public int fee;
    /**
     * 商品类型
     */
    public int goodsType;
    /**
     * 预支付ID
     */
    public String prepayId;
    /**
     * 订单状态，0：初始，1：待支付，2：已支付，3：待退款，4：已退款，5：已关闭
     */
    public int state;
}
