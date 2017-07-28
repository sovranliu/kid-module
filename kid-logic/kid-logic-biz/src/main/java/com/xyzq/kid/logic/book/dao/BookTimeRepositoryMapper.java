package com.xyzq.kid.logic.book.dao;

import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;

public interface BookTimeRepositoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookTimeRepository record);

    int insertSelective(BookTimeRepository record);

    BookTimeRepository selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookTimeRepository record);

    int updateByPrimaryKey(BookTimeRepository record);
}