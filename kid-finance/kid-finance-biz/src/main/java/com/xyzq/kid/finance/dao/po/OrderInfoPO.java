package com.xyzq.kid.finance.dao.po;

/**
 * 订单信息持久化对象
 */
public class OrderInfoPO extends OrderPO {
    /**
     * 手机号码
     */
    private String mobileNo;


    public String getMobileNo() {
        return mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
