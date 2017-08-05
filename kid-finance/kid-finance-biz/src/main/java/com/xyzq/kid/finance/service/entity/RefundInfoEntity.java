package com.xyzq.kid.finance.service.entity;

import com.xyzq.simpson.base.time.DateTime;

/**
 * 退款信息
 */
public class RefundInfoEntity {
    /**
     * 退款中
     */
    public final static int STATUS_REFUNDING = 1;
    /**
     * 已退款
     */
    public final static int STATUS_REFUND_SUCCESS = 2;
    /**
     * 退款失败
     */
    public final static int STATUS_REFUND_FAIL = 3;


    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 微信用户开放ID
     */
    public String openId;
    /**
     * 用户手机号码
     */
    public String mobileNo;
    /**
     * 退款号
     */
    public String refundNo;
    /**
     * 支付金额（分）
     */
    public int fee;
    /**
     * 退款金额（分）
     */
    public int refundFee;
    /**
     * 状态，1：退款中，2：退款成功，3：退款失败
     */
    public int status;
    /**
     * 订单创建时间
     */
    public DateTime time;
}
