package com.xyzq.kid.common.wechat.pay.protocol;

import com.xyzq.kid.common.wechat.pay.annotation.ProtocolField;
import com.xyzq.simpson.base.xml.core.IXMLNode;

import java.util.LinkedList;
import java.util.List;

/**
 * 退款查询反馈结构体
 */
public class QueryRefundResponse extends WechatResponse {
    /**
     * 单笔退款信息
     */
    public static class RefundInfo {
        /**
         * 商户退款单号
         */
        @ProtocolField("out_refund_no")
        public String refundTradeNo;
        /**
         * 微信退款单号
         */
        @ProtocolField("refund_id")
        public String refundId;
        /**
         * 退款金额
         */
        @ProtocolField("refund_fee")
        public int refundFee;
        /**
         * 退款状态
         */
        @ProtocolField("refund_status")
        public String refundStatus;
        /**
         * 退款成功时间
         */
        @ProtocolField("refund_success_time")
        public String refundTime;
    }


    /**
     * APP ID
     */
    @ProtocolField("appid")
    public String appId;
    /**
     * 商户号
     */
    @ProtocolField("mch_id")
    public String merchantId;
    /**
     * 随机字符串
     */
    @ProtocolField("nonce_str")
    public String nonceString;
    /**
     * 签名
     */
    @ProtocolField("sign")
    public String sign;
    /**
     * 微信支付订单号
     */
    @ProtocolField("transaction_id")
    public String transactionId;
    /**
     * 订单号
     */
    @ProtocolField("out_trade_no")
    public String orderNo;
    /**
     * 标价金额
     */
    @ProtocolField("total_fee")
    public int fee;
    /**
     * 现金支付金额
     */
    @ProtocolField("cash_fee")
    public int cashFee;
    /**
     * 退款笔数
     */
    @ProtocolField("refund_count")
    public int refundCount;
    /**
     * 退款信息列表
     */
    public List<RefundInfo> refundInfoList;


    /**
     * 解析XML节点
     *
     * @param node XML节点
     * @return 处理结果
     */
    @Override
    public boolean parse(IXMLNode node) {
        if(!super.parse(node)) {
            return false;
        }
        refundInfoList = new LinkedList<RefundInfo>();
        for(int i = 0; i < refundCount; i++) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.refundTradeNo = fetchChildValue(node, "out_refund_no_" + i);
            refundInfo.refundId = fetchChildValue(node, "refund_id_" + i);
            refundInfo.refundFee = Integer.parseInt(fetchChildValue(node, "refund_fee_" + i));
            refundInfo.refundStatus = fetchChildValue(node, "refund_status_" + i);
            refundInfo.refundTime = fetchChildValue(node, "refund_success_time_" + i);
            refundInfoList.add(refundInfo);
        }
        return true;
    }

    /**
     * 查找指定 XML节点下指定名称的字节
     *
     * @param node 父节点
     * @param name 子节点名
     * @return 值
     */
    private static String fetchChildValue(IXMLNode node, String name) {
        for(IXMLNode son : node.children()) {
            if(name.equals(son.getName())) {
                return son.getValue();
            }
        }
        return null;
    }
}
