package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketPO;

import java.util.List;

public interface TicketDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketPO record);

    int insertSelective(TicketPO record);

    TicketPO selectByPrimaryKey(Integer id);

    TicketPO getTicketsInfoBySerialno(String serialno);

    List<TicketPO> getTicketsInfoByOwnerMobileNo(String serialno);

    List<TicketPO> getTicketsInfoByPayerOpenID(String serialno);

    int updateByPrimaryKeySelective(TicketPO record);

    int updateByPrimaryKey(TicketPO record);

    int updateExtendByPrimaryKey(TicketPO record);

    int updateHandselByPrimaryKey(TicketPO record);

    int updateUseByPrimaryKey(TicketPO record);

    int updateRecoverByPrimaryKey(TicketPO record);
}