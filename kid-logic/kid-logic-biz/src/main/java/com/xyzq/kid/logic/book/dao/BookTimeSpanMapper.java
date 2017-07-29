package com.xyzq.kid.logic.book.dao;

import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;

public interface BookTimeSpanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookTimeSpan record);

    int insertSelective(BookTimeSpan record);

    BookTimeSpan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookTimeSpan record);

    int updateByPrimaryKey(BookTimeSpan record);
    
    List<BookTimeSpan> queryByCond(Map<String,Object> map);
}