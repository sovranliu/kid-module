package comm.xyzq.kid.common.wechat.mp;

/**
 * JS配置
 */
public class JSConfig {
    /**
     * APP ID
     */
    public String appId = "";
    /**
     * 时间戳
     */
    public String timestamp = null;
    /**
     * JS完整路径
     */
    public String url = null;
    /**
     * JS 凭据
     */
    public String jsTicket = null;
    /**
     * 随机字符串
     */
    public String nonceString = null;
    /**
     * 签名
     */
    public String signature = null;


    /**
     * 属性
     */
    public String appId() {
        return appId;
    }
    public String timestamp() {
        return timestamp;
    }
    public String url() {
        return url;
    }
    public String jsTicket() {
        return jsTicket;
    }
    public String nonceString() {
        return nonceString;
    }
    public String signature() {
        return signature;
    }
}
