package com.xyzq.kid.logic.ticket.bean;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.ticket.dao.TicketHistoryDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketHistoryPO;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<TicketHistoryEntity> selectByTicketId(int ticketId) {
        List<TicketHistoryEntity> ticketHistoryEntities = new ArrayList<>();
        List<TicketHistoryPO> ticketHistoryPOList = ticketHistoryDAO.selectByTicketId(ticketId);
        if(null != ticketHistoryPOList) {
            for (int i = 0; i < ticketHistoryPOList.size(); i++) {
                if(ticketHistoryPOList.get(i).getDeleted() == CommonTool.STATUS_NORMAL) {
                    ticketHistoryEntities.add(TicketHistoryPOToEntity(ticketHistoryPOList.get(i)));
                }
            }
        }
        return ticketHistoryEntities;
    }

    public List<TicketHistoryEntity> selectByPreMobile(String mobileNo) {
        List<TicketHistoryEntity> ticketHistoryEntities = new ArrayList<>();
        List<TicketHistoryPO> ticketHistoryPOList = ticketHistoryDAO.selectByPreMobile(mobileNo);
        if(null != ticketHistoryPOList) {
            for (int i = 0; i < ticketHistoryPOList.size(); i++) {
                if(ticketHistoryPOList.get(i).getDeleted() == CommonTool.STATUS_NORMAL) {
                    ticketHistoryEntities.add(TicketHistoryPOToEntity(ticketHistoryPOList.get(i)));
                }
            }
        }
        return ticketHistoryEntities;
    }

    /**
     * 获取增票记录
     * @param mobileNo
     * @return
     */
    public List<TicketHistoryEntity> selectHandselByPreMobile(String mobileNo) {
        List<TicketHistoryEntity> ticketHistoryEntities = new ArrayList<>();
        List<TicketHistoryPO> ticketHistoryPOList = ticketHistoryDAO.selectByPreMobile(mobileNo);
        if(null != ticketHistoryPOList) {
            for (int i = 0; i < ticketHistoryPOList.size(); i++) {
                if(ticketHistoryPOList.get(i).getDeleted() == CommonTool.STATUS_NORMAL  &&
                        ticketHistoryPOList.get(i).getAction() == TicketHistoryEntity.TICKET_ACTION_HANDSEL) {
                    ticketHistoryEntities.add(TicketHistoryPOToEntity(ticketHistoryPOList.get(i)));
                }
            }
        }
        return ticketHistoryEntities;
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
        TicketHistoryPO ticketHistoryPO = TicketHistoryEntityToPO(entity);
        ticketHistoryDAO.insert(ticketHistoryPO);
        return ticketHistoryPO.getId();
    }

    /**
     * 选择性新增飞行票信息
     * @param entity
     * @return
     */
    public int insertSelective(TicketHistoryEntity entity){
        TicketHistoryPO ticketHistoryPO = TicketHistoryEntityToPO(entity);
        ticketHistoryDAO.insertSelective(ticketHistoryPO);
        return ticketHistoryPO.getId();
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
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getTicketid()) {
            entity.ticketid = po.getTicketid();
        }
        if(null != po.getAction()) {
            entity.action = po.getAction();
        }
        if(null != po.getPrevalidperiod()) {
            entity.prevalidperiod = CommonTool.dataToStringYMD(po.getPrevalidperiod());
        }
        if(null != po.getPremobile()) {
            entity.premobile = po.getPremobile();
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
            po.setTicketid(entity.ticketid );
        }
        if(null != entity.action ) {
            po.setAction(entity.action);
        }
        if(null != entity.prevalidperiod) {
            po.setPrevalidperiod(CommonTool.stringToDataYMD(entity.prevalidperiod));
        }
        if(null != entity.premobile) {
            po.setPremobile(entity.premobile);
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
