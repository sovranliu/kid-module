package com.xyzq.kid.logic.book.entity;

import java.util.Date;

/**
 * 可预约时间段维护
 */
public class BookTimeSpanEntity {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 可预约时间段 HH24:MI-HH24:MI
     */
    private String timeSpan;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    /**
     * 是否删除，0：否，默认为0，1：已删除
     */
    private String deleteFlag;
}
