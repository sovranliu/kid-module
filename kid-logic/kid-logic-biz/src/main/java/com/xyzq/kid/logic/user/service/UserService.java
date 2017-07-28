package com.xyzq.kid.logic.user.service;

import com.xyzq.kid.logic.user.bean.DemoBean;
import com.xyzq.kid.logic.user.bean.UserBean;
import com.xyzq.kid.logic.user.entity.DemoEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 */
@Service("userService")
public class UserService {
    /**
     * 用户信息
     */
    @Autowired
    private UserBean userBean;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public UserEntity getUserById() {
        return userBean.selectByPrimaryKey(1);
    }


}
