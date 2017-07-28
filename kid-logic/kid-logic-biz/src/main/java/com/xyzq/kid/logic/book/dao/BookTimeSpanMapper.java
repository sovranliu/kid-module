package com.xyzq.kid.logic.book.dao;

import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;

public interface BookTimeSpanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookTimeSpan record);

    int insertSelective(BookTimeSpan record);

    BookTimeSpan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookTimeSpan record);

    int updateByPrimaryKey(BookTimeSpan record);
}