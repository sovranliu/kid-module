package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.logic.ticket.bean.TicketBean;
import com.xyzq.kid.logic.ticket.bean.TicketHistoryBean;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 方法描述 根据id查找票信息
     *
     * @return 返回值 TicketEntity
     */
    public TicketEntity getTicketById(int id) {
        return ticketBean.selectByPrimaryKey(id);
    }

    /**
     * 根据主键删除飞行票信息
     * @param id
     * @return
     */
    public int deleteTicket(Integer id) {
        return ticketBean.deleteByPrimaryKey(id);
    }

    /**
     * 全量新增飞行票信息
     * @param entity
     * @return
     */
    public int insert(TicketEntity entity){
        entity.deleted = 0;
        return ticketBean.insert(entity);
    }

    /**
     * 方法描述 根据id查找票历史记录信息
     *
     * @return 返回值 TicketHistoryEntity
     */
    public TicketHistoryEntity getTicketHistoryById(int id) {
        return ticketHistoryBean.selectByPrimaryKey(id);
    }

    /**
     * 方法描述 根据ticketid查找票历史记录信息
     *
     * @return 返回值 TicketHistoryEntity
     */
    public List<TicketHistoryEntity> getTicketHistory(int ticketId) {
        return null;
    }



}
