package com.xyzq.kid.finance.service;

import com.xyzq.kid.common.wechat.pay.protocol.*;
import com.xyzq.kid.finance.dao.OrderDAO;
import com.xyzq.kid.finance.dao.ReceiptDAO;
import com.xyzq.kid.finance.dao.po.OrderInfoPO;
import com.xyzq.kid.finance.dao.po.OrderPO;
import com.xyzq.kid.finance.dao.po.ReceiptPO;
import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.finance.service.entity.*;
import com.xyzq.kid.finance.service.exception.OrderExistException;
import com.xyzq.kid.finance.service.exception.WechatResponseException;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.model.Page;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.kid.common.wechat.pay.WechatPayHelper;
import com.xyzq.simpson.base.time.Duration;
import com.xyzq.simpson.base.type.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource(name = "payListener")
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
     * @param tag 附属数据
     * @return 订单信息
     */
    public NewOrderEntity createOrder(String orderNo, String openId, String goodsTitle, int goodsType, int fee, String ip, String tag) throws OrderExistException, IOException, WechatResponseException {
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
        orderPO.setTag(tag);
        orderDAO.insertOrder(orderPO);
        logger.info("insert into order, orderNo = " + orderNo);
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
        result.tag = tag;
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
        if(0 == orderPO.getState() || 5 == orderPO.getState() ) {
            // 未支付
            OrderEntity orderEntity = new OrderEntity();
            convert(orderPO, orderEntity);
            return orderEntity;
        }
        else if(1 == orderPO.getState()) {
            // 远程查询订单信息
            QueryOrderRequest queryOrderRequest = QueryOrderRequest.build(orderNo, null);
            QueryOrderResponse queryOrderResponse = WechatPayHelper.queryOrder(queryOrderRequest);
            if(!queryOrderResponse.isSuccessful()) {
                throw new WechatResponseException("queryOrder", queryOrderRequest.toString(), queryOrderResponse.toString());
            }
            if("SUCCESS".equals(queryOrderResponse.state)) {
                logger.info("order query success, orderNo = " + orderNo);
                // 支付成功
                ReceiptPO receiptPO = null;
                if(null == receiptDAO.loadByOrderNo(orderNo)) {
                    receiptPO = new ReceiptPO();
                    receiptPO.setOrderNo(orderNo);
                    receiptPO.setOpenId(queryOrderResponse.openId);
                    receiptPO.setAmount(queryOrderResponse.fee);
                    receiptPO.setTransactionId(queryOrderResponse.transactionId);
                    receiptPO.setMode("query");
                    receiptPO.setDeleted(false);
                    receiptDAO.insertReceipt(receiptPO);
                    logger.info("insert receipt when query, orderNo = " + orderNo);
                }
                else {
                    receiptPO = receiptDAO.loadByOrderNo(orderNo);
                }
                orderDAO.updateOrderPaid(orderNo);
                // 已经支付
                PaidOrderEntity paidOrderEntity = new PaidOrderEntity();
                convert(orderPO, paidOrderEntity);
                convert(receiptPO, paidOrderEntity);
                return paidOrderEntity;
            }
            else if("NOTPAY".equals(queryOrderResponse.state) || "USERPAYING".equals(queryOrderResponse.state)) {
                // 未支付
                if(orderPO.getCreateTime().getTime() < DateTime.now().toLong() - Duration.MINUTE_MILLIS * 10) {
                    logger.info("query order and try close, orderNo = " + orderNo);
                    // 超过十分钟进行关单
                    closeOrder(orderNo);
                    orderPO.setState(5);
                }
                else {
                    logger.info("query order and not close, orderNo = " + orderNo);
                }
                OrderEntity orderEntity = new OrderEntity();
                convert(orderPO, orderEntity);
                return orderEntity;
            }
            else if("REVOKED".equals(queryOrderResponse.state) || "CLOSED".equals(queryOrderResponse.state) || "PAYERROR".equals(queryOrderResponse.state)) {
                // 未支付且无法再支付
                logger.info("query order and set state closed, orderNo = " + orderNo);
                orderDAO.updateOrderClosed(orderNo);
                OrderEntity orderEntity = new OrderEntity();
                convert(orderPO, orderEntity);
                return orderEntity;
            }
        }
        else if(2 == orderPO.getState()) {
            // 已经支付
            ReceiptPO receiptPO = receiptDAO.loadByOrderNo(orderNo);
            PaidOrderEntity paidOrderEntity = new PaidOrderEntity();
            convert(orderPO, paidOrderEntity);
            if(null != receiptPO) {
                convert(receiptPO, paidOrderEntity);
            }
            else {
                logger.error("order is paid, but receipt not found, orderNo = " + orderNo);
            }
            return paidOrderEntity;
        }
        else if(3 == orderPO.getState() || 4 == orderPO.getState()) {
            // 退款
            RefundOrderEntity refundOrderEntity = new RefundOrderEntity();
            convert(orderPO, refundOrderEntity);
            ReceiptPO receiptPO = receiptDAO.loadByOrderNo(orderNo);
            if(null != receiptPO) {
                convert(receiptPO, refundOrderEntity);
            }
            else {
                logger.error("order is refund, but receipt not found, orderNo = " + orderNo);
            }
            return refundOrderEntity;
        }
        throw new RuntimeException("unexpected state of order, state = " + orderPO.getState());
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
        receiptPO.setOrderNo(payNotify.tradeNo);
        receiptPO.setOpenId(payNotify.openId);
        receiptPO.setAmount(payNotify.totalFee);
        receiptPO.setTransactionId(payNotify.transactionId);
        receiptPO.setMode("notify");
        receiptDAO.insertReceipt(receiptPO);
        OrderPO orderPO = orderDAO.loadByOrderNo(payNotify.tradeNo);
        if(null != orderPO && 1 == orderDAO.updateOrderPaid(payNotify.tradeNo)) {
            if(null != payListener) {
                try {
                    payListener.onPay(payNotify.tradeNo, payNotify.openId, Integer.parseInt(payNotify.attach), payNotify.totalFee, orderPO.getTag());
                }
                catch (Exception ex) {
                    logger.error("pay callback failed, payNotify = \n" + payNotify, ex);
                }
            }
        }
        return true;
    }

    /**
     * 查询订单
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param status 状态，1：未支付，2：已支付，3：已退款
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param begin 开始索引
     * @param size 查询个数
     * @return 订单列表
     */
    public Page<OrderInfoEntity> find(String orderNo, String openId, int status, DateTime beginTime, DateTime endTime, int begin, int size) {
        java.sql.Timestamp beginTimestamp = null;
        if(null != beginTime) {
            beginTimestamp = new java.sql.Timestamp(beginTime.toLong());
        }
        java.sql.Timestamp endTimestamp = null;
        if(null != endTime) {
            endTimestamp = new java.sql.Timestamp(endTime.toLong());
        }
        java.util.List<OrderInfoPO> orderPOList = orderDAO.select(orderNo, openId, status, beginTimestamp, endTimestamp, begin, size);
        List<OrderInfoEntity> result = new List<OrderInfoEntity>();
        for(OrderInfoPO orderInfoPO : orderPOList) {
            OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
            orderInfoEntity.orderNo = orderInfoPO.getOrderNo();
            orderInfoEntity.openId = orderInfoPO.getOpenId();
            orderInfoEntity.mobileNo = orderInfoPO.getMobileNo();
            orderInfoEntity.userName = orderInfoPO.getUserName();
            orderInfoEntity.serialNo = orderInfoPO.getSerialNo();
            orderInfoEntity.tag = orderInfoPO.getTag();
            orderInfoEntity.fee = orderInfoPO.getFee();
            orderInfoEntity.goodsType = orderInfoPO.getGoodsType();
            switch(orderInfoPO.getState()) {
                case 0:
                case 1:
                case 5:
                    orderInfoEntity.status = OrderInfoEntity.STATUS_NOPAY;
                    break;
                case 2:
                    orderInfoEntity.status = OrderInfoEntity.STATUS_PAID;
                    break;
                case 3:
                case 4:
                    orderInfoEntity.status = OrderInfoEntity.STATUS_REFUND;
                    break;
            }
            orderInfoEntity.time = DateTime.parse(orderInfoPO.getCreateTime().getTime());
            // 添加
            result.add(orderInfoEntity);
        }
        Page<OrderInfoEntity> page = new Page<OrderInfoEntity>();
        page.total = orderDAO.count(orderNo, openId, status, beginTimestamp, endTimestamp);
        page.list = result;
        return page;
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
        orderEntity.tag = orderPO.getTag();
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
