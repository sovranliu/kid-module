package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.time.Duration;
import com.xyzq.kid.common.wechat.utility.WechatConfig;

/**
 * 统一下单请求结构体
 */
public class UnifiedOrderRequest extends WechatRequest {
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
     * 签名
     */
    public String signature;
    /**
     * 签名方式
     */
    @ProtocolField("sign_type")
    public String signType;
    /**
     * 商品描述
     */
    @ProtocolField("body")
    public String body;
    /**
     * 商品详情
     */
    @ProtocolField("detail")
    public String detail;
    /**
     * 附加数据
     */
    @ProtocolField("attach")
    public String attach;
    /**
     * 商户订单号
     */
    @ProtocolField("out_trade_no")
    public String tradeNo;
    /**
     * 标价币种
     */
    @ProtocolField("fee_type")
    public String feeType;
    /**
     * 标价金额
     */
    @ProtocolField("total_fee")
    public int totalFee;
    /**
     * 终端IP
     */
    @ProtocolField("spbill_create_ip")
    public String creatorIp;
    /**
     * 交易起始时间
     */
    @ProtocolField("time_start")
    public String startTime;
    /**
     * 交易结束时间
     */
    @ProtocolField("time_expire")
    public String expireTime;
    /**
     * 订单优惠标记
     */
    @ProtocolField("goods_tag")
    public String goodsTag;
    /**
     * 通知地址
     */
    @ProtocolField("notify_url")
    public String notifyUrl;
    /**
     * 交易类型
     */
    @ProtocolField("trade_type")
    public String tradeType;
    /**
     * 商品ID
     */
    @ProtocolField("product_id")
    public String productId;
    /**
     * 指定支付方式
     */
    @ProtocolField("limit_pay")
    public String limitPay;
    /**
     * 用户标识
     */
    @ProtocolField("openid")
    public String openId;


    /**
     * 构建统一下单请求结构体
     *
     * @param tradeNo 订单号
     * @param openId 微信用户开放IP
     * @param goodsTitle 商品标题
     * @param goodsType 商品类型
     * @param fee 支付费用
     * @param ip 用户IP
     * @return 统一下单请求结构体
     */
    public static UnifiedOrderRequest build(String tradeNo, String openId, String goodsTitle, int goodsType, int fee, String ip) {
        UnifiedOrderRequest result = new UnifiedOrderRequest();
        result.appId = WechatConfig.appId;
        result.merchantId = WechatConfig.merchantId;
        result.deviceInfo = "WEB";
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signType = "MD5";
        result.body = goodsTitle;
        result.attach = String.valueOf(goodsType);
        result.tradeNo = tradeNo;
        result.totalFee = fee;
        result.creatorIp = ip;
        DateTime startTime = DateTime.now();
        result.startTime = startTime.toString("yyyyMMddHHmmss");
        startTime = startTime.add(Duration.createHours(2));
        result.expireTime = startTime.toString("yyyyMMddHHmmss");
        result.notifyUrl = WechatConfig.notiyUrl;
        result.tradeType = "JSAPI";
        result.limitPay = "no_credit";
        result.openId = openId;
        return result;
    }
}
