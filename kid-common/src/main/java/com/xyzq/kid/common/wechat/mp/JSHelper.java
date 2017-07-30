package com.xyzq.kid.common.wechat.mp;

import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.kid.common.wechat.utility.WechatHelper;

/**
 * JS 帮助类
 */
public class JSHelper {
    /**
     * 隐藏构造函数
     */
    private JSHelper() { }

    /**
     * 获取JS配置对象
     *
     * @param url 调用URL
     * @return JS配置对象
     */
    public static JSConfig fetchConfig(String url) {
        long tick = DateTime.now().toLong();
        JSConfig result = new JSConfig();
        result.appId = WechatConfig.appId;
        result.timestamp = String.valueOf(tick);
        result.url = url;
        result.jsTicket = JSTicketHelper.getTicket();
        result.nonceString = Serial.makeRandomString(32).toUpperCase();
        result.signature = WechatHelper.makeJSSign(result.jsTicket, result.nonceString, tick, result.url);
        return result;
    }
}
