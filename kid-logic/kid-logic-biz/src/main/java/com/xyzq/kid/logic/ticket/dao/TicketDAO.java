package com.xyzq.kid.logic.ticket.dao;

import com.xyzq.kid.logic.ticket.dao.po.TicketPO;

import java.util.List;
import java.util.Map;

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

    int updateExpiredByPrimaryKey(Integer id);

    int updateRefundByPrimaryKey(Integer id);

    int updateRefundingByPrimaryKey(Integer id);

    int updateHandselByPrimaryKey(TicketPO record);

    int updateHandselByPrimaryKeyLock(Map paramMap);

    int updateUseByPrimaryKey(Integer id);

    int updateRecoverByPrimaryKey(Integer id);

    List<TicketPO> queryTicketByCond(Map paramMap);

    int queryTicketByCondCount(Map paramMap);

    int updateHandselNewUser(Map paramMap);

    int updateHandselExpired(Map paramMap);

//    int updateMobileNo(Map paramMap);
}