package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.ticket.bean.TicketBean;
import com.xyzq.kid.logic.ticket.bean.TicketHistoryBean;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     *
     * @param ticketEntity
     */
    private void buyTickets(TicketEntity ticketEntity){
        ticketEntity.deleted = CommonTool.STATUS_NORMAL;
        ticketEntity.serialno = getSerialno();
        ticketEntity.status = TicketEntity.TICKET_STATUS_NEW;
        int ticketId = ticketBean.insert(ticketEntity);

        TicketHistoryEntity ticketHistoryEntity = new TicketHistoryEntity();
        ticketHistoryEntity.ticketid = ticketId;
        ticketHistoryEntity.action = TicketHistoryEntity.TICKET_ACTION_NEW;
        ticketHistoryEntity.deleted = CommonTool.STATUS_NORMAL;

        ticketHistoryBean.insertSelective(ticketHistoryEntity);

        return;
    }

    /**
     * 增票
     * @param entity
     * @return
     */
    public int handselTickets(TicketEntity entity){
        entity.deleted = 0;
        return ticketBean.insert(entity);
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
     * 获取飞行票状态
     */

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

}
