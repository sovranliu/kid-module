package com.xyzq.kid.common.wechat.mp;

import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.io.net.http.HttpHelper;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.base.json.JSONVisitor;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.type.Table;

/**
 * 网页相关帮助类
 */
public class WebHelper {
    /**
     * 用户授权URL
     */
    public final static String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WechatConfig.appId + "&redirect_uri=[REDIRECT_URI]&response_type=code&scope=snsapi_base&state=[STATE]#wechat_redirect";
    /**
     * 用户口令
     */
    public final static String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WechatConfig.appId + "&secret=" + WechatConfig.appSecret + "&code=[CODE]&grant_type=authorization_code";


    /**
     * 通过Code获取微信用户开放ID
     *
     * @param code 代码
     * @return 微信用户开放ID
     */
    public static String fetchOpenId(String code) throws Exception {
        if(Text.isBlank(code)) {
            return null;
        }
        Table<String, Object> parameters = new Table<String, Object>();
        parameters.put("CODE", code);
        String response = HttpHelper.invoke(URL_ACCESS_TOKEN, parameters);
        if(Text.isBlank(response)) {
            return null;
        }
        JSONVisitor visitor = new JSONVisitor(JSONObject.convertFromString(response));
        return visitor.getString("openid");
    }
}
