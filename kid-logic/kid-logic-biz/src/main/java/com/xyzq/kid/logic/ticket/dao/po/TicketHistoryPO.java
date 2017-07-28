package com.xyzq.kid.logic.ticket.dao.po;

import java.util.Date;

public class TicketHistoryPO {
    private Integer id;

    private Integer ticketid;

    private Boolean action;

    private Date prevalidperiod;

    private String premobile;

    private Byte deleted;

    private Date createtime;

    private Date updatetime;

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

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Date getPrevalidperiod() {
        return prevalidperiod;
    }

    public void setPrevalidperiod(Date prevalidperiod) {
        this.prevalidperiod = prevalidperiod;
    }

    public String getPremobile() {
        return premobile;
    }

    public void setPremobile(String premobile) {
        this.premobile = premobile == null ? null : premobile.trim();
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}