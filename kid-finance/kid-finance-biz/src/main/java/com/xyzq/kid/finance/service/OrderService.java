package com.xyzq.kid.finance.service;

import com.xyzq.kid.common.wechat.pay.protocol.*;
import com.xyzq.kid.finance.dao.OrderDAO;
import com.xyzq.kid.finance.dao.ReceiptDAO;
import com.xyzq.kid.finance.dao.po.OrderPO;
import com.xyzq.kid.finance.dao.po.ReceiptPO;
import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.finance.service.entity.NewOrderEntity;
import com.xyzq.kid.finance.service.entity.OrderEntity;
import com.xyzq.kid.finance.service.entity.PaidOrderEntity;
import com.xyzq.kid.finance.service.exception.OrderExistException;
import com.xyzq.kid.finance.service.exception.WechatResponseException;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.kid.common.wechat.pay.WechatPayHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 支付订单服务
 */
@Service
public class OrderService {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(OrderService.class);
    /**
     * 订单数据访问对象
     */
    @Autowired
    private OrderDAO orderDAO;
    /**
     * 收款数据访问对象
     */
    @Autowired
    private ReceiptDAO receiptDAO;
    /**
     * 支付监听器
     */
    private PayListener payListener;


    /**
     * 生成订单
     *
     * @param orderNo 订单号，全局不重复，传递null表示由系统生成
     * @param openId 微信用户开放IP
     * @param goodsTitle 商品标题
     * @param goodsType 商品类型
     * @param fee 支付费用
     * @param ip 用户IP
     * @return 订单信息
     */
    public NewOrderEntity createOrder(String orderNo, String openId, String goodsTitle, int goodsType, int fee, String ip) throws OrderExistException, IOException, WechatResponseException {
        logger.info("OrderService.createOrder(" + orderNo + ", " + openId + ", " + goodsType + ", " + fee + ", " + ip + ")");
        if(Text.isBlank(orderNo)) {
            orderNo = generateOrderNo();
        }
        else {
            OrderPO orderPO = orderDAO.loadByOrderNo(orderNo);
            if(null != orderPO) {
                throw new OrderExistException(orderNo);
            }
        }
        // 记录请求
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderNo(orderNo);
        orderPO.setOpenId(openId);
        orderPO.setFee(fee);
        orderPO.setGoodsType(goodsType);
        orderDAO.insertOrder(orderPO);
        // 发起请求
        UnifiedOrderRequest unifiedOrderRequest = UnifiedOrderRequest.build(orderNo, openId, goodsTitle, goodsType, fee, ip);
        UnifiedOrderResponse unifiedOrderResponse = WechatPayHelper.unifiedOrder(unifiedOrderRequest);
        if(!unifiedOrderResponse.isSuccessful()) {
            throw new WechatResponseException("createOrder", unifiedOrderRequest.toString(), unifiedOrderResponse.toString());
        }
        orderPO.setPrepayId(unifiedOrderResponse.prepayId);
        int count = orderDAO.updateOrderSuccess(orderNo, unifiedOrderResponse.prepayId);
        if(1 != count) {
            throw new RuntimeException("update order status return unexpected count : " + count + ", orderNo = " + orderNo + ", prepayId = " + unifiedOrderResponse.prepayId);
        }
        NewOrderEntity result = new NewOrderEntity();
        result.orderNo = orderPO.getOrderNo();
        result.openId = orderPO.getOpenId();
        result.fee = orderPO.getFee();
        result.goodsType = orderPO.getGoodsType();
        result.prepayId = orderPO.getPrepayId();
        result.state = OrderEntity.STATE_PAYING;
        result.makeSign();
        return result;
    }

