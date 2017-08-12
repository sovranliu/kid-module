package com.xyzq.kid.logic.record.dao.po;

import com.xyzq.kid.logic.record.entity.RecordEntity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Brann on 17/7/27.
 */
public class RecordPO {
    /**
     * 主键ID，自增长的流水号
     */
    private Integer id;
    /**
     * 票券的serialNumber
     */
    private String serialNo;
    /**
     * 飞行日志文件名称
     */
    private String path;
    /**
     * 是否被购买，0:未购买，1:已购买
     */
    private String purchased;
    /**
     * 记录是否被软删，1:删除，0:未删除
     */
    private String deleted;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public RecordPO() {
        purchased = RecordEntity.UNPURCHASED;
        deleted = RecordEntity.STATUS_NORMAL;
        createTime = new Timestamp(System.currentTimeMillis());
        updateTime =new Timestamp(System.currentTimeMillis());
    }

    public RecordPO(RecordEntity entity) {
        if (entity != null) {
            id = entity.id;
            serialNo = entity.serialNo;
            path = entity.path;
            purchased = entity.purchased;
            deleted = entity.deleted;
            createTime = entity.createTime;
            updateTime = entity.updateTime;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSerialNumber() {
        return serialNo;
    }

    public void setSerialNumber(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
