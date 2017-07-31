package com.xyzq.kid.logic.dateUnviable.dao.po;

import java.sql.Timestamp;

/**
 * Table  LGC_Book_DateUnviable
 * 不可预约日期记录表
 * Created by Brann on 17/7/27.
 */
public class DateUnviablePO {
	/**
	 * 主键ID，自增长的流水号
	 */
	private Integer id;

	/**
	 * 日期数据格式是String
	 * yyyy-MM-dd
	 */
	private String UnviableDate;

	/**
	 * 记录是否被软删，1:删除，0:未删除
	 */
	private String deleted;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 更新时间
	 */
	private Timestamp updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnviableDate() {
		return UnviableDate;
	}

	public void setUnviableDate(String unviableDate) {
		UnviableDate = unviableDate;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
