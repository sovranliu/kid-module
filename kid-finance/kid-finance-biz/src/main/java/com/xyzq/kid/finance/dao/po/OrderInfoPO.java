package com.xyzq.kid.finance.dao.po;

/**
 * 订单信息持久化对象
 */
public class OrderInfoPO extends OrderPO {
    /**
     * 手机号码
     */
    private String mobileNo;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 票流水号
     */
    private String serialNo;


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
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
