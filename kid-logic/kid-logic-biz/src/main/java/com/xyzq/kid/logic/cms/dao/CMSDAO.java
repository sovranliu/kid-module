package com.xyzq.kid.logic.cms.dao;

import com.xyzq.kid.logic.cms.dao.po.CMSPO;

import java.util.List;
import java.util.Map;

public interface CMSDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(CMSPO record);

    int insertSelective(CMSPO record);

    CMSPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CMSPO record);

    int updateByPrimaryKey(CMSPO record);

    List<CMSPO> queryCMSByCond(Map paramMap);

    int queryCMSByCondCount(Map paramMap);
}