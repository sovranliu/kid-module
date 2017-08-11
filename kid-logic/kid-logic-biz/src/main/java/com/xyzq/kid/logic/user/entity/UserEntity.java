package com.xyzq.kid.logic.user.entity;


/**
 * 用户信息
 */
public class UserEntity {
    public final static int USER_MALE = 1;//男
    public final static int USER_FEMALE = 2;//女

    /**
     * 用户主键
     */
    public Integer id;
    /**
     * 手机号
     */
    public String telephone;
    /**
     * 微信号
     */
    public String openid;
    /**
     * 真实姓名
     */
    public String userName;
    /**
     * 真实姓名
     */
    public String address;
    /**
     * 性别，0位置，1男，2女
     */
    public int sex = 0;
    /**
     * 关注时间，null表示未关注
     */
    public String subscribetime;
    /**
     * 用户头像
     */
    public String avatarUrl;
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
                ", telephone='" + telephone + '\'' +
                ", openid='" + openid + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", sex=" + sex +
                ", subscribetime='" + subscribetime + '\'' +
                "，avatarUrl=" + avatarUrl +
                ", deleted=" + deleted +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
