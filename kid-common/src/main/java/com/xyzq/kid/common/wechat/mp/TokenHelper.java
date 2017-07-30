package com.xyzq.kid.common.wechat.mp;

import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.base.json.JSONString;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.kid.common.wechat.utility.XMLHttpsUtil;
import org.apache.log4j.Logger;

/**
 * 访问票据管理类
 */
public class TokenHelper {
    /**
     * 获取凭据URL
     */
    private final static String URL_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=[APPID]&secret=[APPSECRET]";
    /**
     * 缓存周期
     */
    public final static long PERIOD_CACHE = 3600 * 1000;
    /**
     * 日志对象
     */
    private static Logger logger = Logger.getLogger("pay");
    /**
     * 缓存的票据
     */
    private static String token = null;
    /**
     * 缓存时间
     */
    private static long tick = 0;


    /**
     * 隐藏构造函数
     */
    private TokenHelper() { }


    /**
     * 获取票据
     *
     * @return 有效票据
     */
    public static String getToken() {
        if(DateTime.now().toLong() > PERIOD_CACHE + tick) {
            String newToken = fetchToken();
            if(null != newToken) {
                token = newToken;
            }
        }
        return token;
    }

    /**
     * 获取票据
     *
     * @return 有效票据
     */
    public synchronized static String fetchToken() {
        if(DateTime.now().toLong() < PERIOD_CACHE + tick) {
            return token;
        }
        String url = URL_ACCESSTOKEN.replace("[APPID]", WechatConfig.appId).replace("[APPSECRET]", WechatConfig.appSecret);
        String result = null;
        try {
            result = XMLHttpsUtil.get(url);
        }
        catch(Exception ex) {
            logger.error("call HttpUtil.send(" + url + ") failed", ex);
            return null;
        }
        JSONObject json = JSONObject.convert(result);
        if(null == json) {
            logger.error("empty response in HttpUtil.send(" + url + ")");
            return null;
        }
        JSONString token = (JSONString) json.get("access_token");
        if(null == token) {
            logger.error("error response in HttpUtil.send(" + url + "), " + token);
            return null;
        }
        tick = DateTime.now().toLong();
        return token.get();
    }
}
