package com.xyzq.kid.logic.message.entity;

import java.util.Date;

/**
 * 在线留言
 */
public class MessageEntity {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 留言内容
     */
    private String message;
    /**
     * 回复内容
     */
    private String answer;
    /**
     * 回复人
     */
    private String answerPerson;
    /**
     * 回复时间
     */
    private Date answerTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    /**
     *  是否删除标记，0：未删除，默认为0，1：已删除
     */
    private String deleteFlag;
}
