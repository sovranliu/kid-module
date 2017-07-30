package com.xyzq.kid.finance.dao.po;

import java.util.Date;

/**
 * 退款持久化对象
 */
public class RefundPO {
    /**
     * 订单ID
     */
    private int id;
    /**
     * 退款号
     */
    private String refundNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退款金额（分）
     */
    private int refundFee;
    /**
     * 退款原因
     */
    private String reason;
    /**
     * 微信返回退款ID
     */
    private String refundId;
    /**
     * 状态，0：初始，1：退款中，2：退款成功，3：退款失败
     */
    private int state;
    /**
     * 退款成功时间
     */
    private Date refundTime;
    /**
     * 是否被软删
     */
    private boolean deleted;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 记录最后更新时间
     */
    private Date updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
