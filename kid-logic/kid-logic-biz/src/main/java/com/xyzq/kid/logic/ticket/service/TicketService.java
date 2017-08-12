package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.common.service.SMSService;
import com.xyzq.kid.finance.service.RefundService;
import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.config.common.ConfigCommon;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.logic.config.service.ConfigService;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.ticket.bean.TicketBean;
import com.xyzq.kid.logic.ticket.bean.TicketHistoryBean;
import com.xyzq.kid.logic.ticket.bean.TicketRefundBean;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import com.xyzq.simpson.base.type.Table;
import com.xyzq.simpson.base.type.core.ITable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 票务服务
 */
@Service("ticketService")
public class TicketService implements PayListener {
    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(TicketService.class);

    /**
     * 票信息
     */
    @Autowired
    private TicketBean ticketBean;
    /**
     * 退票申请信息
     */
    @Autowired
    private TicketRefundBean ticketRefundBean;
    /**
     * 票信息
     */
    @Autowired
    private TicketHistoryBean ticketHistoryBean;
    /**
     * 用户信息
     */
    @Autowired
    private UserService userService;
    /**
     * 退票服务
     */
    @Autowired
    private RefundService refundService;
    /**
     * 票价服务
     */
    @Autowired
    private GoodsTypeService goodsTypeService;
    /**
     * 配置服务
     */
    @Autowired
    private ConfigService configService;

    @Autowired
    private SMSService smsService;
    /**
     * 买票回调接口
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param goodsType 商品类型
     * @param fee 支付金额
     * @param tag 附属数据
     */
    @Override
    public void onPay(String orderNo, String openId, int goodsType, int fee, String tag) {
        logger.info("TicketService.buySingleTickets[in]-orderNo:" + orderNo + ",openId:"+ openId + ",goodsType:" + goodsType + ",fee:" + fee);
        UserEntity userEntity = userService.selectByOpenId(tag);
        if(null == userEntity || null == userEntity.openid) {
            logger.error("No user by openId:" + tag);
            return;
        }
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.telephone = userEntity.telephone;
        ticketEntity.payNumber = orderNo;
        ticketEntity.payeropenid = openId;
        ticketEntity.price = new BigDecimal(goodsTypeService.calculateFee(goodsType));
        ConfigEntity configEntity = configService.load(ConfigCommon.TICKET_EXPIREDATE);
        ticketEntity.expire = CommonTool.addDataYMD(CommonTool.dataToStringYMD(new Date()), Integer.parseInt(configEntity.content));
        //单人票
        if(goodsTypeService.isSingleTicket(goodsType)) {
            ticketEntity.insurance = goodsTypeService.containsInsurance(goodsType);
            ticketEntity.type = TicketEntity.TICKET_TYPE_SINGLE;
            buySingleTickets(ticketEntity);
            return;
        }
        //团购票
        if(goodsTypeService.isGroupTicket(goodsType)) {
            ticketEntity.type = TicketEntity.TICKET_TYPE_GROUP;
            buyGroupleTickets(ticketEntity, goodsTypeService.calculateTicketCount(goodsType));
            return;
        }
    }

    /**
     * 个人购票
     * @param ticketEntity
     * @return
     */
    private void buySingleTickets(TicketEntity ticketEntity){
        logger.info("TicketService.buySingleTickets[in]-ticketEntity:" + ticketEntity.toString());
        ticketEntity.type = TicketEntity.TICKET_TYPE_SINGLE;
        buyTickets(ticketEntity);
        return;
    }

    /**
     * 团队购票
     * @param ticketEntity 票信息
     * @param num 购买张数
     * @return
     */
    private void buyGroupleTickets(TicketEntity ticketEntity, int num){
        logger.info("TicketService.buyGroupleTickets[in]-ticketEntity:" + ticketEntity.toString() + ",num:" + num);
        if(num >= 3) {
            for (int i = 0; i < num; i++) {
                ticketEntity.type = TicketEntity.TICKET_TYPE_GROUP;
                ticketEntity.insurance = false;
                buyTickets(ticketEntity);
            }
        }
        return;
    }

