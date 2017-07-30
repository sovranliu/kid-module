package com.xyzq.kid.common.action;

import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;

/**
 * 登录用户动作范例
 */
public class CustomerDemoAction extends CustomerAction {
    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String execute(Visitor visitor, Context context) throws Exception {
        // 这里为父类处理请求逻辑，若父类返回了模版名表示父类要求子类不再继续处理
        String result = super.execute(visitor, context);
        if(null != result) {
            return result;
        }
        // 这里是从上下文中提取当前用户的手机号码和微信用户开放ID
        String mobileNo = (String) context.get(CONTEXT_KEY_MOBILENO);
        String openId = (String) context.get(CONTEXT_KEY_OPENID);
        // TODO:这里是本类应该处理的逻辑
        return "success.json";
    }
}
