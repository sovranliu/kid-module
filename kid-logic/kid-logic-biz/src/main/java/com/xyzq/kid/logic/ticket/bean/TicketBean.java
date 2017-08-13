package com.xyzq.kid.logic.ticket.bean;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.ticket.dao.TicketDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketPO;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketHistoryEntity;
import com.xyzq.kid.logic.user.dao.UserDAO;
import com.xyzq.kid.logic.user.dao.po.UserPO;
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
public class TicketBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private UserDAO userDAO;


    /**
     * 根据主键查下飞行票信息
     *
     * @return 返回值
     */
    public TicketEntity selectByPrimaryKey(int id) {
        TicketPO ticketPO = ticketDAO.selectByPrimaryKey(id);
        return TicketPOToEntity(ticketPO);
    }

    /**
     * 根据流水号获取个人票务信息
     * @param serialno
     * @return
     */
    public TicketEntity getTicketsInfoBySerialno(String serialno ){
        TicketPO ticketPO = ticketDAO.getTicketsInfoBySerialno(serialno);
        return TicketPOToEntity(ticketPO);
    }

    public List<TicketEntity> getTicketsInfoByOwnerMobileNo(String mobileNo) {
        List<TicketEntity> ticketEntityList = new ArrayList<>();
        UserPO userPO = userDAO.selectByMolieNo(mobileNo);
        if(null != userPO) {
            List<TicketPO> ticketPOList = ticketDAO.getTicketsInfoByOwnerMobileNo(userPO.getOpenid());

            if (null != ticketPOList) {
                for (int i = 0; i < ticketPOList.size(); i++) {
                    if (ticketPOList.get(i).getDeleted() == CommonTool.STATUS_NORMAL) {
                        ticketEntityList.add(TicketPOToEntity(ticketPOList.get(i)));
                    }
                }
            }
        }
        return ticketEntityList;
    }

    public List<TicketEntity> getTicketsInfoByPayerOpenID(String openID) {
        List<TicketEntity> ticketEntityList = new ArrayList<>();
        List<TicketPO> ticketPOList = ticketDAO.getTicketsInfoByPayerOpenID(openID);
        if(null != ticketPOList) {
            for (int i = 0; i < ticketPOList.size(); i++) {
                if(ticketPOList.get(i).getDeleted() == CommonTool.STATUS_NORMAL) {
                    ticketEntityList.add(TicketPOToEntity(ticketPOList.get(i)));
                }
            }
        }
        return ticketEntityList;
    }

    /**
     * 根据主键删除飞行票信息
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return ticketDAO.deleteByPrimaryKey(id);
    }

    /**
     * 全量新增飞行票信息
     * @param entity
     * @return
     */
    public int insert(TicketEntity entity){
        TicketPO ticketPO = TicketEntityToPO(entity);
        ticketDAO.insert(ticketPO);
        return ticketPO.getId();
    }

    /**
     * 选择性新增飞行票信息
     * @param entity
     * @return
     */
    public int insertSelective(TicketEntity entity){
        TicketPO ticketPO = TicketEntityToPO(entity);
        ticketDAO.insertSelective(ticketPO);
        return ticketPO.getId();
    }

    /**
     * 根据主键选择性更新飞行票信息
     * @param entity
     * @return
     */
    public int updateByPrimaryKeySelective(TicketEntity entity){
        return ticketDAO.updateByPrimaryKeySelective(TicketEntityToPO(entity));
    }

    /**
     * 根据主键全量更新飞行票信息
     * @param entity
     * @return
     */
    public int updateByPrimaryKey(TicketEntity entity){
        return ticketDAO.updateByPrimaryKey(TicketEntityToPO(entity));
    }

    /**
     * 展期
     * @param entity
     * @return
     */
    public int updateExtendByPrimaryKey(TicketEntity entity){
        return ticketDAO.updateExtendByPrimaryKey(TicketEntityToPO(entity));
    }

    /**
     * 过期
     * @param id
     * @return
     */
    public int updateExpiredByPrimaryKey(Integer id){
        return ticketDAO.updateExpiredByPrimaryKey(id);
    }

    /**
     * 退票
     * @param id
     * @return
     */
    public int updateRefundByPrimaryKey(Integer id){
        return ticketDAO.updateRefundByPrimaryKey(id);
    }

    /**
     * 申请退票
     * @param id
     * @return
     */
    public int updateRefundingByPrimaryKey(Integer id){
        return ticketDAO.updateRefundingByPrimaryKey(id);
    }

    /**
     * 赠送
     * @param entity
     * @return
     */
    public int updateHandselByPrimaryKey(TicketEntity entity){
        return ticketDAO.updateHandselByPrimaryKey(TicketEntityToPO(entity));
    }

    /**
     * 使用
     * @param id
     * @return
     */
    public int updateUseByPrimaryKey(Integer id){
        return ticketDAO.updateUseByPrimaryKey(id);
    }

    /**
     * 使用
     * @param paramMap
     * @return
     */
    public int updateHandselByPrimaryKeyLock(Map paramMap){
        return ticketDAO.updateHandselByPrimaryKeyLock(paramMap);
    }

    /**
     * 恢复
     * @param id
     * @return
     */
    public int updateRecoverByPrimaryKey(Integer id){
        return ticketDAO.updateRecoverByPrimaryKey(id);
    }

    public Page<TicketEntity> queryTicketByCond(Map paramMap) {
        List<TicketEntity> ticketEntityList = new ArrayList<>();
        List<TicketPO> ticketPOList = ticketDAO.queryTicketByCond(paramMap);
        if(null != ticketPOList) {
            for (int i = 0; i < ticketPOList.size(); i++) {
                ticketEntityList.add(TicketPOToEntity(ticketPOList.get(i)));
            }
        }
        int sum = ticketDAO.queryTicketByCondCount(paramMap);

        Page result = new Page();
        result.setCurrentPage((Integer)paramMap.get("begin") + 1);
        result.setPageSize((Integer)paramMap.get("limit"));
        result.setRows(sum);
        result.setResultList(ticketEntityList);

        return result;
    }

    /**
     * TicketPO 转化为 TicketEntity
     * @param po
     * @return TicketEntity
     */
    public TicketEntity TicketPOToEntity(TicketPO po) {
        if(null == po) {
            return null;
        }
        TicketEntity entity = new TicketEntity();
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getSerialno()) {
            entity.serialNumber = po.getSerialno();
        }
        if(null != po.getType()) {
            entity.type = po.getType();
        }
        if(null != po.getOwneropenid()) {
            UserPO userPO = userDAO.selectByOpenId(po.getOwneropenid());
            if(null != userPO) {
                entity.telephone = userPO.getMobileno();
            }
        }
        if(null != po.getPayeropenid()) {
            entity.payeropenid = po.getPayeropenid();
        }
        if(null != po.getPrice()) {
            entity.price = po.getPrice();
        }
        if(null != po.getExpiredate()) {
            entity.expire = CommonTool.dataToStringYMD(po.getExpiredate());
        }
        if(null != po.getInsurance()) {
            entity.insurance = po.getInsurance();
        }
        if(null != po.getOrderno()) {
            entity.payNumber = po.getOrderno();
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
     * TicketEntity 转化为 TicketPO
     * @param entity
     * @return TicketPO
     */
    public TicketPO TicketEntityToPO(TicketEntity entity) {
        TicketPO po = new TicketPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.serialNumber)  {
            po.setSerialno(entity.serialNumber);
        }
        if(null != entity.type ) {
            po.setType(entity.type);
        }
        if(null != entity.telephone) {
            UserPO userPO = userDAO.selectByMolieNo(entity.telephone);
            if(null != userPO) {
                po.setOwneropenid(userPO.getOpenid());
            }
        }
        if(null != entity.payeropenid) {
            po.setPayeropenid(entity.payeropenid);
        }
        if(null != entity.price) {
            po.setPrice(entity.price);
        }
        if(null != entity.expire) {
            po.setExpiredate(CommonTool.stringToDataYMD(entity.expire));
        }
        if(null != entity.insurance) {
            po.setInsurance(entity.insurance);
        }
        if(null != entity.payNumber) {
         po.setOrderno(entity.payNumber);
        }
        if(null != entity.status) {
         po.setStatus(entity.status);
        }
        if(null != entity.deleted ) {
            po.setDeleted(entity.deleted);
        }
//        if(null != entity.createtime) {
//            po.setCreatetime(CommonTool.StringToDataYMDHMS(entity.createtime));
//        }
//        if(null != entity.updatetime) {
//            po.setUpdatetime(CommonTool.StringToDataYMDHMS(entity.updatetime));
//        }
        return po;
    }

//    public int updateMobileNo(String mobile, String mobilePre) {
//        Map paramMap = new HashMap();
//        paramMap.put("mobileno", mobile);
//        paramMap.put("mobilenoPre", mobilePre);
//
//        return ticketDAO.updateMobileNo(paramMap);
//    }

}
