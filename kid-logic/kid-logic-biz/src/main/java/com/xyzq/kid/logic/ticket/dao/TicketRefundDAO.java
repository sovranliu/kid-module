package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;

import java.util.List;
import java.util.Map;

public interface TicketRefundDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(TicketRefundPO record);

    int insertSelective(TicketRefundPO record);

    TicketRefundPO selectByPrimaryKey(Integer id);

    List<TicketRefundPO> selectRefunding(Map paramMap);
    int selectRefundingCount();

    List<TicketRefundPO> selectRefunded(Map paramMap);
    int selectRefundedCount();

    int refuseByPrimaryKey(Integer id);

    int accessByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TicketRefundPO record);

    int updateByPrimaryKey(TicketRefundPO record);
}