    /**
     * 通过订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    public OrderEntity queryOrder(String orderNo) throws IOException {
        OrderPO orderPO = orderDAO.loadByOrderNo(orderNo);
        if(null == orderPO) {
            // 订单不存在
            return null;
        }
        ReceiptPO receiptPO = receiptDAO.loadByOrderNo(orderNo);
        if(null != receiptPO) {
            // 1：已支付，2：待退款，3：已退款
            PaidOrderEntity paidOrderEntity = new PaidOrderEntity();
            convert(orderPO, paidOrderEntity);
            convert(receiptPO, paidOrderEntity);
            return paidOrderEntity;
        }
        // 远程查询订单信息
        QueryOrderRequest queryOrderRequest = QueryOrderRequest.build(orderNo, null);
        QueryOrderResponse queryOrderResponse = WechatPayHelper.queryOrder(queryOrderRequest);
        if(!queryOrderResponse.isSuccessful()) {
            throw new WechatResponseException("queryOrder", queryOrderRequest.toString(), queryOrderResponse.toString());
        }
        return null;
    }

    /**
     * 通过订单号关闭订单
     *
     * @param orderNo 订单号
     */
    public void closeOrder(String orderNo) throws IOException, WechatResponseException {
        OrderPO orderPO = orderDAO.loadByOrderNo(orderNo);
        if(null == orderPO) {
            // 订单不存在
            return;
        }
        if(1 != orderPO.getState()) {
            // 订单状态不允许关闭
            return;
        }
        CloseOrderRequest closeOrderRequest = CloseOrderRequest.build(orderNo);
        CloseOrderResponse closeOrderResponse = WechatPayHelper.closeOrder(closeOrderRequest);
        if(closeOrderResponse.isSuccessful()) {
            int count = orderDAO.updateOrderClosed(orderNo);
            if(1 == count) {
                return;
            }
            logger.error("set order state closed, but changed " + count + " records");
        }
        else {
            throw new WechatResponseException("queryOrder", closeOrderRequest.toString(), closeOrderResponse.toString());
        }
    }

    /**
     * 支付通知
     *
     * @param protocol 通知协议体
     * @return 通知处理是否成功，false表示让微信后续重复通知
     */
    public boolean notifyPayment(String protocol) throws IOException {
        logger.info("receive notifyPayment:\n" + protocol);
        PayNotify payNotify = WechatPayHelper.onPayNotify(protocol);
        if(!payNotify.isValid()) {
            return false;
        }
        ReceiptPO receiptPO = new ReceiptPO();
        receiptDAO.insertReceipt(receiptPO);
        if(1 == orderDAO.updateOrderPaid(payNotify.tradeNo)) {
            if(null != payListener) {
                try {
                    payListener.onPay(payNotify.tradeNo, payNotify.openId, Integer.parseInt(payNotify.attach), payNotify.totalFee);
                }
                catch (Exception ex) {
                    logger.error("pay callback failed, payNotify = \n" + payNotify, ex);
                }
            }
        }
        return true;
    }

    /**
     * 生成不重复订单号
     *
     * @return 订单号
     */
    public static synchronized String generateOrderNo() {
        StringBuilder builder = new StringBuilder();
        builder.append(DateTime.now().toLong());
        builder.append(Serial.makeLoopInteger());
        builder.append(Serial.makeRandomString(32));
        return builder.substring(0, 32);
    }

    /**
     * 类型转换
     *
     * @param orderPO 订单持久层对象
     * @param orderEntity 订单信息
     */
    private void convert(OrderPO orderPO, OrderEntity orderEntity) {
        orderEntity.orderNo = orderPO.getOrderNo();
        orderEntity.openId = orderPO.getOpenId();
        orderEntity.fee = orderPO.getFee();
        orderEntity.goodsType = orderPO.getGoodsType();
        orderEntity.prepayId = orderPO.getPrepayId();
        orderEntity.state = orderPO.getState();
    }

    /**
     * 类型转换
     *
     * @param receiptPO 收款持久层对象
     * @param paidOrderEntity 已付订单信息
     */
    private void convert(ReceiptPO receiptPO, PaidOrderEntity paidOrderEntity) {
        paidOrderEntity.transactionId = receiptPO.getTransactionId();
        paidOrderEntity.amount = receiptPO.getAmount();
    }
}
