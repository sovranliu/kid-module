package com.xyzq.kid.logic.book.entity;

import java.util.Date;

/**
 * 范例持久对象
 */
public class BookHistoryEntity {
    /**
     * 主键ID
     */
    private int id;
    /**
     * 关联的预约记录ID
     */
    private int bookId;
    /**
     * 关联的可预约时间仓库ID
     */
    private int bookTimeId;
    /**
     * 预约日期 YYY-MM-DD
     */
    private String bookDate;
    /**
     * 预约时段 HH24:MI-HH24:MI
     */
    private String bookTime;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 是否删除，0：否，默认为0，1：已删除
     */
    private String deleteFlag;
}