    /**
     * 赠票
     * @param ticketId 飞行票ID
     * @param mobileNo 被赠送人手机号
     * @return 成功返回-success
     */
    public String handselTickets(int ticketId, String mobileNo, String preMobile, String type){
        logger.info("TicketService.handselTickets[in]-ticketId:" + ticketId + ",mobileNo:" + mobileNo + ", preMobile:" + preMobile);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "票被删除!";
        }
        if(ticketEntity.type == TicketEntity.TICKET_TYPE_SINGLE) {
            logger.error("single ticket can not handsel![" + ticketEntity.type + "]");
            return "个人票不可赠送!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "票状态不支持当前操作!";
        }
        if(null == mobileNo) {
            logger.error("mobileno is null!");
            return "未能正确获取手机号!";
        }
        int count = queryTickethandselCount(ticketId);
        if(count > 0) {
            List<TicketHistoryEntity> ticketHistoryEntityList = ticketHistoryBean.selectHandselByTicketId(ticketId);
            if(null != ticketHistoryEntityList && ticketHistoryEntityList.size() > 0) {
                for (int i = 0; i < ticketHistoryEntityList.size(); i++) {
                    if(ticketHistoryEntityList.get(i).premobile.equals(mobileNo)) {
                        return "票已经被" + ticketEntity.telephone + "领取！";
                    }
                }
            }

            if(mobileNo.equals(ticketEntity.telephone)) {
                logger.error("can not handsel to self![" + ticketEntity.telephone + "]");
                return "您已获取票【" + ticketEntity.serialNumber + "】!";
            }

            logger.info("ticket has been handseled or handseling!");
            return "票已被抢!";
        }

        if(mobileNo.equals(ticketEntity.telephone)) {
            logger.error("can not handsel to self![" + ticketEntity.telephone + "]");
            return "不可赠送给自己!";
        }

        if(type == CommonTool.HANDLE_RECEIVE) {
            handselReceiveSuccessTicketHistory(ticketId, ticketEntity.telephone);
        } else {
            handselTicketHistory(ticketId, ticketEntity.telephone);
        }

        Map paramMap = new HashMap<>();
        paramMap.put("id", ticketId);
        paramMap.put("ownermobileno", mobileNo);
        paramMap.put("premobileno", preMobile);
        int result = ticketBean.updateHandselByPrimaryKeyLock(paramMap);

        if(0 == result) {
            return "赠送失败!";
        }

        ITable<String, String> data = new Table<String, String>();
        UserEntity userEntity = userService.selectByOpenId(ticketEntity.payeropenid);
        data.put("userName", userEntity.userName);
        data.put("serialNumber", ticketEntity.serialNumber);
        smsService.sendSMS(mobileNo, "redundticket", data);

