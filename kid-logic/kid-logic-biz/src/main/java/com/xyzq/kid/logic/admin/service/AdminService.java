package com.xyzq.kid.logic.admin.service;

import com.xyzq.kid.logic.admin.bean.AdminBean;
import com.xyzq.kid.logic.admin.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by Brann on 17/7/27.
 */
@Service("adminService")
public class AdminService {
    /**
     * 范例组件
     */
    @Autowired
    private AdminBean adminBean;


    private static Logger log = Logger.getLogger(AdminService.class.getName());


    /**
     * 根据ID加载AdminEntity
     * @Param  id
     * @return 返回值AdminEntity
     */
    public AdminEntity load(Integer id) {
        return adminBean.load(id);
    }


    /**
     * 根据飞行票ID和购买状态字段查询record
     *
     * @return RecordEntity
     * @Param ticketID
     * @Param purchased
     */
    public AdminEntity findByUserName(String userName) {
        return adminBean.findByUserName(userName);
    }


}
