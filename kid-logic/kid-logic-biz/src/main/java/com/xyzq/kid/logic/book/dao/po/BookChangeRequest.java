package com.xyzq.kid.logic.book.dao.po;

import java.util.Date;

public class BookChangeRequest {
    private Integer id;

    private Integer bookid;

    private String reqesttype;

    private String requestreason;

    private Integer userid;

    private Date createtime;

    private Date lastupdatetime;

    private String status;

    private String approval;

    private Date approvetime;

    private String approvecomments;

    private String deleteflag;

    private Integer booktimeid;

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

    public String getReqesttype() {
        return reqesttype;
    }

    public void setReqesttype(String reqesttype) {
        this.reqesttype = reqesttype == null ? null : reqesttype.trim();
    }

    public String getRequestreason() {
        return requestreason;
    }

    public void setRequestreason(String requestreason) {
        this.requestreason = requestreason == null ? null : requestreason.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval == null ? null : approval.trim();
    }

    public Date getApprovetime() {
        return approvetime;
    }

    public void setApprovetime(Date approvetime) {
        this.approvetime = approvetime;
    }

    public String getApprovecomments() {
        return approvecomments;
    }

    public void setApprovecomments(String approvecomments) {
        this.approvecomments = approvecomments == null ? null : approvecomments.trim();
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag == null ? null : deleteflag.trim();
    }

    public Integer getBooktimeid() {
        return booktimeid;
    }

    public void setBooktimeid(Integer booktimeid) {
        this.booktimeid = booktimeid;
    }
}