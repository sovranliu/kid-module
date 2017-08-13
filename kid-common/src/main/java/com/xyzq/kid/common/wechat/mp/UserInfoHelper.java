package com.xyzq.kid.common.wechat.mp;

import com.xyzq.simpson.base.io.net.http.HttpHelper;
import com.xyzq.simpson.base.json.JSONArray;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.kid.common.wechat.utility.XMLHttpsUtil;
import com.xyzq.simpson.base.json.JSONString;
import com.xyzq.simpson.base.json.JSONVisitor;
import com.xyzq.simpson.base.type.List;
import com.xyzq.simpson.base.type.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 访问票据管理类
 */
public class UserInfoHelper {
    /**
     * 游客信息
     */
    public static class GuestInfo {
        /**
         * 微信用户开放ID
         */
        public String openId;
    }

    /**
     * 会员信息
     */
    public static class MemberInfo extends GuestInfo {
        /**
         * 昵称
         */
        public String nickName;
        /**
         * 性别：1时是男性，值为2时是女性，值为0时是未知
         */
        public int sex;
        /**
         * 城市
         */
        public String city;
        /**
         * 头像图片URL
         */
        public String headImgUrl;
        /**
         * 时间戳
         */
        public long subscribeTime;
    }


    /**
     * 获取用户信息URL
     */
    private final static String URL_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=[ACCESS_TOKEN]&openid=[OPENID]&lang=zh_CN";
    /**
     * 批量获取用户信息URL
     */
    private final static String URL_BATCHGET = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=[ACCESS_TOKEN]#";
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(UserInfoHelper.class);


    /**
     * 隐藏构造函数
     */
    private UserInfoHelper() { }

    /**
     * 判断是否关注公众号
     *
     * @param openId 用户开放ID
     * @return 有效票据
     */
    public static boolean isFans(String openId) throws IOException {
        GuestInfo guestInfo = fetchUserInfo(openId);
        if(null == guestInfo) {
            logger.error("isFan is failed and try adjust true");
            return true;
        }
        if(guestInfo instanceof MemberInfo) {
            logger.info(openId + " is fans");
            return true;
        }
        else {
            logger.info(openId + " is not fans");
            return false;
        }
    }

    /**
     * 提取用户信息
     *
     * @param openId 微信用户开放ID
     * @return 用户信息实体
     */
    public static GuestInfo fetchUserInfo(String openId) throws IOException {
        String oldToken = TokenHelper.getToken();
        String url = URL_USERINFO.replace("[ACCESS_TOKEN]", oldToken).replace("[OPENID]", openId);
        String result = XMLHttpsUtil.get(url);
        logger.info("fetch user " + openId + " information :\n" + result);
        JSONVisitor visitor = new JSONVisitor(JSONObject.convert(result));
        if("40001".equals(visitor.getString("errcode"))) {
            String newToken = TokenHelper.fetchToken();
            if(!oldToken.equals(newToken)) {
                result = XMLHttpsUtil.get(url);
                logger.info("refetch user " + openId + " information :\n" + result);
                visitor = new JSONVisitor(JSONObject.convert(result));
                if(!"40001".equals(visitor.getString("errcode"))) {
                    return convert(visitor);
                }
            }
        }
        else {
            return convert(visitor);
        }
        logger.error("fetch user info failed");
        return null;
    }

    /**
     * 批量提取用户信息
     *
     * @param openIds 微信用户开放ID列表
     * @return 用户信息实体列表
     */
    public static List<GuestInfo> fetchUserInfo(List<String> openIds) throws Exception {
        String url = URL_BATCHGET.replace("[ACCESS_TOKEN]", TokenHelper.getToken());
        Table<String, Object> parameters = new Table<String, Object>();
        JSONArray jsonArray = new JSONArray();
        for(String openId : openIds) {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("openid", new JSONString(openId));
            jsonItem.put("lang", new JSONString("zh_CN"));
            jsonArray.add(jsonItem);
        }
        JSONObject jsonParameter = new JSONObject();
        jsonParameter.put("user_list", jsonArray);
        parameters.put("", jsonParameter.toString());
        String response = HttpHelper.invoke(url, parameters);
        JSONVisitor visitorResponse = new JSONVisitor(JSONObject.convertFromString(response));
        if(null == visitorResponse.getVisitors("user_info_list")) {
            throw new RuntimeException("wechat response error : " + response);
        }
        List<GuestInfo> result = new List<GuestInfo>();
        for(JSONVisitor visitorItem : visitorResponse.getVisitors("user_info_list")) {
            GuestInfo guestInfo = convert(visitorItem);
            if(null == guestInfo) {
                continue;
            }
            result.add(guestInfo);
        }
        return result;
    }

    /**
     * JSON转用户信息
     *
     * @param visitor JSON访问者
     * @return 用户信息
     */
    private static GuestInfo convert(JSONVisitor visitor) {
        if(0 == visitor.getInteger("subscribe", 0)) {
            // 未关注公众号
            GuestInfo guestInfo = new GuestInfo();
            guestInfo.openId = visitor.getString("openid");
            return guestInfo;
        }
        else {
            // 已经关注公众号
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.openId = visitor.getString("openid");
            memberInfo.nickName = visitor.getString("nickname");
            memberInfo.sex = visitor.getInteger("sex", 0);
            memberInfo.city = visitor.getString("city");
            memberInfo.headImgUrl = visitor.getString("headimgurl");
            if(null != memberInfo.headImgUrl && memberInfo.headImgUrl.endsWith("0")) {
                memberInfo.headImgUrl = memberInfo.headImgUrl.substring(0, memberInfo.headImgUrl.length() - 1) + "132";
            }
            memberInfo.subscribeTime = visitor.getLong("subscribe_time", 0);
            return memberInfo;
        }
    }
}
