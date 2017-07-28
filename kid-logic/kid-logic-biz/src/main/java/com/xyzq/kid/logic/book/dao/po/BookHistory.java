package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class BookHistory {
    private Integer id;

    private Integer bookId;

    private String bookTimeId;

    private String bookDate;

    private String bookTime;

    private Date createTime;

    private String deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookTimeId() {
        return bookTimeId;
    }

    public void setBookTimeId(String bookTimeId) {
        this.bookTimeId = bookTimeId == null ? null : bookTimeId.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}