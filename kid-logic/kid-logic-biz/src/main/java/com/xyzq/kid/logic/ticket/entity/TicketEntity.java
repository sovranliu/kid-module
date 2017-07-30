package com.xyzq.kid.logic.ticket.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 范例实体
 */
public class TicketEntity {

    public final static int TICKET_TYPE_SINGLE = 1;//个人票
    public final static int TICKET_TYPE_GROUP = 0;//团体票

    public final static int TICKET_STATUS_NEW = 0;//未使用
    public final static int TICKET_STATUS_USED = 1;//已使用
    public final static int TICKET_STATUS_EXPIRED = 2;//过期
    public final static int TICKET_STATUS_BACK = 3;//退票

    /**
     * 飞行票主键
     */
    public Integer id;
    /**
     * 流水号
     */
    public String serialno;
    /**
     * 类型，1：单人票，2：团体票
     */
    public Integer type;
    /**
     * 购买者手机号码
     */
    public String ownermobileno;
    /**
     * 支付者OpenID
     */
    public String payeropenid;
    /**
     * 购买价格，单位分
     */
    public BigDecimal price;
    /**
     * 截至日期
     */
    public String expiredate;
    /**
     * 是否包含保险，0：没有，1：包含
     */
    public Boolean insurance;
    /**
     * 支付订单号
     */
    public String orderno;
    /**
     * 状态，0：未使用，1：已使用，2：过期，3：退票
     */
    public Integer status;
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

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + id +
                ", serialno='" + serialno + '\'' +
                ", type=" + type +
                ", ownermobileno='" + ownermobileno + '\'' +
                ", payeropenid='" + payeropenid + '\'' +
                ", price=" + price +
                ", expiredate='" + expiredate + '\'' +
                ", insurance=" + insurance +
                ", orderno='" + orderno + '\'' +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
