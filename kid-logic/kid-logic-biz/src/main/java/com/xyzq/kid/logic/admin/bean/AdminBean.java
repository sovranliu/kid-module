package com.xyzq.kid.logic.admin.bean;

/**
 * Created by Brann on 17/7/27.
 */

import com.xyzq.kid.logic.admin.dao.AdminDAO;
import com.xyzq.kid.logic.admin.dao.po.AdminPO;
import com.xyzq.kid.logic.admin.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class AdminBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private AdminDAO adminDAO;


    /**
     *  根据ID加载entity
     *
     * @return 返回值
     */
    public AdminEntity load(Integer id) {
        AdminPO adminPO = adminDAO.load(id);
        AdminEntity entity = new AdminEntity(adminPO);
        return entity;
    }
    /**
     * 根据userName查询AdminEntity
     * @Param userName
     * @return AdminEntity
     */
    public AdminEntity findByUserName(String userName){
        AdminPO adminPO = adminDAO.findByUserName(userName);
        AdminEntity entity = new AdminEntity(adminPO);
        return entity;
    }

}
