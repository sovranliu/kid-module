package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketHistoryPO;

import java.util.List;
import java.util.Map;

public interface TicketHistoryDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketHistoryPO record);

    int insertSelective(TicketHistoryPO record);

    TicketHistoryPO selectByPrimaryKey(Integer id);

    List<TicketHistoryPO> selectByTicketId(Integer ticketid);

    List<TicketHistoryPO> selectByPreMobile(String premobile);

    int updateByPrimaryKeySelective(TicketHistoryPO record);

    int updateByPrimaryKey(TicketHistoryPO record);

    int queryTickethandselCount(Integer ticketid);

    List<TicketHistoryPO> selectHandselByTicketId(Integer ticketid);

    int updateMobileNo(Map paramMap);
}