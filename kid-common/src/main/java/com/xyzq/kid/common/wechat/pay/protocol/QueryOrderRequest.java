package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.etc.Serial;

/**
 * 订单查询请求结构体
 */
public class QueryOrderRequest extends WechatRequest {
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
    public String tradeNo;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString;
    /**
     * 签名方式
     */
    @ProtocolField("sign_type")
    public String signType;
    /**
     * 签名
     */
    public String signature;


    /**
     * 构建订单查询请求结构体
     *
     * @param tradeNo 订单号
     * @param transactionId 微信支付订单号
     * @return 订单查询请求结构体
     */
    public static QueryOrderRequest build(String tradeNo, String transactionId) {
        QueryOrderRequest result = new QueryOrderRequest();
        result.appId = WechatConfig.appId;
        result.merchantId = WechatConfig.merchantId;
        result.tradeNo = tradeNo;
        result.transactionId = transactionId;
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signType = "MD5";
        result.signature = null;
        return result;
    }
}
