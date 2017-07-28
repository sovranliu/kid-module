package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class Book {
    private Integer id;

    private Integer ticketId;

    private String bookStatus;

    private String bookDate;

    private String bookTime;

    private Integer userId;

    private Date createTime;

    private Date lastUpdateTime;

    private String smsNotified;

    private String smsContent;

    private Integer bookTimeId;

    private String deleteFlag;

    private Date notifiedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus == null ? null : bookStatus.trim();
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate == null ? null : bookDate.trim();
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime == null ? null : bookTime.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getSmsNotified() {
        return smsNotified;
    }

    public void setSmsNotified(String smsNotified) {
        this.smsNotified = smsNotified == null ? null : smsNotified.trim();
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent == null ? null : smsContent.trim();
    }

    public Integer getBookTimeId() {
        return bookTimeId;
    }

    public void setBookTimeId(Integer bookTimeId) {
        this.bookTimeId = bookTimeId;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    public Date getNotifiedTime() {
        return notifiedTime;
    }

    public void setNotifiedTime(Date notifiedTime) {
        this.notifiedTime = notifiedTime;
    }
}