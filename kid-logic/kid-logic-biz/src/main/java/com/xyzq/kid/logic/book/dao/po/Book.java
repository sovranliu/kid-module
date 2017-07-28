package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class Book {
    private Integer id;

    private Integer ticketid;

    private String bookstatus;

    private String bookdate;

    private String booktime;

    private Integer userid;

    private Date createtime;

    private Date lastupdatetime;

    private String smsnotified;

    private String smscontent;

    private Integer booktimeid;

    private String deleteflag;

    private Date notifiedtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketid() {
        return ticketid;
    }

    public void setTicketid(Integer ticketid) {
        this.ticketid = ticketid;
    }

    public String getBookstatus() {
        return bookstatus;
    }

    public void setBookstatus(String bookstatus) {
        this.bookstatus = bookstatus == null ? null : bookstatus.trim();
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getSmsnotified() {
        return smsnotified;
    }

    public void setSmsnotified(String smsnotified) {
        this.smsnotified = smsnotified == null ? null : smsnotified.trim();
    }

    public String getSmscontent() {
        return smscontent;
    }

    public void setSmscontent(String smscontent) {
        this.smscontent = smscontent == null ? null : smscontent.trim();
    }

    public Integer getBooktimeid() {
        return booktimeid;
    }

    public void setBooktimeid(Integer booktimeid) {
        this.booktimeid = booktimeid;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag == null ? null : deleteflag.trim();
    }

    public Date getNotifiedtime() {
        return notifiedtime;
    }

    public void setNotifiedtime(Date notifiedtime) {
        this.notifiedtime = notifiedtime;
    }
}