package com.xyzq.kid.logic.user.entity;


/**
 * 用户信息
 */
public class UserEntity {
    /**
     * 用户主键
     */
    public Integer id;
    /**
     * 手机号
     */
    public String mobileno;
    /**
     * 微信号
     */
    public String openid;
    /**
     * 真实姓名
     */
    public String realname;
    /**
     * 真实姓名
     */
    public String address;
    /**
     * 性别，0位置，1男，2女
     */
    public Integer gender;
    /**
     * 关注时间，null表示未关注
     */
    public String subscribetime;
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
}
