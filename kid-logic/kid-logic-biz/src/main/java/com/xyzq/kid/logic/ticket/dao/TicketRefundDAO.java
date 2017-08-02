package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;

import java.util.List;

public interface TicketRefundDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketRefundPO record);

    int insertSelective(TicketRefundPO record);

    TicketRefundPO selectByPrimaryKey(Integer id);

    List<TicketRefundPO> selectRefunding(Integer num);

    List<TicketRefundPO> selectRefunded(Integer num);

    int refuseByPrimaryKey(Integer id);

    int accessByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TicketRefundPO record);

    int updateByPrimaryKey(TicketRefundPO record);
}