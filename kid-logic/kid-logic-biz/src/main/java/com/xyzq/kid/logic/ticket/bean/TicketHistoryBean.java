package com.xyzq.kid.logic.ticket.bean;

import com.xyzq.kid.logic.user.common.CommonTool;
import com.xyzq.kid.logic.ticket.dao.TicketHistoryDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketHistoryPO;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class TicketHistoryBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private TicketHistoryDAO ticketHistoryDAO;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public TicketHistoryEntity selectByPrimaryKey(int id) {
        TicketHistoryPO ticketHistoryPO = ticketHistoryDAO.selectByPrimaryKey(id);
        return TicketHistoryPOToEntity(ticketHistoryPO);
    }

    /**
     * 根据主键删除飞行票信息
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return ticketHistoryDAO.deleteByPrimaryKey(id);
    }

    /**
     * 全量新增飞行票信息
     * @param entity
     * @return
     */
    public int insert(TicketHistoryEntity entity){
        return ticketHistoryDAO.insert(TicketHistoryEntityToPO(entity));
    }

    /**
     * 选择性新增飞行票信息
     * @param entity
     * @return
     */
    public int insertSelective(TicketHistoryEntity entity){
        return ticketHistoryDAO.insertSelective(TicketHistoryEntityToPO(entity));
    }

    /**
     * 根据主键选择性更新飞行票信息
     * @param entity
     * @return
     */
    public int updateByPrimaryKeySelective(TicketHistoryEntity entity){
        return ticketHistoryDAO.updateByPrimaryKeySelective(TicketHistoryEntityToPO(entity));
    }

    /**
     * TicketHistoryPO 转化为 TicketHistoryEntity
     * @param po
     * @return TicketHistoryEntity
     */
    public TicketHistoryEntity TicketHistoryPOToEntity(TicketHistoryPO po) {
        if(null == po) {
            return null;
        }
        TicketHistoryEntity entity = new TicketHistoryEntity();
        entity.id = po.getId();
        entity.ticketid = po.getId();
        entity.action = po.getAction();
        entity.prevalidperiod = CommonTool.DataToStringYMD(po.getPrevalidperiod());
        entity.premobile = po.getPremobile();
        entity.deleted = po.getDeleted();
        entity.createtime = CommonTool.DataToStringYMDHMS(po.getCreatetime());
        entity.updatetime = CommonTool.DataToStringYMDHMS(po.getUpdatetime());
        return entity;
    }

    /**
     * TicketHistoryEntity 转化为 TicketHistoryPO
     * @param entity
     * @return TicketHistoryPO
     */
    public TicketHistoryPO TicketHistoryEntityToPO(TicketHistoryEntity entity) {
        TicketHistoryPO po = new TicketHistoryPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.ticketid ) {
            po.setId(entity.ticketid );
        }
        if(null != entity.action ) {
            po.setAction(entity.action);
        }
        if(null != entity.prevalidperiod) {
            po.setPrevalidperiod(CommonTool.StringToDataYMD(entity.prevalidperiod));
        }
        if(null != entity.premobile) {
            po.setPremobile(entity.premobile);
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
