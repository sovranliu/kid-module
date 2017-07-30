package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.kid.common.wechat.utility.WechatHelper;

/**
 * 微信通知
 */
public class WechatNotify extends WechatResponse {
    /**
     * 签名
     */
    @ProtocolField("sign")
    public String sign;


    /**
     * 判断通知是否合法
     *
     * @return 通知是否合法
     */
    public boolean isValid() {
        return WechatHelper.makeSign(toXML()).equals(sign);
    }
}
