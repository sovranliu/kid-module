package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class BookTimeRepository {
    private Integer id;

    private String bookdate;

    private Integer booktimespanid;

    private Date createtime;

    private String createperson;

    private Date lastupdatetime;

    private Integer bookamount;

    private Integer booktotal;

    private String deleteflag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate == null ? null : bookdate.trim();
    }

    public Integer getBooktimespanid() {
        return booktimespanid;
    }

    public void setBooktimespanid(Integer booktimespanid) {
        this.booktimespanid = booktimespanid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateperson() {
        return createperson;
    }

    public void setCreateperson(String createperson) {
        this.createperson = createperson == null ? null : createperson.trim();
    }

    public Date getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public Integer getBookamount() {
        return bookamount;
    }

    public void setBookamount(Integer bookamount) {
        this.bookamount = bookamount;
    }

    public Integer getBooktotal() {
        return booktotal;
    }

    public void setBooktotal(Integer booktotal) {
        this.booktotal = booktotal;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag == null ? null : deleteflag.trim();
    }
}