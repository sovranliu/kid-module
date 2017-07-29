package com.xyzq.kid.logic.record.entity;

import com.xyzq.kid.logic.record.dao.po.FlyRecordPO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class FlyRecordEntity {
    /**
     * 主键ID，自增长的流水号
     */
    private int id;
    /**
     * 票券的id
     */
    private int ticketID;
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


    public FlyRecordEntity(FlyRecordPO flyRecordPO){
        id = flyRecordPO.getId();
        ticketID = flyRecordPO.getTicketID();
        path = flyRecordPO.getPath();
        purchased=flyRecordPO.getPurchased();
        deleted = flyRecordPO.getDeleted();
        creator = flyRecordPO.getCreator();
        createTime = flyRecordPO.getCreateTime();
        updator = flyRecordPO.getUpdator();
        updateTime = flyRecordPO.getUpdateTime();
    }
}