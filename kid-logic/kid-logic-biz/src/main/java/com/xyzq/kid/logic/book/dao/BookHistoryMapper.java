package com.xyzq.kid.logic.book.dao;

import com.xyzq.kid.logic.book.dao.po.BookHistory;

public interface BookHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookHistory record);

    int insertSelective(BookHistory record);

    BookHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookHistory record);

    int updateByPrimaryKey(BookHistory record);
}