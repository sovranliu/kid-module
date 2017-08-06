package com.xyzq.kid.common.service;

import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.base.type.core.ITable;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信发送服务
 */
@Service("smsService")
public class SMSService {
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
        // TODO:短信投递代码
        return "";
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
