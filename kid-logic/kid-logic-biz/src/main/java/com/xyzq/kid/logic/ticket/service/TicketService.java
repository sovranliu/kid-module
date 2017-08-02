package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.ticket.bean.TicketBean;
import com.xyzq.kid.logic.ticket.bean.TicketHistoryBean;
import com.xyzq.kid.logic.ticket.bean.TicketRefundBean;
import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 票务服务
 */
@Service("ticketService")
public class TicketService {
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
     * 个人购票
     * @param ticketEntity
     * @return
     */
    public void buySingleTickets(TicketEntity ticketEntity){
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
    public void buyGroupleTickets(TicketEntity ticketEntity, int num){
        logger.info("TicketService.buyGroupleTickets[in]-ticketEntity:" + ticketEntity.toString() + ",num:" + num);
        if(num >= 3) {
            for (int i = 0; i < num; i++) {
                ticketEntity.type = TicketEntity.TICKET_TYPE_GROUP;
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
    public String handselTickets(int ticketId, String mobileNo){
        logger.info("TicketService.handselTickets[in]-ticketId:" + ticketId + ",mobileNo:" + mobileNo);
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            return "ticket has been deleted!";
        }
        if(ticketEntity.type == TicketEntity.TICKET_TYPE_SINGLE) {
            return "single ticket can not handsel!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            return "ticket status error!";
        }
        if(null == mobileNo) {
            return "mobileno is null!";
        }
        if(mobileNo.equals(ticketEntity.ownermobileno)) {
            return "can not handsel to self!";
        }

        handselTicketHistory(ticketId, ticketEntity.ownermobileno);

        ticketEntity.ownermobileno = mobileNo;
        ticketBean.updateHandselByPrimaryKey(ticketEntity);

        return "success";
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
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
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
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_USED) {
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
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
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
            return "ticket expire!";
        }
        if(CommonTool.checkExpire(extendDate)) {
            return "wrong extendDate!";
        }
        extendTicketHistory(ticketId,ticketEntity.expiredate);

        ticketEntity.expiredate = extendDate;
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
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_NEW) {
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
            return "ticket expire!";
        }

        refundingTicketHistory(ticketId);

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
            return "ticket has been deleted!";
        }
        if(ticketEntity.status != TicketEntity.TICKET_STATUS_BACKING) {
            return "ticket status error!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
            return "ticket expire!";
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
            UserEntity userEntity = userService.selectByMolieNo(ticketEntity.ownermobileno);
            if(null != userEntity && userEntity.mobileno.length() >0 ) {
                handselEffectiveTicketHistory(ticketHistoryEntityList.get(i).id);
            } else {
                Date handseldate = CommonTool.StringToDataYMDHMS(ticketHistoryEntityList.get(i).createtime);
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
                    if(CommonTool.checkExpire(ticketEntityList.get(i).expiredate)) {
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
    public int insertSelective(TicketRefundEntity ticketRefundEntity) {
        ticketRefundEntity.status = TicketRefundEntity.REFUND_STATUS_NEW;
        return ticketRefundBean.insertSelective(ticketRefundEntity);
    }

    /**
     * 退款申请通过
     * @param id
     * @return
     */
    public int accessByPrimaryKey(Integer id) {
        return ticketRefundBean.accessByPrimaryKey(id);
    }

    /**
     * 退款申请拒绝
     * @param id
     * @return
     */
    public int refuseByPrimaryKey(Integer id) {
        return ticketRefundBean.refuseByPrimaryKey(id);
    }

    /**
     * 退款申请记录查询（未处理）
     * @param limits
     * @return
     */
    public List<TicketRefundEntity> getTicketRefunding(int limits) {
        return ticketRefundBean.selectRefunding(limits);
    }

    /**
     * 退款申请历史记录查询（已处理）
     * @param limits
     * @return
     */
    public List<TicketRefundEntity> getTicketRefunded(int limits) {
        return ticketRefundBean.selectRefunded(limits);
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
        ticketEntity.serialno = getSerialno();
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
        if(action == TicketHistoryEntity.TICKET_ACTION_HANDSEL) {
            ticketHistoryEntity.premobile = preMobile;
        }
        if(action == TicketHistoryEntity.TICKET_ACTION_EXTEND) {
            ticketHistoryEntity.prevalidperiod = preValidPeriod;
        }
        ticketHistoryEntity.deleted = CommonTool.STATUS_NORMAL;
        ticketHistoryBean.insertSelective(ticketHistoryEntity);
    }

}
