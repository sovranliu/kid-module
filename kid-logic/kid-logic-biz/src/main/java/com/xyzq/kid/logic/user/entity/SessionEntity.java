package com.xyzq.kid.logic.user.entity;

import com.xyzq.simpson.base.etc.Serial;

/**
 * 会话实体
 */
public class SessionEntity {
    /**
     * 会话ID
     *
     * 存在于终端的Cookie中
     */
    public String sId;
    /**
     * 手机号码
     */
    public String mobileNo;
    /**
     * 微信用户开放ID
     */
    public String openId;


    /**
     * 构造函数
     */
    public SessionEntity() { }

    /**
     * 构造函数
     *
     * @param sId 会话ID
     * @param mobileNo 手机号码
     * @param openId 微信用户开放ID
     */
    public SessionEntity(String sId, String mobileNo, String openId) {
        this.sId = sId;
        this.mobileNo = mobileNo;
        this.openId = openId;
    }

    /**
     * 构造函数
     *
     * @param sId 会话ID
     * @param mobileNoOpenId 手机号码与微信用户开放ID
     */
    public SessionEntity(String sId, String mobileNoOpenId) {
        this.sId = sId;
        this.mobileNo = mobileNoOpenId.split(",")[0].trim();
        this.openId = mobileNoOpenId.split(",")[1].trim();
    }

    /**
     * 自动生成会话ID
     */
    public void makeSId() {
        this.sId = Serial.makeLocalID();
    }
}
