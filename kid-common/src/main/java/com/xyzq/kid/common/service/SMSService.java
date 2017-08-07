package com.xyzq.kid.common.service;

import com.xyzq.simpson.bart.client.BartClient;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.io.net.http.HttpClient;
import com.xyzq.simpson.base.io.net.http.HttpRequest;
import com.xyzq.simpson.base.io.net.http.HttpResponse;
import com.xyzq.simpson.base.io.net.http.body.HttpURLEncodedBody;
import com.xyzq.simpson.base.io.net.http.filter.HttpTextFilter;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.base.type.core.ILink;
import com.xyzq.simpson.base.type.core.ITable;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信发送服务
 */
@Service("smsService")
public class SMSService {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(SMSService.class);
    /**
     * 短信平台接口地址
     */
    @Value("${kid.sms.url}")
    protected String kid_sms_url;
    /**
     * 短信平台帐号
     */
    @Value("${kid.sms.username}")
    protected String kid_sms_username;
    /**
     * 短信平台密码
     */
    @Value("${kid.sms.password}")
    protected String kid_sms_password;
    /**
     * 缓存访问对象
     *
     * 缓存中内容为：code-[rekey] -> [captcha]
     */
    @Resource(name = "cache")
    protected ITimeLimitedCache<String, String> cache;


    /**
     * 发送短信
     *
     * @param mobileNo 目标手机号码
     * @param templateName 短信模版名称
     * @param data 模版占位符数据映射
     * @return 短信内容
     */
    public String sendSMS(String mobileNo, String templateName, ITable<String, String> data) {
        String template = BartClient.instance().fetch("kid.sms.template." + templateName);
        if(null == template) {
            logger.error("invalid template : " + templateName);
            return null;
        }
        String content = template;
        for(ILink<String, String> link : data) {
            content = content.replace("[" + link.origin() + "]", link.destination());
        }
        while(true) {
            int i = content.indexOf("[");
            if(-1 == i) {
                break;
            }
            int j = content.indexOf("]", i);
            if(-1 == j) {
                break;
            }
            content = content.replace(content.substring(i, j + 1), "");
        }
        HttpRequest request = new HttpRequest("POST");
        HttpURLEncodedBody body = new HttpURLEncodedBody();
        body.put("username", kid_sms_username);
        body.put("password", kid_sms_password);
        body.put("destAddr", mobileNo);
        body.put("content", content);
        body.put("reqReport", false);
        request.body = body;
        String responseText = null;
        HttpClient client = new HttpClient();
        try {
            HttpResponse response = client.invoke(kid_sms_url, request);
            HttpTextFilter filter = new HttpTextFilter();
            responseText = filter.filter(response);
            logger.info("sms = " + kid_sms_url  + "\ndestAddr = " + mobileNo + " \nresponse = " + responseText);
        }
        catch (Exception e) {
            logger.error("call sms platform failed : \nurl = " + kid_sms_url + "\ntemplate = " + templateName + "\nresponse = " + responseText, e);
        }
        return content;
    }

    /**
     * 发送短信验证码
     *
     * @param mobileNo 目标手机号码
     * @param templateName 短信模版名称
     * @param data 模版占位符数据映射
     * @return 短信内容
     */
    public String sendSMSCaptcha(String mobileNo, String templateName, ITable<String, String> data) {
        String captcha = Serial.makeRandomString("0123456789", 4);
        cache.set("code-" + mobileNo, captcha, 1000 * 60 * 10);
        // captcha 是写死的验证码占位符
        if(null == data) {
            data = new Table<String, String>();
        }
        data.put("captcha", captcha);
        return sendSMS(mobileNo, templateName, data);
    }

    /**
     * 验证用户提交的验证码是否正确
     *
     * @param rekey 验证码键、可以为手机号码
     * @param value 用户提交的验证码内容
     * @return 验证结果
     */
    public boolean checkCaptcha(String rekey, String value) {
        return ("" + value).equalsIgnoreCase(cache.get("code-" + rekey));
    }
}
