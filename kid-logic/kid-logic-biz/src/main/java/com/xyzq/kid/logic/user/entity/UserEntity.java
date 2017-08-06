package com.xyzq.kid.logic.user.entity;


/**
 * 用户信息
 */
public class UserEntity {
    public final static int USER_MALE = 1;//男
    public final static int USER_FEMALE = 1;//女

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
    public int gender = 0;
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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", mobileno='" + mobileno + '\'' +
                ", openid='" + openid + '\'' +
                ", realname='" + realname + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", subscribetime='" + subscribetime + '\'' +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
