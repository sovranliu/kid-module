package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.etc.Serial;

/**
 * 退款查询请求结构体
 */
public class QueryRefundRequest extends WechatRequest {
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
    public String signature;
    /**
     * 签名方式
     */
    @ProtocolField("sign_type")
    public String signType;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 订单号
     */
    @ProtocolField("out_trade_no")
    public String tradeNo;
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
     * 构建退款请求结构体
     *
     * @param tradeNo 订单号
     * @param transactionId 微信支付订单号
     * @param refundTradeNo 微信支付订单号
     * @param refundId 微信支付订单号
     * @return 订单查询请求结构体
     */
    public static QueryRefundRequest build(String tradeNo, String transactionId, String refundTradeNo, String refundId) {
        QueryRefundRequest result = new QueryRefundRequest();
        result.appId = WechatConfig.appId;
        result.merchantId = WechatConfig.merchantId;
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signature = null;
        result.tradeNo = tradeNo;
        result.signType = "MD5";
        result.transactionId = transactionId;
        result.refundTradeNo = refundTradeNo;
        result.refundId = refundId;
        return result;
    }
}
