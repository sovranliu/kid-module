package com.xyzq.kid.finance.dao.po;

import java.util.Date;

/**
 * 收款持久化对象
 */
public class ReceiptPO {
    /**
     * 订单ID
     */
    private int id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 支付者微信开放ID
     */
    private String openId;
    /**
     * 收款金额（分）
     */
    private int amount;
    /**
     * 微信支付订单号
     */
    private String transactionId;
    /**
     * 收款记录方式：notify，query
     */
    private String mode;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
