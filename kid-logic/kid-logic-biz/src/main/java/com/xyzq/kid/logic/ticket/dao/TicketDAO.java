package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketPO;

public interface TicketDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketPO record);

    int insertSelective(TicketPO record);

    TicketPO selectByPrimaryKey(Integer id);

    TicketPO getTicketsInfoBySerialno(String serialno);

    int updateByPrimaryKeySelective(TicketPO record);

    int updateByPrimaryKey(TicketPO record);
}