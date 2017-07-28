package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketHistoryPO;

public interface TicketHistoryDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketHistoryPO record);

    int insertSelective(TicketHistoryPO record);

    TicketHistoryPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TicketHistoryPO record);

    int updateByPrimaryKey(TicketHistoryPO record);
}