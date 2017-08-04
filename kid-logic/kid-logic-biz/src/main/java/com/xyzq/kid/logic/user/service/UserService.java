package com.xyzq.kid.logic.user.service;

import com.xyzq.kid.logic.user.bean.UserBean;
import com.xyzq.kid.logic.user.entity.SessionEntity;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务
 */
@Service("userService")
public class UserService {
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
        cache.set("sid-" + entity.sId, mobileNoOpenId, 1000 * 60 * 24 * 30);
    }

    /**
     * 方法描述
     *
     * @return 返回值
     */
    public UserEntity getUserById(int id) {
        return userBean.selectByPrimaryKey(id);
    }

    /**
     * 根据手机号获取用户信息
     * @param mobileNo
     * @return
     */
    public UserEntity selectByMolieNo(String mobileNo) {
        return userBean.selectByMolieNo(mobileNo);
    }

    /**
     * 用户注册
     * @param entity
     * @return
     */
    public int insertSelective(UserEntity entity) {
        return userBean.insertSelective(entity);
    }

    /**
     * 用户更新
     * @param entity
     * @return
     */
    public int updateByMobileNo(UserEntity entity) {
        return userBean.updateByMobileNo(entity);
    }

    /**
     * 根据openId查询用户信息
     * @param openId
     * @return UserEntity
     */
    public UserEntity selectByOpenId(String openId) {
        return userBean.selectByOpenId(openId);
    }
}
