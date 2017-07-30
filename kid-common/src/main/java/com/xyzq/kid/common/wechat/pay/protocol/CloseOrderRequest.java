package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.etc.Serial;

/**
 * 关闭订单请求结构体
 */
public class CloseOrderRequest extends WechatRequest {
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
     * 构建关闭订单请求结构体
     *
     * @param tradeNo 订单号
     * @return 关闭订单请求结构体
     */
    public static CloseOrderRequest build(String tradeNo) {
        CloseOrderRequest result = new CloseOrderRequest();
        result.appId = WechatConfig.appId;
        result.merchantId = WechatConfig.merchantId;
        result.tradeNo = tradeNo;
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signature = null;
        result.signType = "MD5";
        return result;
    }
}
