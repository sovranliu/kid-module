package com.xyzq.kid.logic.admin.entity;

import com.xyzq.kid.logic.admin.dao.po.AdminPO;

import java.sql.Timestamp;

/**
 * Created by Brann on 17/7/27.
 */
public class AdminEntity {
	public final static String DELETED = "1";//已删除
	public final static String UNDELETED = "0";//未删除
	/**
	 * 主键ID，自增长的流水号
	 */
	public Integer id;
	/**
	 * 登录用户名
	 */
	public String userName;
	/**
	 * 飞行日志文件名称
	 */
	public String password;
	/**
	 * 是否被购买，0:未购买，1:已购买
	 */
	public String email;
	/**
	 * 记录是否被软删，1:删除，0:未删除
	 */
	public String mobile;
	/**
	 * 记录是否被软删，1:删除，0:未删除
	 */
	public String deleted;

	/**
	 * 创建时间
	 */
	public Timestamp createTime;

	/**
	 * 更新时间
	 */
	public Timestamp updateTime;

	public AdminEntity(Integer id,String userName, String password, String email, String mobile) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.mobile = mobile;
		this.deleted = UNDELETED;
	}

	public AdminEntity(AdminPO adminPO) {
		if (adminPO != null) {
			id = adminPO.getId();
			userName = adminPO.getUserName();
			password = adminPO.getPassword();
			email = adminPO.getEmail();
			mobile = adminPO.getMobile();
			deleted = adminPO.getDeleted();
			createTime = adminPO.getCreateTime();
			updateTime = adminPO.getUpdateTime();
		}
	}
}
