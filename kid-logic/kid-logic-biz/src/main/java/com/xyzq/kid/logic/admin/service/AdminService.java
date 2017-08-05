package com.xyzq.kid.logic.admin.service;

import com.xyzq.kid.logic.admin.bean.AdminBean;
import com.xyzq.kid.logic.admin.dao.po.AdminPO;
import com.xyzq.kid.logic.admin.entity.AdminEntity;
import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 管理员服务
 */
@Service("adminService")
public class AdminService {
	/**
	 * 日志组件
	 */
	protected static Logger logger = LoggerFactory.getLogger(AdminService.class);
	/**
	 * 缓存访问对象
	 *
	 * 缓存中内容为：ID
	 */
	@Resource(name = "cache")
	protected ITimeLimitedCache<String, Integer> cache;
	/**
	 * 范例组件
	 */
	@Autowired
	private AdminBean adminBean;


	/**
	 * 根据会话ID获取管理员ID
	 *
	 * @param aId 会话ID
	 * @return 管理员ID
	 */
	public Integer fetchAdminId(String aId) {
		return cache.get("aid-" + aId);
	}

	/**
	 * 保存会话
	 *
	 * @param adminId 管理员ID
	 * @return 会话ID
	 */
	public String saveSession(int adminId) {
		String aId = Serial.makeLocalID();
		cache.set("aid-" + aId, adminId, 1000 * 60 * 24);
		return aId;
	}

	/**
	 * 根据ID加载AdminEntity
	 *
	 * @return 返回值AdminEntity
	 * @Param id
	 */
	public AdminEntity load(Integer id) {
		return adminBean.load(id);
	}

	/**
	 * 根据飞行票ID和购买状态字段查询record
	 *
	 * @Param userName
	 * @return AdminEntity
	 */
	public AdminEntity findByUserName(String userName) {
		return adminBean.findByUserName(userName);
	}

	/**
	 * 新增管理用户
	 *
	 * @Param adminEntity
	 */
	public void addAdmin(AdminEntity adminEntity) {
		AdminPO adminPO = new AdminPO(adminEntity);
		adminBean.addAdmin(adminPO);
	}

	/**
	 * 更新管理用户
	 *
	 * @Param adminEntity
	 */
	public void updateAdmin(AdminEntity adminEntity) {
		AdminPO adminPO = new AdminPO(adminEntity);
		adminBean.updateAdmin(adminPO);
	}

	/**
	 * 删除管理用户
	 *
	 * @Param id
	 */
	public void deleteAdmin(Integer id) {
		adminBean.deleteAdmin(id);
	}
}
