package com.xyzq.kid.finance.service.entity;

import com.xyzq.simpson.base.time.DateTime;

/**
 * 订单信息实体
 *
 * 用于后台查询
 */
public class OrderInfoEntity {
    /**
     * 未成功支付
     */
    public final static int STATUS_NOPAY = 1;
    /**
     * 已支付
     */
    public final static int STATUS_PAID = 2;
    /**
     * 已退款
     */
    public final static int STATUS_REFUND = 3;


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
     * 商品类型
     */
    public int goodsType;
    /**
     * 支付金额（分）
     */
    public int fee;
    /**
     * 支付状态
     */
    public int status;
    /**
     * 订单创建时间
     */
    public DateTime time;
}
