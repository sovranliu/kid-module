package com.xyzq.kid.logic.user.service;

import com.xyzq.kid.common.wechat.mp.UserInfoHelper;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务
 */
@Service("userService")
public class UserService {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(UserService.class);
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
        logger.info("save session " + entity.sId + " = " + mobileNoOpenId);
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
        paramMap.put("begin", begin);
        paramMap.put("limit", limit);
        return userBean.queryUserByCond(paramMap);
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

    public int readPostBenefit(String mobileno) {
        logger.info("UserService.readPostBenefit[in]-mobileno:" + mobileno);
        return userBean.readPostBenefit(mobileno);
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
     * 注册帐号
     *
     * @param openId 微信用户开放ID
     * @param mobileNo 用户手机号码
     * @param name 用户姓名
     * @return 执行结果
     */
    public boolean register(String openId, String mobileNo, String name) {
        logger.info("user register, openId = " + openId + ", mobileNo = " + mobileNo + ", name = " + name);
        UserEntity entity = new UserEntity();
        entity.telephone = mobileNo;
        entity.userName = name;
        entity.openid = openId;
        try {
            UserInfoHelper.GuestInfo guestInfo = UserInfoHelper.fetchUserInfo(openId);
            if(null != guestInfo && guestInfo instanceof UserInfoHelper.MemberInfo) {
                entity.avatarUrl = ((UserInfoHelper.MemberInfo) guestInfo).headImgUrl;
            }
        }
        catch (Exception e) {
            logger.error("load avatar failed", e);
        }
        userBean.insertSelective(entity);
        return true;
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

    public int insertSelective(UserEntity entity) {
        return userBean.insertSelective(entity);
    }

    public int updateMobileNo(String mobile, String mobilePre) {
        return userBean.updateMobileNo(mobile, mobilePre);
    }
}
