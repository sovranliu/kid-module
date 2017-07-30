package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;

/**
 * 订单查询请求结构体
 */
public class QueryOrderResponse extends WechatResponse {
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
     * 订单号
     */
    @ProtocolField("out_trade_no")
    public String orderNo;
    /**
     * 微信用户开放ID
     */
    @ProtocolField("openid")
    public String openId = null;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 标价金额
     */
    @ProtocolField("total_fee")
    public int fee;
    /**
     * 收款金额
     */
    @ProtocolField("cash_fee")
    public int amount;
    /**
     * 交易状态，SUCCESS—支付成功，REFUND—转入退款，NOTPAY—未支付，CLOSED—已关闭，REVOKED—已撤销（刷卡支付），USERPAYING--用户支付中，PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    @ProtocolField("trade_state")
    public String state;
}
