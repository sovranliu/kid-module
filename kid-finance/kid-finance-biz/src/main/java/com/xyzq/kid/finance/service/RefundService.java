package com.xyzq.kid.finance.service;

import com.xyzq.kid.common.wechat.pay.WechatPayHelper;
import com.xyzq.kid.common.wechat.pay.protocol.*;
import com.xyzq.kid.finance.dao.OrderDAO;
import com.xyzq.kid.finance.dao.RefundDAO;
import com.xyzq.kid.finance.dao.po.OrderPO;
import com.xyzq.kid.finance.dao.po.RefundInfoPO;
import com.xyzq.kid.finance.dao.po.RefundPO;
import com.xyzq.kid.finance.service.entity.RefundEntity;
import com.xyzq.kid.finance.service.entity.RefundInfoEntity;
import com.xyzq.kid.finance.service.exception.OrderExistException;
import com.xyzq.kid.finance.service.exception.WechatResponseException;
import com.xyzq.simpson.base.async.Operator;
import com.xyzq.simpson.base.async.core.IOperation;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.model.Page;
import com.xyzq.simpson.base.model.core.IModule;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import com.xyzq.simpson.base.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 退款服务
 */
@Service
public class RefundService implements IModule {
    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(RefundService.class);
    /**
     * 订单数据访问对象
     */
    @Autowired
    private OrderDAO orderDAO;
    /**
     * 退款数据访问对象
     */
    @Autowired
    private RefundDAO refundDAO;
    /**
     * 是否需要停止作业
     */
    protected volatile AtomicBoolean needStop = null;


    /**
     * 初始化
     */
    @Override
    @PostConstruct
    public boolean initialize() {
        needStop = new AtomicBoolean(false);
        new Operator<Void>(new IOperation<Void>() {
            @Override
            public Void onExecute() {
                int begin = 0;
                while(true) {
                    // 执行退款状态查询作业
                    Timestamp beginTime = new Timestamp(DateTime.now().subtract(Duration.createDays(3)).toLong());
                    List<RefundInfoPO> list = refundDAO.select(null, null, 1, beginTime, null, begin, 10);
                    for(RefundInfoPO refundInfoPO : list) {
                        if(needStop.get()) {
                            return null;
                        }
                        try {
                            queryRefund(refundInfoPO.getOrderNo());
                        }
                        catch (Exception ex) {
                            logger.error("refund state check failed, orderNo = " + refundInfoPO.getOrderNo(), ex);
                        }
                    }
                    begin += list.size();
                    try {
                        synchronized (needStop) {
                            needStop.wait(Duration.createHours(1).millis());
                        }
                    }
                    catch (Exception e) {
                        logger.error("refund state check sleep interrupted", e);
                    }
                    if(needStop.get()) {
                        return null;
                    }
                }
            }
        });
        return true;
    }

    /**
     * 析构
     */
    @Override
    @PreDestroy
    public void terminate() {
        needStop.set(true);
        synchronized (needStop) {
            needStop.notifyAll();
        }
    }

    /**
     * 申请退款
     *
     * @param orderNo 订单号
     * @param transactionId 微信订单号
     * @param refundNo 退款单号，全局不重复，传递null表示由系统生成
     * @param refundFee 退款金额
     * @param reason 原因
     * @return 执行结果
     */
    public boolean refund(String orderNo, String transactionId, String refundNo, int refundFee, String reason) throws IOException {
        logger.info("RefundService.refund(" + orderNo + ", " + refundNo + ")");
        OrderPO orderPO = orderDAO.loadByOrderNo(orderNo);
        if(null == orderPO) {
            logger.error("refund with invalid order number : " + orderNo);
            return false;
        }
        if(Text.isBlank(refundNo)) {
            refundNo = generateRefundNo();
        }
        else {
            RefundPO refundPO = refundDAO.loadByRefundNo(refundNo);
            if(null != refundPO) {
                throw new OrderExistException(orderNo);
            }
        }
        // 记录请求
        RefundPO refundPO = new RefundPO();
        refundPO.setRefundNo(refundNo);
        refundPO.setOrderNo(orderNo);
        refundPO.setRefundFee(refundFee);
        refundPO.setState(0);
        refundPO.setIsFailed("N");
        refundPO.setReason(reason);
        try {
            refundDAO.insertRefund(refundPO);
        }
        catch (Exception ex) {
            logger.error("insert into refund table failed, refundNo = " + refundNo + ", orderNo = " + orderNo + ", refundFee = " + refundFee, ex);
            return false;
        }
        // 发起请求
        RefundRequest refundRequest = RefundRequest.build(orderNo, refundNo, transactionId, orderPO.getFee(), refundFee, reason);
        RefundResponse refundResponse = WechatPayHelper.refund(refundRequest);
        if(!refundResponse.isSuccessful()) {
            throw new WechatResponseException("refund", refundRequest.toString(), refundResponse.toString());
        }
        refundPO.setState(1);
        refundDAO.updateRefunding(refundNo, refundResponse.refundId);
        orderDAO.updateOrderRefunding(orderNo);
        return true;
    }

