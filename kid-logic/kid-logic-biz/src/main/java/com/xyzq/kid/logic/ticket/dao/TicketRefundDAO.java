package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;

public interface TicketRefundDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketRefundPO record);

    int insertSelective(TicketRefundPO record);

    TicketRefundPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TicketRefundPO record);

    int updateByPrimaryKey(TicketRefundPO record);
}