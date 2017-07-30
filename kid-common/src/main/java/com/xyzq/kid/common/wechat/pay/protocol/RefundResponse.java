package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;

/**
 * 退款反馈结构体
 */
public class RefundResponse extends WechatResponse {
    /**
     * APP ID
     */
    @ProtocolField("appid")
    public String appId;
    /**
     * 商户号
     */
    @ProtocolField("mch_id")
    public String merchantId;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString;
    /**
     * 签名
     */
    @ProtocolField("sign")
    public String sign;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 商户订单号
     */
    @ProtocolField("out_trade_no")
    public String orderNo;
    /**
     * 商户退款单号
     */
    @ProtocolField("out_refund_no")
    public String refundTradeNo;
    /**
     * 微信退款单号
     */
    @ProtocolField("refund_id")
    public String refundId;
    /**
     * 退款金额
     */
    @ProtocolField("refund_fee")
    public int refundFee;
    /**
     * 标价金额
     */
    @ProtocolField("total_fee")
    public int totalFee;
    /**
     * 标价币种
     */
    @ProtocolField("fee_type")
    public String feeType;
    /**
     * 现金支付金额
     */
    @ProtocolField("cash_fee")
    public int cashFee;
    /**
     * 现金退款金额
     */
    @ProtocolField("cash_refund_fee")
    public int cashRefundFee;
    /**
     * 代金券退款总金额
     */
    @ProtocolField("coupon_refund_fee")
    public int couponRefundFee;
    /**
     * 退款代金券使用数量
     */
    @ProtocolField("coupon_refund_count")
    public int couponRefundCount;
}
