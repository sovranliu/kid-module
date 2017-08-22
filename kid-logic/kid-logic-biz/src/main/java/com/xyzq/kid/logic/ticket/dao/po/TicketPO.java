package com.xyzq.kid.logic.ticket.dao.po;

import java.math.BigDecimal;
import java.util.Date;

public class TicketPO {
    private Integer id;

    private String serialno;

    private Integer type;

    private String owneropenid;

    private String payeropenid;

    private BigDecimal price;

    private BigDecimal refundprice;

    private Date expiredate;

    private Boolean insurance;

    private String orderno;

    private Integer status;

    private Byte deleted;

    private Date createtime;

    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno == null ? null : serialno.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOwneropenid() {
        return owneropenid;
    }

    public void setOwneropenid(String owneropenid) {
        this.owneropenid = owneropenid;
    }

    public String getPayeropenid() {
        return payeropenid;
    }

    public void setPayeropenid(String payeropenid) {
        this.payeropenid = payeropenid == null ? null : payeropenid.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRefundprice() {
        return refundprice;
    }

    public void setRefundprice(BigDecimal refundprice) {
        this.refundprice = refundprice;
    }

    public Date getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(Date expiredate) {
        this.expiredate = expiredate;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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