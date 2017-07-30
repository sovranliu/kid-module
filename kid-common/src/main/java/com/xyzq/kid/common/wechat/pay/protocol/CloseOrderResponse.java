package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;

/**
 * 关闭订单反馈结构体
 */
public class CloseOrderResponse extends WechatResponse {
    /**
     * 微信用户开放ID
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
     * 订单号
     */
    @ProtocolField("out_trade_no")
    public String orderNo;
}
