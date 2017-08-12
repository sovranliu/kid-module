package com.xyzq.kid.logic.user.bean;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.user.dao.UserDAO;
import com.xyzq.kid.logic.user.dao.po.UserPO;
import com.xyzq.kid.logic.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.H;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class UserBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private UserDAO userDAO;

    public Page<UserEntity> queryUserByCond(Map paramMap) {
        List<UserEntity> userEntityList = new ArrayList<>();
        List<UserPO> userPOList = userDAO.queryUserByCond(paramMap);
        if(null != userPOList) {
            for (int i = 0; i < userPOList.size(); i++) {
                userEntityList.add(UserPOToEntity(userPOList.get(i)));
            }
        }
        int sum = userDAO.queryUserByCondCount(paramMap);

        Page result = new Page();
        result.setCurrentPage((Integer)paramMap.get("begin") + 1);
        result.setPageSize((Integer)paramMap.get("limit"));
        result.setRows(sum);
        result.setResultList(userEntityList);

        return result;
    }

    public int readPostBenefit(String mobileno) {
        return userDAO.readPostBenefit(mobileno);
    }

    public int updateMobileNo(String mobile, String mobilePre) {
        Map paramMap = new HashMap();
        paramMap.put("mobileno", mobile);
        paramMap.put("mobilenoPre", mobilePre);

        return userDAO.updateMobileNo(paramMap);
    }

    /**
     * 根据主键查询用户信息
     * @param id 主键
     * @return
     */
    public UserEntity selectByPrimaryKey(int id) {
        return UserPOToEntity(userDAO.selectByPrimaryKey(id));
    }

    public UserEntity selectByMolieNo(String mobileNo) {
        return UserPOToEntity(userDAO.selectByMolieNo(mobileNo));
    }

    /**
     * 根据主键删除用户
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return userDAO.deleteByPrimaryKey(id);
    }

    /**
     * 全量新增用户信息
     * @param entity
     * @return
     */
    public int insert(UserEntity entity){
        UserPO userPO = UserEntityToPO(entity);
        userDAO.insert(userPO);
        return userPO.getId();
    }

    /**
     * 部分新增用户信息
     * @param entity
     * @return
     */
    public int insertSelective(UserEntity entity){
        UserPO userPO = UserEntityToPO(entity);
        userDAO.insertSelective(userPO);
        return userPO.getId();
    }

    public int updateByMobileNo(UserEntity entity){
        return userDAO.updateByMobileNo(UserEntityToPO(entity));
    }

    /**
     * 选择性更新用户信息
     * @param entity
     * @return
     */
    public int updateByPrimaryKeySelective(UserEntity entity){
        return userDAO.updateByPrimaryKeySelective(UserEntityToPO(entity));
    }

    /**
     * 全量更新用户信息
     * @param entity
     * @return
     */
    public int updateByPrimaryKey(UserEntity entity){
        return userDAO.updateByPrimaryKey(UserEntityToPO(entity));
    }

    /**
     * UserPO转UserEntity
     * @param po
     * @return UserEntity
     */
    public UserEntity UserPOToEntity(UserPO po){
        if(null == po) {
            return null;
        }
        UserEntity entity = new UserEntity();
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getMobileno()) {
            entity.telephone = po.getMobileno();
        }
        if(null != po.getOpenid()) {
            entity.openid = po.getOpenid();
        }
        if(null != po.getRealname()) {
            entity.userName = po.getRealname();
        }
        if(null != po.getAddress()) {
            entity.address = po.getAddress();
        }
        if(null != po.getGender()) {
            entity.sex = po.getGender();
        }
        if(null != po.getSubscribetime()) {
            entity.subscribetime = CommonTool.dataToStringYMDHMS(po.getSubscribetime());
        }
        if(null != po.getAvatarUrl()) {
            entity.avatarUrl = po.getAvatarUrl();
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
     * UserEntity装UserPO
     * @param entity
     * @return UserPO
     */
    public UserPO UserEntityToPO(UserEntity entity){
        UserPO po = new UserPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.telephone) {
            po.setMobileno(entity.telephone);
        }
        if(null != entity.openid) {
            po.setOpenid(entity.openid);
        }
        if(null != entity.userName) {
            po.setRealname(entity.userName);
        }
        if(null != entity.address) {
            po.setAddress( entity.address);
        }
        po.setGender(entity.sex);
        if(null != entity.subscribetime) {
            po.setSubscribetime(CommonTool.stringToDataYMDHMS(entity.subscribetime));
        }
        if(null != entity.avatarUrl) {
            po.setAvatarUrl( entity.avatarUrl);
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
    /**
     * 根据openId查询用户信息
     * @param openId
     * @return UserEntity
     */
    public UserEntity selectByOpenId(String openId) {
        return UserPOToEntity(userDAO.selectByOpenId(openId));
    }
}
