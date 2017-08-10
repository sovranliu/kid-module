package com.xyzq.kid.finance.dao.po;

/**
 * 退款信息持久化对象
 */
public class RefundInfoPO extends RefundPO {
    /**
     * 手机号码
     */
    private String mobileNo;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 支付者微信开放ID
     */
    private String openId;
    /**
     * 票流水号
     */
    private String serialNo;
    /**
     * 附属信息
     */
    private String tag;
    /**
     * 商品类型
     */
    private int goodsType;
    /**
     * 支付金额
     */
    private int fee;


    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public int getGoodsType() {
        return goodsType;
    }
    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public int getFee() {
        return fee;
    }
    public void setFee(int fee) {
        this.fee = fee;
    }
}
