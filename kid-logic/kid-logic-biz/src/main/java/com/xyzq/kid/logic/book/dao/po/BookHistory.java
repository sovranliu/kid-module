package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class BookHistory {
    private Integer id;

    private Integer bookid;

    private String booktimeid;

    private String bookdate;

    private String booktime;

    private Date createtime;

    private String deleteflag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String getBooktimeid() {
        return booktimeid;
    }

    public void setBooktimeid(String booktimeid) {
        this.booktimeid = booktimeid == null ? null : booktimeid.trim();
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate == null ? null : bookdate.trim();
    }

    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime == null ? null : booktime.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag == null ? null : deleteflag.trim();
    }
}