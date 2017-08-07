package com.xyzq.kid.logic.ticket.entity;

/**
 * 范例实体
 */
public class TicketRefundEntity {

    public final static int REFUND_STATUS_NEW = 1;//申请中
    public final static int REFUND_STATUS_SUCCESS = 2;//批准
    public final static int REFUND_STATUS_FAIL = 3;//驳回
    /**
     * 飞行票历史操作主键
     */
    public Integer id;
    /**
     * 飞行票票ID
     */
    public Integer ticketid;
    /**
     * 1：申请中，2：批准，3：驳回
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
        return "TicketRefundEntity{" +
                "id=" + id +
                ", ticketid=" + ticketid +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
