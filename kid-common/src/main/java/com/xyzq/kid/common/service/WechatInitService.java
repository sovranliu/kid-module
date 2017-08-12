package com.xyzq.kid.common.service;

import com.xyzq.kid.common.wechat.utility.WechatConfig;
import com.xyzq.simpson.base.character.Encoding;
import com.xyzq.simpson.base.json.JSONObject;
import com.xyzq.simpson.base.json.JSONVisitor;
import com.xyzq.simpson.base.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 微信初始化服务
 */
public class WechatInitService {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(WechatInitService.class);


    /**
     * 微信初始化
     */
    public void init() throws Exception {
//        String confText = Text.loadFile(new File("/apps/data/kid-secret/wechat.json"), Encoding.ENCODING_UTF8);
//        JSONVisitor visitor = new JSONVisitor(JSONObject.convertFromString(confText));
//        WechatConfig.appId = visitor.getString("appId");
//        WechatConfig.appSecret = visitor.getString("appSecret");
//        WechatConfig.merchantId = visitor.getString("merchantId");
//        WechatConfig.apiKey = visitor.getString("apiKey");
//        WechatConfig.apiPassword = visitor.getString("apiPassword");
//        WechatConfig.notiyUrl = visitor.getString("notiyUrl");
//        WechatConfig.token = visitor.getString("token");
        logger.info("WechatInitService.init() finished");
    }
}
