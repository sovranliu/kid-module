package com.xyzq.kid.finance.dao.po;

import java.util.Date;

/**
 * 订单持久化对象
 */
public class OrderPO {
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
     * 订单金额（分）
     */
    private int fee;
    /**
     * 商品类型
     */
    private int goodsType;
    /**
     * 预支付ID
     */
    private String PrepayId;
    /**
     * 状态，状态，0：初始，1：待支付，2：已支付，3：待退款，4：已退
     */
    private int state;
    /**
     * 附属数据
     */
    private String tag;
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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getPrepayId() {
        return PrepayId;
    }

    public void setPrepayId(String prepayId) {
        PrepayId = prepayId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
