package com.xyzq.kid.logic.book.dao;

import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.book.dao.po.BookChangeRequest;

public interface BookChangeRequestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookChangeRequest record);

    int insertSelective(BookChangeRequest record);

    BookChangeRequest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookChangeRequest record);

    int updateByPrimaryKey(BookChangeRequest record);
    
    List<BookChangeRequest> queryRequestByCond(Map<String,Object> map);
}