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
     * 支付者微信开放ID
     */
    private String openId;
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
}
