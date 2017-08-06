package com.xyzq.kid.logic.user.service;

import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.user.bean.UserBean;
import com.xyzq.kid.logic.user.entity.SessionEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 */
@Service("userService")
public class UserService {
    public static Logger logger = LoggerFactory.getLogger(UserService.class);
    /**
     * 缓存访问对象
     *
     * 缓存中内容为：mobileNo,openId
     */
    @Resource(name = "cache")
    protected ITimeLimitedCache<String, String> cache;
    /**
     * 用户信息
     */
    @Autowired
    private UserBean userBean;


    /**
     * 根据会话ID获取会话实体
     *
     * @param sId 会话ID
     * @return 会话实体
     */
    public SessionEntity fetchSession(String sId) {
        String mobileNoOpenId = cache.get("sid-" + sId);
        if(null == mobileNoOpenId) {
            return null;
        }
        return new SessionEntity(sId, mobileNoOpenId);
    }

    /**
     * 保存会话实体
     *
     * @param entity 会话实体
     */
    public void saveSession(SessionEntity entity) {
        String mobileNoOpenId = entity.mobileNo + "," + entity.openId;
        if(null == entity.sId) {
            return;
        }
        cache.set("sid-" + entity.sId, mobileNoOpenId, 1000 * 60 * 24 * 30);
    }

    /**
     * 用户信息分页查询
     * @param userName 用户姓名
     * @param telephone 手机号
     * @param begin 起始页
     * @param limit 条数
     * @return 符合要求的条数
     */
    public Page<UserEntity> getUserList(String userName, String telephone, int begin, int limit) {
        logger.info("UserService.getUserList[in]-userName:" + userName + ",telephone:"+ telephone + ",begin:" + begin + ",limit:" + limit);
        Map paramMap = new HashMap<>();
        if(null != userName && userName.length() > 0) {
            paramMap.put("realname", userName);
        }
        if(null != telephone && telephone.length() > 0) {
            paramMap.put("mobileno", telephone);
        }
        paramMap.put("begin", (begin - 1) * limit);
        paramMap.put("limit", limit);
        // TODO:梅叶俊
        // return userBean.queryUserByCond(paramMap);
        return null;
    }

    /**
     * 方法描述
     *
     * @return 返回值
     */
    public UserEntity getUserById(int id) {
        logger.info("UserService.getUserById[in]-id:" + id);
        return userBean.selectByPrimaryKey(id);
    }

    /**
     * 根据手机号获取用户信息
     * @param mobileNo
     * @return
     */
    public UserEntity selectByMolieNo(String mobileNo) {
        logger.info("UserService.getUserById[in]-mobileNo:" + mobileNo);
        return userBean.selectByMolieNo(mobileNo);
    }

    /**
     * 用户注册
     * @param entity
     * @return
     */
    public int insertSelective(UserEntity entity) {
        logger.info("UserService.getUserById[in]-:" + entity.toString());
        return userBean.insertSelective(entity);
    }

    /**
     * 用户更新
     * @param entity
     * @return
     */
    public int updateByMobileNo(UserEntity entity) {
        logger.info("UserService.updateByMobileNo[in]-:" + entity.toString());
        return userBean.updateByMobileNo(entity);
    }

    /**
     * 根据openId查询用户信息
     * @param openId
     * @return UserEntity
     */
    public UserEntity selectByOpenId(String openId) {
        logger.info("UserService.selectByOpenId[in]-openId:" + openId);
        return userBean.selectByOpenId(openId);
    }
}
