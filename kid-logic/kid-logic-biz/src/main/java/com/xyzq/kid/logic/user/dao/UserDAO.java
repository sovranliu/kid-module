package com.xyzq.kid.logic.user.dao;

import com.xyzq.kid.logic.user.dao.po.UserPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    UserPO selectByMolieNo(String mobileno);

    int updateByMobileNo(UserPO record);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);

    UserPO selectByOpenId(@Param("openId")String openId);

    List<UserPO> queryUserByCond(Map paramMap);

    int queryUserByCondCount(Map paramMap);

    int readPostBenefit(String mobileno);

    int updateMobileNo(Map paramMap);
}