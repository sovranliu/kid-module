package com.xyzq.kid.logic.record.entity;

import com.xyzq.kid.logic.record.dao.po.FlyRecordPO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class RecordEntity {
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


    public RecordEntity(RecordPO recordPO){
        id = recordPO.getId();
        ticketID = recordPO.getTicketID();
        path = recordPO.getPath();
        purchased=recordPO.getPurchased();
        deleted = recordPO.getDeleted();
        creator = recordPO.getCreator();
        createTime = recordPO.getCreateTime();
        updator = recordPO.getUpdator();
        updateTime = recordPO.getUpdateTime();
    }
}
