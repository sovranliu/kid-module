package comm.xyzq.kid.common.wechat.pay.protocol;

import comm.xyzq.kid.common.wechat.pay.annotation.ProtocolField;

/**
 * 统一下单反馈结构体
 */
public class UnifiedOrderResponse extends WechatResponse {
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
    public String deviceInfo = null;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString = null;
    /**
     * 签名
     */
    @ProtocolField("sign")
    public String sign = null;
    /**
     * 交易类型
     */
    @ProtocolField("trade_type")
    public String tradeType = null;
    /**
     * 预支付交易会话标识
     */
    @ProtocolField("prepay_id")
    public String prepayId = null;
    /**
     * 二维码链接
     */
    @ProtocolField("code_url")
    public String codeUrl = null;
}
