package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.ticket.bean.TicketBean;
import com.xyzq.kid.logic.ticket.bean.TicketHistoryBean;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 票务服务
 */
@Service("ticketService")
public class TicketService {
    /**
     * 票信息
     */
    @Autowired
    private TicketBean ticketBean;
    /**
     * 票历史信息
     */
    @Autowired
    private TicketHistoryBean ticketHistoryBean;

    /**
     * 个人购票
     * @param ticketEntity
     * @return
     */
    public void buySingleTickets(TicketEntity ticketEntity){
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
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            return "ticket has been deleted!";
        }
        if(ticketEntity.type == TicketEntity.TICKET_TYPE_SINGLE) {
            return "single ticket can not handsel!";
        }
        if(null == mobileNo) {
            return "mobileno is null!";
        }
        if(mobileNo.equals(ticketEntity.ownermobileno)) {
            return "can not handsel to self!";
        }

        handselTicketHistory(ticketId, ticketEntity.ownermobileno);

        ticketEntity.ownermobileno = mobileNo;
        ticketBean.updateByPrimaryKey(ticketEntity);

        return "success";
    }

    /**
     * 用票
     * @param ticketId 飞行票ID
     * @return 成功返回-success
     */
    public String useTickets(int ticketId) {
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            return "ticket has been deleted!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
            return "ticket expire!";
        }
        useTicketHistory(ticketId);

        ticketEntity.status = TicketEntity.TICKET_STATUS_USED;
        ticketBean.updateByPrimaryKey(ticketEntity);

        return "success";
    }

    /**
     * 票期拓展
     * @param ticketId 飞行票ID
     * @param extendDate 拓展日期
     * @return 成功返回-success
     */
    public String extendTickets(int ticketId, String extendDate) {
        TicketEntity ticketEntity = ticketBean.selectByPrimaryKey(ticketId);
        if(ticketEntity.deleted != CommonTool.STATUS_NORMAL) {
            return "ticket has been deleted!";
        }
        if(CommonTool.checkExpire(ticketEntity.expiredate)) {
            return "ticket expire!";
        }
        if(!CommonTool.checkExpire(extendDate)) {
            return "wrong extendDate!";
        }
        extendTicketHistory(ticketId,ticketEntity.expiredate);

        ticketEntity.expiredate = extendDate;
        ticketBean.updateByPrimaryKey(ticketEntity);

        return "success";
    }

    /**
     * 获取个人票务信息
     * @param entity
     * @return
     */
    public int getPersionTickets(TicketEntity entity){
        entity.deleted = 0;
        return ticketBean.insert(entity);
    }

    /**
     * 根据流水号获取个人票务信息
     * @param serialno
     * @return 入参为空返回空
     */
    public TicketEntity getTicketsInfoBySerialno (String serialno ){
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
        return ticketBean.getTicketsInfoByOwnerMobileNo(mobileNo);
    }

    /**
     * 根据手机号获取飞行票信息
     * @param openID
     * @return
     */
    public List<TicketEntity> getTicketsInfoByPayerOpenID(String openID) {
        return ticketBean.getTicketsInfoByPayerOpenID(openID);
    }


    /**
     * 获取飞行票操作历史
     */

    /**
     * 处理用户增票--自己是增票者
     */

    /**
     * 处理用户增票--自己是被增者
     */

    /**
     * 生成飞行票流水号
     * @return
     */
    private String getSerialno() {
        //TODO 仅仅时间
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddhhmmssSSS" );
        return sdf.format(new Date());
    }

    /**
     * 买票
     * @param ticketEntity
     */
    private void buyTickets(TicketEntity ticketEntity){
        ticketEntity.deleted = CommonTool.STATUS_NORMAL;
        ticketEntity.serialno = getSerialno();
        ticketEntity.status = TicketEntity.TICKET_STATUS_NEW;
        //TODO 未正确返回tickedId
        int ticketId = ticketBean.insert(ticketEntity);
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
     * 创建飞行票使用历史记录
     * @param ticketId
     */
    private void useTicketHistory(int ticketId) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_USE, null, null);
    }

    /**
     * 创建飞行票有效期延长历史记录
     * @param ticketId
     */
    private void extendTicketHistory(int ticketId, String preValidPeriod) {
        doInsertTicketHistory(ticketId, TicketHistoryEntity.TICKET_ACTION_EXTEND, preValidPeriod, null);
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
