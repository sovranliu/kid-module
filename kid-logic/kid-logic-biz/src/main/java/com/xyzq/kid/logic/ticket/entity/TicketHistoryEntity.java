package com.xyzq.kid.logic.ticket.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 范例实体
 */
public class TicketHistoryEntity {
    /**
     * 飞行票历史操作主键
     */
    public Integer id;
    /**
     * 飞行票票ID
     */
    public Integer ticketid;
    /**
     * 1-新建，2-赠送，3-使用，4-有效期延长
     */
    public Boolean action;
    /**
     * Action为4时，记录下上次截止日
     */
    public String prevalidperiod;
    /**
     * Action为2时，记录下原归属人手机号
     */
    public String premobile;
    /**
     * 记录是否被软删
     */
    public Byte deleted;
    /**
     * 记录创建时间
     */
    public String createtime;
    /**
     * 记录变更时间
     */
    public String updatetime;
}
