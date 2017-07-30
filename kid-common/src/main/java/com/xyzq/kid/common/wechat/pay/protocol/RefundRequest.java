package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.kid.common.wechat.utility.WechatConfig;

/**
 * 退款请求结构体
 */
public class RefundRequest extends WechatRequest {
    /**
     * APP ID
     */
    @ProtocolField("appid")
    public String appId = null;
    /**
     * 商户号
     */
    @ProtocolField("mch_id")
    public String merchantId = null;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString = null;
    /**
     * 签名
     */
    public String sign = null;
    /**
     * 签名方式
     */
    @ProtocolField("sign_type")
    public String signType;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId = null;
    /**
     * 订单号
     */
    @ProtocolField("out_trade_no")
    public String tradeNo = null;
    /**
     * 商户退款单号
     */
    @ProtocolField("out_refund_no")
    public String refundTradeNo = null;
    /**
     * 订单金额
     */
    @ProtocolField("total_fee")
    public int totalFee;
    /**
     * 退款金额
     */
    @ProtocolField("refund_fee")
    public int refundFee;
    /**
     * 货币种类
     */
    @ProtocolField("refund_fee_type")
    public String refundFeeType;
    /**
     * 退款原因
     */
    @ProtocolField("refund_desc")
    public String refundDescription;


    /**
     * 构建退款请求结构体
     *
     * @param tradeNo 交易流水号
     * @param refundTradeNo 退款交易流水号
     * @param transactionId 交易号
     * @param totalFee 交易金额（分）
     * @param refundFee 退款金额（分）
     * @param reason 退款原因
     * @return 退款请求结构体
     */
    public static RefundRequest build(String tradeNo, String refundTradeNo, String transactionId, int totalFee, int refundFee, String reason) {
        RefundRequest result = new RefundRequest();
        result.appId = WechatConfig.appId;
        result.merchantId = WechatConfig.merchantId;
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signType = "MD5";
        result.transactionId = transactionId;
        result.tradeNo = tradeNo;
        result.refundTradeNo = refundTradeNo;
        result.totalFee = totalFee;
        result.refundFee = refundFee;
        result.refundFeeType = "CNY";
        result.refundDescription = reason;
        return result;
    }
}
