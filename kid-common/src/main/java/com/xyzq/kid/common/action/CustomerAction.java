package com.xyzq.kid.common.action;

import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.simpson.maggie.framework.action.core.IAction;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;

import javax.annotation.Resource;

/**
 * 登录用户动作基类
 */
public class CustomerAction implements IAction {
    /**
     * 手机号码在上下文中的键
     */
    public final static String CONTEXT_KEY_MOBILENO = "mobileNo";
    /**
     * 用户微信开放ID在上下文中的键
     */
    public final static String CONTEXT_KEY_OPENID = "openId";


    /**
     * 缓存访问对象
     *
     * 缓存中内容为：mobileNo,openId
     */
    @Resource(name = "cache")
    private ITimeLimitedCache<String, String> cache;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        String sId = visitor.cookie("sid");
        if(Text.isBlank(sId)) {
            context.set("code", "-9");
            context.set("action", "login");
            return "success.json";
        }
        String info = cache.get(sId);
        if(Text.isBlank(info) || !info.contains(",")) {
            context.set("code", "-8");
            context.set("action", "login");
            return "success.json";
        }
        String mobileNo = info.split(",")[0].trim();
        String openId = info.split(",")[1].trim();
        context.put(CONTEXT_KEY_MOBILENO, mobileNo);
        context.put(CONTEXT_KEY_OPENID, openId);
        return null;
    }
}