    /**
     * 通过退款单号查询退款信息
     *
     * @param orderNo 订单号
     * @return 退款信息，null表示未退款
     */
    public RefundEntity queryRefund(String orderNo) throws IOException {
        OrderPO orderPO = orderDAO.loadByOrderNo(orderNo);
        if(null == orderPO) {
            // 订单不存在
            return null;
        }
        List<RefundPO> refundPOList = refundDAO.selectByOrderNo(orderNo);
        for(RefundPO refundPO : refundPOList) {
            RefundEntity result = new RefundEntity();
            convert(refundPO, result);
            if(RefundEntity.STATE_SUCCESS == result.state) {
                return result;
            }
            else if(RefundEntity.STATE_FAIL == result.state) {
                return result;
            }
            else if(RefundEntity.STATE_INIT == result.state) {
                continue;
            }
            else {
                // 远程查询退款信息
                // QueryRefundRequest queryRefundRequest = QueryRefundRequest.build(refundPO.getOrderNo(), null, refundPO.getRefundNo(), refundPO.getRefundId());
                QueryRefundRequest queryRefundRequest = QueryRefundRequest.build(null, null, refundPO.getRefundNo(), null);
                QueryRefundResponse queryRefundResponse = WechatPayHelper.queryRefund(queryRefundRequest);
                if(!queryRefundResponse.isSuccessful()) {
                    throw new WechatResponseException("queryOrder", queryRefundRequest.toString(), queryRefundResponse.toString());
                }
                for(QueryRefundResponse.RefundInfo refundInfo : queryRefundResponse.refundInfoList) {
                    if(result.refundNo.equals(refundInfo.refundTradeNo)) {
                        if("SUCCESS".equalsIgnoreCase(refundInfo.refundStatus)) {
                            Date dateTime = null;
                            try {
                                dateTime = DateTime.parse(refundInfo.refundTime).toDate();
                            }
                            catch (ParseException e) { }
                            refundDAO.updateRefundSuccess(refundInfo.refundTradeNo, dateTime);
                            orderDAO.updateOrderRefundSuccess(orderNo);
                            result.state = 2;
                        }
                        else if("REFUNDCLOSE".equalsIgnoreCase(refundInfo.refundStatus) || "CHANGE".equalsIgnoreCase(refundInfo.refundStatus)) {
                            refundDAO.updateRefundFail(refundInfo.refundTradeNo);
                            orderDAO.updateOrderRefundFail(orderNo);
                            result.state = 3;
                        }
                        else if("PROCESSING".equalsIgnoreCase(refundInfo.refundStatus)) {
                            logger.info("order " + orderNo + " refund state is processing");
                        }
                    }
                }
                return result;
            }
        }
        return null;
    }

    /**
     * 查询订单
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param status 状态，1：退款中，2：已退款，3：退款失败
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param begin 开始索引
     * @param size 查询个数
     * @return 订单列表
     */
    public Page<RefundInfoEntity> find(String orderNo, String openId, Integer status, DateTime beginTime, DateTime endTime, int begin, int size) {
        java.sql.Timestamp beginTimestamp = null;
        if(null != beginTime) {
            beginTimestamp = new java.sql.Timestamp(beginTime.toLong());
        }
        java.sql.Timestamp endTimestamp = null;
        if(null != endTime) {
            endTimestamp = new java.sql.Timestamp(endTime.toLong());
        }
        java.util.List<RefundInfoPO> refundInfoPOList = refundDAO.select(orderNo, openId, status, beginTimestamp, endTimestamp, begin, size);
        com.xyzq.simpson.base.type.List<RefundInfoEntity> result = new com.xyzq.simpson.base.type.List<RefundInfoEntity>();
        for(RefundInfoPO refundInfoPO : refundInfoPOList) {
            RefundInfoEntity refundInfoEntity = new RefundInfoEntity();
            refundInfoEntity.orderNo = refundInfoPO.getOrderNo();
            refundInfoEntity.openId = refundInfoPO.getOpenId();
            refundInfoEntity.mobileNo = refundInfoPO.getMobileNo();
            refundInfoEntity.userName = refundInfoPO.getUserName();
            refundInfoEntity.serialNo = refundInfoPO.getSerialNo();
            refundInfoEntity.goodsType = refundInfoPO.getGoodsType();
            refundInfoEntity.refundNo = refundInfoPO.getRefundNo();
            refundInfoEntity.tag = refundInfoPO.getTag();
            refundInfoEntity.fee = refundInfoPO.getFee();
            refundInfoEntity.refundFee = refundInfoPO.getRefundFee();
            switch(refundInfoPO.getState()) {
                case 0:
                case 1:
                    refundInfoEntity.status = RefundInfoEntity.STATUS_REFUNDING;
                    break;
                case 2:
                    refundInfoEntity.status = RefundInfoEntity.STATUS_REFUND_SUCCESS;
                    break;
                case 3:
                    refundInfoEntity.status = RefundInfoEntity.STATUS_REFUND_FAIL;
                    break;
            }
            refundInfoEntity.time = DateTime.parse(refundInfoPO.getCreateTime().getTime());
            // 添加
            result.add(refundInfoEntity);
        }
        Page<RefundInfoEntity> page = new Page<RefundInfoEntity>();
        page.list = result;
        page.total = refundDAO.count(orderNo, openId, status, beginTimestamp, endTimestamp);
        return page;
    }

    /**
     * 生成不重复退款号
     *
     * @return 退款号
     */
    public static synchronized String generateRefundNo() {
        StringBuilder builder = new StringBuilder();
        builder.append(DateTime.now().toLong());
        builder.append(Serial.makeLoopInteger());
        builder.append(Serial.makeRandomString(36));
        return builder.substring(0, 36);
    }

    /**
     * 类型转换
     *
     * @param refundPO 退款持久层对象
     * @param refundEntity 推款信息
     */
    private void convert(RefundPO refundPO, RefundEntity refundEntity) {
        refundEntity.orderNo = refundPO.getOrderNo();
        refundEntity.refundNo = refundPO.getRefundNo();
        refundEntity.refundFee = refundPO.getRefundFee();
        refundEntity.state = refundPO.getState();
    }
}
