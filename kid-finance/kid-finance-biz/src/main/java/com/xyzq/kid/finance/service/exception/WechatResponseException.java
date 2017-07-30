package com.xyzq.kid.finance.service.exception;

/**
 * 微信反馈异常
 */
public class WechatResponseException extends RuntimeException {
    /**
     * 接口名称
     */
    public String interfaceName;
    /**
     * 请求内容
     */
    public String requestContent;
    /**
     * 反馈内容
     */
    public String responseContent;


    /**
     * 构造函数
     *
     * @param interfaceName 接口名称
     * @param requestContent 请求内容
     * @param responseContent 反馈内容
     */
    public WechatResponseException(String interfaceName, String requestContent, String responseContent) {
        super("invoke tencent service '" + interfaceName + "' failed, request :\n" + requestContent + " response :\n" + responseContent);
        this.interfaceName = interfaceName;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
    }
}
