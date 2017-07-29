package com.xyzq.kid.logic.book.entity;

import java.util.Date;

public class BookChangeRequestEntity {
	
    private Integer id;

    private Integer bookid;
    
    /**
     * 1:改期，2：撤销
     */
    private String reqesttype;
    
    /**
     * 申请缘由
     */
    private String requestreason;
    
    private Integer userid;

    private Date createtime;

    private Date lastupdatetime;
    
    /**
     * 申请状态 1：申请中，2：申请通过，3：拒绝申请
     */
    private String status;
    
    /**
     * 审批人
     */
    private String approval;
    
    /**
     * 审批时间
     */
    private Date approvetime;
    /**
     * 审批备注
     */
    private String approvecomments;
    
    /**
     * 删除标记，0：未删除，1：已删除，默认为0
     */
    private String deleteflag;
    
    /**
     * 关联改期选中的新预约时间
     */
    private Integer booktimeid;

}