package com.xyzq.kid.logic.ticket.bean;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.ticket.dao.TicketRefundDAO;
import com.xyzq.kid.logic.ticket.dao.TicketRefundDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class TicketRefundBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private TicketRefundDAO ticketRefundDAO;

    public int insertSelective(TicketRefundEntity ticketRefundEntity) {
        return ticketRefundDAO.insertSelective(TicketRefundEntityToPO(ticketRefundEntity));
    }

    public Page<TicketRefundEntity> selectRefunding(Integer begin, Integer limit) {
        List<TicketRefundEntity> ticketRefundEntityList = new ArrayList<>();
        Map paramMap = new HashMap<>();
        paramMap.put("begin", (begin - 1) * limit);
        paramMap.put("limit", limit);
        List<TicketRefundPO> ticketRefundPOList = ticketRefundDAO.selectRefunding(paramMap);
        if(null != ticketRefundPOList) {
            for (int i = 0; i < ticketRefundPOList.size(); i++) {
                ticketRefundEntityList.add(TicketRefundPOToEntity(ticketRefundPOList.get(i)));
            }
        }
        int sum = ticketRefundDAO.selectRefundingCount();
        Page result = new Page();
        result.setCurrentPage(begin + 1);
        result.setPageSize(limit);
        result.setRows(sum);
        result.setResultList(ticketRefundEntityList);

        return result;
    }

    public TicketRefundEntity selectByTicketId(Integer ticketId) {
        return TicketRefundPOToEntity(ticketRefundDAO.selectByTicketId(ticketId));
    }

    public Page<TicketRefundEntity> selectRefunded(Integer begin, Integer limit) {
        List<TicketRefundEntity> ticketRefundEntityList = new ArrayList<>();
        Map paramMap = new HashMap<>();
        paramMap.put("begin", (begin - 1) * limit);
        paramMap.put("limit", limit);
        List<TicketRefundPO> ticketRefundPOList = ticketRefundDAO.selectRefunded(paramMap);
        if(null != ticketRefundPOList) {
            for (int i = 0; i < ticketRefundPOList.size(); i++) {
                ticketRefundEntityList.add(TicketRefundPOToEntity(ticketRefundPOList.get(i)));
            }
        }
        int sum = ticketRefundDAO.selectRefundedCount();
        Page result = new Page();
        result.setCurrentPage(begin + 1);
        result.setRows(sum);
        result.setResultList(ticketRefundEntityList);

        return result;
    }

    public int refuseByPrimaryKey(Integer id) {
        return ticketRefundDAO.refuseByPrimaryKey(id);
    }

    public int accessByPrimaryKey(Integer id) {
        return ticketRefundDAO.accessByPrimaryKey(id);
    }

    /**
     * TicketRefundPO 转化为 TicketRefundEntity
     * @param po
     * @return TicketRefundEntity
     */
    public TicketRefundEntity TicketRefundPOToEntity(TicketRefundPO po) {
        if(null == po) {
            return null;
        }
        TicketRefundEntity entity = new TicketRefundEntity();
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getTicketid()) {
            entity.ticketid = po.getTicketid();
        }
        if(null != po.getStatus()) {
            entity.status = po.getStatus();
        }
        if(null != po.getDeleted()) {
            entity.deleted = po.getDeleted();
        }
        if(null != po.getCreatetime()) {
            entity.createtime = CommonTool.dataToStringYMDHMS(po.getCreatetime());
        }
        if(null != po.getUpdatetime()) {
            entity.updatetime = CommonTool.dataToStringYMDHMS(po.getUpdatetime());
        }
        return entity;
    }

    /**
     * TicketRefundEntity 转化为 TicketRefundPO
     * @param entity
     * @return TicketRefundPO
     */
    public TicketRefundPO TicketRefundEntityToPO(TicketRefundEntity entity) {
        TicketRefundPO po = new TicketRefundPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.ticketid ) {
            po.setTicketid(entity.ticketid );
        }
        if(null != entity.status ) {
            po.setStatus(entity.status);
        }
        if(null != entity.deleted) {
            po.setDeleted(entity.deleted);
        }
        if(null != entity.createtime) {
            po.setCreatetime(CommonTool.stringToDataYMDHMS(entity.createtime));
        }
        if(null != entity.updatetime) {
            po.setUpdatetime(CommonTool.stringToDataYMDHMS(entity.updatetime));
        }
        return po;
    }
}
