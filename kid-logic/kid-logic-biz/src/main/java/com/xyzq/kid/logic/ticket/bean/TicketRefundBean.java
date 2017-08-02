package com.xyzq.kid.logic.ticket.bean;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.ticket.dao.TicketRefundDAO;
import com.xyzq.kid.logic.ticket.dao.TicketRefundDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<TicketRefundEntity> selectRefunding(Integer num) {
        List<TicketRefundEntity> ticketRefundEntityList = new ArrayList<>();
        List<TicketRefundPO> ticketRefundPOList = ticketRefundDAO.selectRefunding(num);
        if(null != ticketRefundPOList) {
            for (int i = 0; i < ticketRefundPOList.size(); i++) {
                ticketRefundEntityList.add(TicketRefundPOToEntity(ticketRefundPOList.get(i)));
            }
        }
        return ticketRefundEntityList;
    }

    public List<TicketRefundEntity> selectRefunded(Integer limit) {
        List<TicketRefundEntity> ticketRefundEntityList = new ArrayList<>();
        List<TicketRefundPO> ticketRefundPOList = ticketRefundDAO.selectRefunded(limit);
        if(null != ticketRefundPOList) {
            for (int i = 0; i < ticketRefundPOList.size(); i++) {
                ticketRefundEntityList.add(TicketRefundPOToEntity(ticketRefundPOList.get(i)));
            }
        }
        return ticketRefundEntityList;
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
            entity.createtime = CommonTool.DataToStringYMDHMS(po.getCreatetime());
        }
        if(null != po.getUpdatetime()) {
            entity.updatetime = CommonTool.DataToStringYMDHMS(po.getUpdatetime());
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
            po.setCreatetime(CommonTool.StringToDataYMDHMS(entity.createtime));
        }
        if(null != entity.updatetime) {
            po.setUpdatetime(CommonTool.StringToDataYMDHMS(entity.updatetime));
        }
        return po;
    }
}
