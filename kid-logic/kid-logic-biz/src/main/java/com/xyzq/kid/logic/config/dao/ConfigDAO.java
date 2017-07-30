package com.xyzq.kid.logic.config.dao;

import com.xyzq.kid.logic.config.dao.po.ConfigPO;

import java.util.List;

public interface ConfigDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigPO record);

    int insertSelective(ConfigPO record);

    ConfigPO selectByPrimaryKey(Integer id);

    ConfigPO selectByName(String name);

    List<ConfigPO> selectByNameArr(String[] nameArr);

    int updateByPrimaryKeySelective(ConfigPO record);

    int updateByPrimaryKey(ConfigPO record);
}