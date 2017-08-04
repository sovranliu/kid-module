package com.xyzq.kid.logic.admin.dao;

import com.xyzq.kid.logic.admin.dao.po.AdminPO;


/**
 * CNS_Admin表数据访问接口
 */
public interface AdminDAO {
    /**
     * 加载指定ID的
     */
    AdminPO load(Integer id);

    /**
     * 根据userName查询管理用户
     * @Param userName
     * @return AdminPO
     */
    AdminPO findByUserName(String userName);

    /**
     * 新增管理用户
     * @Param userName
     * @return AdminPO
     */
    void addAdmin(AdminPO adminPO);
    /**
     * 更新管理用户
     * @Param userName
     * @return AdminPO
     */
    void updateAdmin(AdminPO adminPO);

}
