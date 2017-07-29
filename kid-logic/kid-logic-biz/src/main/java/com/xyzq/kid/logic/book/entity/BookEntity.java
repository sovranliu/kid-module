package com.xyzq.kid.logic.book.entity;

import java.util.Date;

/**
 * 预约
 */
public class BookEntity {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 关联票券ID
     */
    private Integer ticketId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 预约日期 YYY-MM-DD
     */
    private String bookDate;
    /**
     * 预约时段 HH24:MI
     */
    private String bookTime;
    /**
     * 关联的可预约时间仓库ID
     */
    private Integer bookTimeId;
    /**
     * 预约状态，1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
     */
    private String bookStatus;
    /**
     * 记录创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;
    /**
     * 是否已短信通知，0：否，1：是
     */
    private String smsNotifiedStatus;
    /**
     * 短信内容
     */
    private String smsContent;
    /**
     * 通知时间
     */
    private Date notifiedTime;
    /**
     * 是否删除，0：否，默认为0，1：已删除
     */
    private String deleteFlag;
    
}
