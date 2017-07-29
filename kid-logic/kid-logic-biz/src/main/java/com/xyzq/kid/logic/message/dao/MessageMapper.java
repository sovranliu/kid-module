package com.xyzq.kid.logic.message.dao;

import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.message.dao.po.Message;

public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
    
    List<Message> selectBySelectiveKey(Message message);
    
    List<Message> queryByCond(Map params);
}