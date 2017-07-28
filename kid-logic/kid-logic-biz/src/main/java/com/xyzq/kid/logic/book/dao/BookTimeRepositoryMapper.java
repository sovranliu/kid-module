package com.xyzq.kid.logic.book.dao;

import java.util.List;

import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;

public interface BookTimeRepositoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookTimeRepository record);

    int insertSelective(BookTimeRepository record);

    BookTimeRepository selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookTimeRepository record);

    int updateByPrimaryKey(BookTimeRepository record);
    
    List<BookTimeRepository> selectByBookDate(String bookDate);
    
    BookTimeRepository selectByBookDateAndTimeSpan(String bookDate,Integer timeSpanId);
    
}