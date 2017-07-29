package com.xyzq.kid.logic.admin.dao.po;

import java.sql.Timestamp;

/**
 * CNS_Admin
 * Created by Brann on 17/7/27.
 */
public class AdminPO {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
