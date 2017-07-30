package com.xyzq.kid.logic.record.entity;

import com.xyzq.kid.logic.record.dao.po.RecordPO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class RecordEntity {

    public final static String PURCHASED = "1";//已购买
    public final static String UNPURCHASED = "0";//未购买
    /**
     * 主键ID，自增长的流水号
     */
    public Integer id;
    /**
     * 票券的id
     */
    public Integer ticketID;
    /**
     * 飞行日志文件名称
     */
    public String path;
    /**
     * 是否被购买，0:未购买，1:已购买
     */
    public String purchased;
    /**
     * 记录是否被软删，1:删除，0:未删除
     */
    public String deleted;
    /**
     * 记录创建人的id
     */
    public String creator;
    /**
     * 创建时间
     */
    public Timestamp createTime;
    /**
     * 最后一次更新人的id
     */
    public String updator;
    /**
     * 更新时间
     */
    public Timestamp updateTime;


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
