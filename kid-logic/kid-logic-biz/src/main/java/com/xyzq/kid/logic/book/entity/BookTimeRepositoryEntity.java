package com.xyzq.kid.logic.book.entity;

import java.util.Date;

/**
 * 范例持久对象
 */
public class BookTimeRepositoryEntity {
    /**
     * 主键ID
     */
    private int id;
    
    /**
     * 预约日期YYYY-MM-DD
     */
    private String bookDate;
    /**
     * 可预约时间段ID
     */
    private int bookTimeSpanId;
    /**
     * 初始总库存
     */
    private int bookTotal;
    /**
     * 剩余库存
     */
    private int bookAmount;
    /**
     *创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    /**
     * 创建人
     */
    private String createPerson;
    /**
     * 是否删除标记，0：未删除，默认为0，1：已删除
     */
    private String deleteFlag;
    
    
}
