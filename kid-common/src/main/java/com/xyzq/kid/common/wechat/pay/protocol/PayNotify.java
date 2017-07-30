package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;

/**
 * 支付通知
 */
public class PayNotify extends WechatNotify {
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
     * 设备号
     */
    @ProtocolField("device_info")
    public String deviceInfo;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString;
    /**
     * 签名类型
     */
    @ProtocolField("sign_type")
    public String signType;
    /**
     * 微信用户开放ID
     */
    @ProtocolField("openid")
    public String openId;
    /**
     * 是否关注公众账号
     */
    @ProtocolField("is_subscribe")
    public boolean isSubscribe = false;
    /**
     * 交易类型
     */
    @ProtocolField("trade_type")
    public String tradeType;
    /**
     * 付款银行
     */
    @ProtocolField("bank_type")
    public String bankType;
    /**
     * 订单金额
     */
    @ProtocolField("total_fee")
    public int totalFee;
    /**
     * 货币种类
     */
    @ProtocolField("fee_type")
    public String feeType;
    /**
     * 现金支付金额
     */
    @ProtocolField("cash_fee")
    public int cashFee;
    /**
     * 现金支付货币类型
     */
    @ProtocolField("cash_fee_type")
    public String cashFeeType;
    /**
     * 总代金券金额
     */
    @ProtocolField("coupon_fee")
    public int couponFee;
    /**
     * 代金券使用数量
     */
    @ProtocolField("coupon_count")
    public int couponCount;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 商户订单号
     */
    @ProtocolField("out_trade_no")
    public String tradeNo;
    /**
     * 商家数据包
     */
    @ProtocolField("attach")
    public String attach;
    /**
     * 支付完成时间
     */
    @ProtocolField("time_end")
    public String endTime;
}
