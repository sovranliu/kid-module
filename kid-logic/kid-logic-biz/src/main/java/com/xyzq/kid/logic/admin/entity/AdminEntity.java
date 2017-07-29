package com.xyzq.kid.logic.admin.entity;

import com.xyzq.kid.logic.admin.dao.po.AdminPO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class AdminEntity {
    /**
     * 主键ID，自增长的流水号
     */
    private int id;
    /**
     * 票券的id
     */
    private int userName;
    /**
     * 飞行日志文件名称
     */
    private String password;
    /**
     * 是否被购买，0:未购买，1:已购买
     */
    private String email;
    /**
     * 记录是否被软删，1:删除，0:未删除
     */
    private String mobile;
    /**
     * 记录是否被软删，1:删除，0:未删除
     */
    private String deleted;
    /**
     * 记录创建人的id
     */
    private String creator;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后一次更新人的id
     */
    private String updator;
    /**
     * 更新时间
     */
    private Timestamp updateTime;


    public AdminEntity(AdminPO adminPO){
        id = adminPO.getId();
        userName = adminPO.getUserName();
        password = adminPO.getPassword();
        email = adminPO.getEmail();
        mobile = adminPO.getMobile();
        deleted = adminPO.getDeleted();
        creator = adminPO.getCreator();
        createTime = adminPO.getCreateTime();
        updator = adminPO.getUpdator();
        updateTime = adminPO.getUpdateTime();
    }
}
