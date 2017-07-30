package comm.xyzq.kid.common.wechat.pay;

import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.xml.XMLNode;
import comm.xyzq.kid.common.wechat.pay.protocol.*;
import comm.xyzq.kid.common.wechat.utility.XMLHttpsUtil;

import java.io.IOException;

/**
 * 微信支付帮助类
 */
public class WechatPayHelper {
    /**
     * 统一下单
     */
    public final static String URL_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 查询下单
     */
    public final static String URL_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    /**
     * 退款
     */
    public final static String URL_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";


    /**
     * 隐藏构造函数
     */
    private WechatPayHelper() { }

    /**
     * 统一下单
     *
     * @param request 统一下单参数
     * @return 统一下单结果
     */
    public static UnifiedOrderResponse unifiedOrder(UnifiedOrderRequest request) throws IOException {
        String response = XMLHttpsUtil.post(URL_UNIFIEDORDER, request.toString());
        if(Text.isBlank(response)) {
            throw new IOException("receive empty data in unifiedOrder");
        }
        XMLNode xml = XMLNode.convert(response);
        if(null == xml) {
            throw new IOException("parse data failed in unifiedOrder:" + response);
        }
        UnifiedOrderResponse result = new UnifiedOrderResponse();
        if(!result.parse(xml)) {
            return null;
        }
        return result;
    }

    /**
     * 订单查询
     *
     * @param request 订单查询参数
     * @return 订单查询结果
     */
    public static QueryOrderResponse queryOrder(QueryOrderRequest request) throws IOException {
        String response = XMLHttpsUtil.post(URL_ORDERQUERY, request.toString());
        if(Text.isBlank(response)) {
            throw new IOException("receive empty data in queryOrder");
        }
        XMLNode xml = XMLNode.convert(response);
        if(null == xml) {
            throw new IOException("parse data failed in queryOrder:" + response);
        }
        QueryOrderResponse result = new QueryOrderResponse();
        if(!result.parse(xml)) {
            return null;
        }
        return result;
    }

    /**
     * 退款
     *
     * @param request 退款参数
     * @return 退款结果
     */
    public static RefundResponse refund(RefundRequest request) throws IOException {
        String response = XMLHttpsUtil.post(URL_REFUND, request.toString());
        if(Text.isBlank(response)) {
            throw new IOException("receive empty data in refund");
        }
        XMLNode xml = XMLNode.convert(response);
        if(null == xml) {
            throw new IOException("parse data failed in refund:" + response);
        }
        RefundResponse result = new RefundResponse();
        if(!result.parse(xml)) {
            return null;
        }
        return result;
    }
}
