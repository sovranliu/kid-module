package com.xyzq.kid.logic.user.dao;

import com.xyzq.kid.logic.user.dao.po.UserPO;

public interface UserDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);
}