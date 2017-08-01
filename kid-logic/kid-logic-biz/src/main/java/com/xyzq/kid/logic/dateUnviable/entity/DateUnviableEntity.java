package com.xyzq.kid.logic.dateUnviable.entity;

import com.xyzq.kid.logic.dateUnviable.dao.po.DateUnviablePO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class DateUnviableEntity {

	public final static String PURCHASED = "1";//已购买
	public final static String UNPURCHASED = "0";//未购买
	/**
	 * 主键ID，自增长的流水号
	 */
	private Integer id;

	/**
	 * 日期数据格式是String
	 * yyyy-MM-dd
	 */
	private String unviableDate;

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


	public DateUnviableEntity(DateUnviablePO dateUnviablePO) {
		if (dateUnviablePO != null) {
			id = dateUnviablePO.getId();
			deleted = dateUnviablePO.getDeleted();
			unviableDate = dateUnviablePO.getUnviableDate();
			createTime = dateUnviablePO.getCreateTime();
			updateTime = dateUnviablePO.getUpdateTime();
		}
	}
}
