package com.xyzq.kid.logic.user.service;

import com.xyzq.kid.logic.user.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by XYZQ on 2017/7/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:config/spring/applicationContext-dao.xml"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void getUserById() throws Exception {
        UserEntity userEntity = userService.getUserById(2);
        System.out.println(userEntity.toString());
    }

    @Test
    public void selectByMolieNo() throws Exception {
        UserEntity userEntity = userService.selectByMolieNo("123");
        System.out.println(userEntity.toString());
    }

    @Test
    public void insertSelective() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.mobileno = "123";
        userEntity.openid = "myj_test";
        userEntity.realname = "myj_test";
        userEntity.address = "abcdefghijklmn";
        userEntity.gender = UserEntity.USER_MALE;
        userEntity.subscribetime= "2017-07-31 11:00:00";
        int id = userService.insertSelective(userEntity);
        System.out.println(id);
    }

    @Test
    public void updateUserInfo() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.mobileno = "123";
        userEntity.openid = "myj_test_new";
        userEntity.realname = "myj_test_new";
        userEntity.address = "abcdefghijklmn_new";
        userEntity.gender = UserEntity.USER_FEMALE;
        userEntity.subscribetime= "2017-08-31 11:00:00";
        int id = userService.updateByMobileNo(userEntity);
        System.out.println(id);
    }

}