        return "success";
    }

    /**
     * 查询票是否有赠送中，或者赠送成功历史记录
     * @param ticketId 票号
     * @return 个数
     */
    public int queryTickethandselCount(Integer ticketId) {
        return ticketHistoryBean.queryTickethandselCount(ticketId);
    }

    /**
     * 用票
     * @param ticketId 飞行票ID
     * @return 成功返回-success
     */
    public String useTickets(int ticketId) {
        logger.info("TicketService.useTickets[in]-ticketId:" + ticketId);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expire)) {
            logger.error("ticket expire![" + ticketEntity.expire + "]");
            return "ticket expire!";
        }
        useTicketHistory(ticketId);

        ticketBean.updateUseByPrimaryKey(ticketEntity.id);

        return "success";
    }

    /**
     * 用票恢复
     * @param ticketId 飞行票ID
     * @return 成功返回-success
     */
    public String recoverTickets(int ticketId) {
        logger.info("TicketService.recoverTickets[in]-ticketId:" + ticketId);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_USED) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expire)) {
            logger.error("ticket expire![" + ticketEntity.expire + "]");
            return "ticket expire!";
        }
        recoverTicketHistory(ticketId);

        ticketBean.updateRecoverByPrimaryKey(ticketEntity.id);

        return "success";
    }

    /**
     * 票期拓展
     * @param ticketId 飞行票ID
     * @param extendDate 拓展日期
     * @return 成功返回-success
     */
    public String extendTickets(int ticketId, String extendDate) {
        logger.info("TicketService.extendTickets[in]-ticketId:" + ticketId + ",extendDate:" + extendDate);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expire)) {
            logger.error("ticket expire![" + ticketEntity.expire + "]");
            return "ticket expire!";
        }
        if(CommonTool.checkExpire(extendDate)) {
            logger.error("wrong extendDate![" + extendDate + "]");
            return "wrong extendDate!";
        }
        extendTicketHistory(ticketId,ticketEntity.expire);

        ticketEntity.expire = extendDate;
        ticketBean.updateExtendByPrimaryKey(ticketEntity);

        return "success";
    }

    /**
     * 申请退票
     * @param ticketId
     * @return
     */
    public String refundingTickets(int ticketId) {
        logger.info("TicketService.backingTickets[in]-ticketId:" + ticketId);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "票被删除!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "票状态不支持此操作!";
        }
        if(CommonTool.checkExpire(ticketEntity.expire)) {
            logger.error("ticket expire![" + ticketEntity.expire + "]");
            return "票已过期!";
        }
        TicketRefundEntity ticketRefundEntityChect = ticketRefundBean.selectByTicketId(ticketId);
        if(null != ticketRefundEntityChect && ticketRefundEntityChect.ticketid > 0) {
            logger.info("already had refund info![" + ticketRefundEntityChect.toString() + "]");
            return "success";
        }

        refundingTicketHistory(ticketId);

        TicketRefundEntity ticketRefundEntity = new TicketRefundEntity();
        ticketRefundEntity.status = TicketRefundEntity.REFUND_STATUS_NEW;
        ticketRefundEntity.ticketid = ticketId;
        ticketRefundEntity.deleted = CommonTool.STATUS_NORMAL;
        insertRefundSelective(ticketRefundEntity);

        ticketBean.updateRefundingByPrimaryKey(ticketEntity.id);

        return "success";
    }

    /**
     * 退票成功
     * @param ticketId
     * @return
     */
    public String refundTickets(int ticketId) {
        logger.info("TicketService.backingTickets[in]-ticketId:" + ticketId);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            logger.error("ticket has been deleted![" + ticketEntity.deleted + "]");
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_BACKING) {
            logger.error("ticket status error![" + ticketEntity.status + "]");
            return "ticket status error!";
        }

        refundTicketHistory(ticketId);

        ticketBean.updateRefundByPrimaryKey(ticketEntity.id);

        return "success";
    }

    /**
     * 根据流水号获取个人票务信息
     * @param serialno
     * @return 入参为空返回空
     */
    public TicketEntity getTicketsInfoBySerialno (String serialno ){
        logger.info("TicketService.getTicketsInfoBySerialno[in]-serialno:" + serialno);
        if(null == serialno || serialno.length() == 0) {
            return null;
        }
        return ticketBean.getTicketsInfoBySerialno(serialno);
    }

    /**
     * 根据手机号获取飞行票信息
     * @param mobileNo 手机号
     * @return
     */
    public List<TicketEntity> getTicketsInfoByOwnerMobileNo(String mobileNo) {
        logger.info("TicketService.getTicketsInfoByOwnerMobileNo[in]-mobileNo:" + mobileNo);

        List<TicketHistoryEntity> ticketHistoryEntityList = ticketHistoryBean.selectHandselByPreMobile(mobileNo);
        for (int i = 0; i < ticketHistoryEntityList.size(); i++) {
            //如果用户在user表中存在，代表赠送生效
            //如果用户在user表中不存在，且已超过24小时，赠送失效
            TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketHistoryEntityList.get(i).ticketid);
            UserEntity userEntity = userService.selectByMolieNo(ticketEntity.telephone);
            if(null != userEntity && userEntity.telephone.length() >0 ) {
                handselEffectiveTicketHistory(ticketHistoryEntityList.get(i).id);
            } else {
                Date handseldate = CommonTool.stringToDataYMDHMS(ticketHistoryEntityList.get(i).createtime);
                Date nowDate = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(handseldate);
                calendar.add(calendar.DATE,1);
                handseldate = calendar.getTime();
                if(handseldate.before(nowDate)) {
                    handselExpiredTicketHistory(ticketHistoryEntityList.get(i).id);
                }
            }

        }

        List<TicketEntity> ticketEntityList = ticketBean.getTicketsInfoByOwnerMobileNo(mobileNo);
        if(null != ticketEntityList) {
            for (int i = 0; i < ticketEntityList.size(); i++) {
                if(ticketEntityList.get(i).status == TicketEntity.TICKET_STATUS_NEW) {
                    if(CommonTool.checkExpire(ticketEntityList.get(i).expire)) {
                        ticketBean.updateExpiredByPrimaryKey(ticketEntityList.get(i).id);
                    }
                }
            }
            return ticketBean.getTicketsInfoByOwnerMobileNo(mobileNo);
        }
        return ticketEntityList;

    }

    /**
     * 根据OpenID获取飞行票信息
     * @param openID
     * @return
     */
    public List<TicketEntity> getTicketsInfoByPayerOpenID(String openID) {
        logger.info("TicketService.getTicketsInfoByPayerOpenID[in]-openID:" + openID);
        return ticketBean.getTicketsInfoByPayerOpenID(openID);
    }

    /**
     * 获取飞行票操作历史
     * @param ticketId
     * @return
     */
    public List<TicketHistoryEntity> getTicketHistoryByTickedId(int ticketId) {
        logger.info("TicketService.getTicketsInfoByPayerOpenID[in]-ticketId:" + ticketId);
        return ticketHistoryBean.selectByTicketId(ticketId);
    }

    /**
     * 发起退款申请
     * @param ticketRefundEntity
     * @return
     */
    public int insertRefundSelective(TicketRefundEntity ticketRefundEntity) {
        ticketRefundEntity.status = TicketRefundEntity.REFUND_STATUS_NEW;
        return ticketRefundBean.insertSelective(ticketRefundEntity);
    }

    /**
     * 退款申请通过（执行退款）
     * @param ticketId 票号
     * @return 退款成功or失败
     */
    public boolean accessRefund(Integer ticketId) {
        TicketRefundEntity ticketRefundEntityChect = ticketRefundBean.selectByTicketId(ticketId);
        boolean result = refund(ticketId);
        if(result) {
            ticketRefundBean.accessByPrimaryKey(ticketRefundEntityChect.id);
        }
        return result;

    }

    /**
     * 退款申请拒绝
     * @param id
     * @return
     */
    public int refuseRefund(Integer ticketId) {
        TicketRefundEntity ticketRefundEntityChect = ticketRefundBean.selectByTicketId(ticketId);
        return ticketRefundBean.refuseByPrimaryKey(ticketRefundEntityChect.id);
    }

    /**
     * 退款申请记录查询（未处理）
     * @param limit
     * @return
     */
    public Page<TicketRefundEntity> getTicketRefunding(Integer begin, int limit) {
        return ticketRefundBean.selectRefunding(begin, limit);
    }

    public List<TicketRefundEntity> selectAllRefunding() {
        return ticketRefundBean.selectAllRefunding();
    }

    /**
     * 退款申请历史记录查询（已处理）
     * @param limit
     * @return
     */
    public Page<TicketRefundEntity> getTicketRefunded(Integer begin, int limit) {
        return ticketRefundBean.selectRefunded(begin, limit);
    }

    public TicketRefundEntity loadRefundByTicketId(Integer ticketId) {
        return ticketRefundBean.selectByTicketId(ticketId);
    }

    /**
     * 根据条件查询飞行记录
     * @param serialno 流水号
     * @param ownermobileno 手机号
     * @param beginDate 售出起
     * @param endDate 售出止
     * @param status 状态
     * @param begin 分页起始
     * @param limit 每页条数
     * @return
     */
    public Page<TicketEntity> queryTicketByCond(String serialno, String ownermobileno, String beginDate, String endDate, Integer status, Integer begin, Integer limit) {
        Map paramMap = new HashMap<>();
        logger.info("TicketService.queryTicketByCond[in]-serialno:" + serialno +
                ", ownermobileno:" + ownermobileno + ", beginDate:" + beginDate +
                ", endDate:" + endDate + ", status:" + status +", begin:" + begin + ", limit:" + limit);
        if(null != serialno && serialno.length() > 0) {
            paramMap.put("serialno", serialno);
        }
        if(null != ownermobileno && ownermobileno.length() > 0) {
            paramMap.put("ownermobileno", ownermobileno);
        }
        if(null != beginDate && beginDate.length() > 0) {
            paramMap.put("beginDate", beginDate);
        }
        if(null != endDate && endDate.length() > 0) {
            paramMap.put("endDate", endDate);
        }
        if(null != status) {
            paramMap.put("status", status);
        }
        paramMap.put("begin", begin);
        paramMap.put("limit", limit);
        return ticketBean.queryTicketByCond(paramMap);
    }


    /**
     * 生成飞行票流水号时分秒+随机4位字母
     * @return
     */
    private String getSerialno() {
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddhhmmssSSS" );
        UUID uuid = UUID.randomUUID();
        return sdf.format(new Date()) + uuid.toString().substring(1, 5);
    }

    /**
     * 买票
     * @param ticketEntity
     */
    private void buyTickets(TicketEntity ticketEntity){
        ticketEntity.deleted = CommonTool.STATUS_NORMAL;
        ticketEntity.serialNumber = getSerialno();
        ticketEntity.status = TicketEntity.TICKET_STATUS_NEW;
        int ticketId = ticketBean.insertSelective(ticketEntity);
        newTicketHistory(ticketId);

        return;
    }

    /**
     * 创建飞行票新建历史记录
     * @param ticketId
     */
    private void newTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_NEW, null, null);
    }

    /**
     * 创建飞行票赠送历史记录
     * @param ticketId
     * @param preMobileNo
     */
    private void handselTicketHistory(int ticketId, String preMobileNo) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_HANDSEL, null, preMobileNo);
    }

    private void handselReceiveSuccessTicketHistory(int ticketId, String preMobileNo) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_HANDSEL_EFFECTIVE, null, preMobileNo);
    }

    /**
     * 飞行票赠送过期
     * @param id
     */
    private void handselExpiredTicketHistory(int id) {
        TicketHistoryEntity ticketHistoryEntityUpdate = new TicketHistoryEntity();
        ticketHistoryEntityUpdate.id = id;
        ticketHistoryEntityUpdate.action = TicketHistoryEntity.TICKET_ACTION_HANDSEL_EXPPIRED;
        ticketHistoryBean.updateByPrimaryKeySelective(ticketHistoryEntityUpdate);
    }

    /**
     * 飞行票赠送生效
     * @param id
     */
    private void handselEffectiveTicketHistory(int id) {
        TicketHistoryEntity ticketHistoryEntityUpdate = new TicketHistoryEntity();
        ticketHistoryEntityUpdate.id = id;
        ticketHistoryEntityUpdate.action = TicketHistoryEntity.TICKET_ACTION_HANDSEL_EFFECTIVE;
        ticketHistoryBean.updateByPrimaryKeySelective(ticketHistoryEntityUpdate);

    }

    /**
     * 创建飞行票使用历史记录
     * @param ticketId
     */
    private void useTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_USE, null, null);
    }

    /**
     * 创建飞行票恢复历史记录
     * @param ticketId
     */
    private void recoverTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_RECOVER, null, null);
    }

    /**
     * 创建飞行票有效期延长历史记录
     * @param ticketId
     */
    private void extendTicketHistory(int ticketId, String preValidPeriod) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_EXTEND, preValidPeriod, null);
    }

    /**
     * 创建飞行票退票申请历史记录
     * @param ticketId
     */
    private void refundingTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_REFUNDING, null, null);
    }

    /**
     * 创建飞行票退票成功历史记录
     * @param ticketId
     */
    private void refundTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_REFUND, null, null);
    }

    /**
     * 新增飞行票历史
     * @param ticketId 飞行票ID
     * @param action    1-新建，2-赠送，3-使用，4-有效期延长
     * @param preValidPeriod    Action为4时，上次截止日
     * @param preMobile Action为2时，记录下原归属人手机号
     */
    private void doInsertTicketHistory(int ticketId, int action, String preValidPeriod, String preMobile) {
        TicketHistoryEntity ticketHistoryEntity = new TicketHistoryEntity();
        ticketHistoryEntity.ticketid = ticketId;
        ticketHistoryEntity.action = action;
        if(action == TicketHistoryEntity.TICKET_ACTION_HANDSEL || action == TicketHistoryEntity.TICKET_ACTION_HANDSEL_EFFECTIVE) {
            ticketHistoryEntity.premobile = preMobile;
        }
        if(action == TicketHistoryEntity.TICKET_ACTION_EXTEND) {
            ticketHistoryEntity.prevalidperiod = preValidPeriod;
        }
        ticketHistoryEntity.deleted = CommonTool.STATUS_NORMAL;
        ticketHistoryBean.insertSelective(ticketHistoryEntity);
    }

    /**
     * 退款
     * @param ticketId 票号
     * @return true-退票成功，false-退票失败
     */
    private boolean refund(int ticketId) {
        logger.info("TicketService.refund[in]-ticketId:" + ticketId);

        boolean result = false;
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            return false;
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_BACKING) {
            return false;
        }

        try {
            result = refundService.refund(ticketEntity.payNumber, null, null, ticketEntity.price.intValue(), null);
        } catch (IOException e) {
            logger.info("TicketService.refund[in]-ticketId:" + ticketId + " fail for " + e.toString());
            return false;
        }

        refundTickets(ticketId);

        return result;
    }
    
    public TicketEntity getTicketByPk(Integer ticketId){
    	return ticketBean.selectByPrimaryKey(ticketId);

    }
}
