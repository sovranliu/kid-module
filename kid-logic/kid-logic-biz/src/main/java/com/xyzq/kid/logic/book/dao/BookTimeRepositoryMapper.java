package com.xyzq.kid.logic.book.dao;

import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;

public interface BookTimeRepositoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookTimeRepository record);

    int insertSelective(BookTimeRepository record);

    BookTimeRepository selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookTimeRepository record);

    int updateByPrimaryKey(BookTimeRepository record);
    
    List<BookTimeRepository> queryByCond(Map<String,Object> map);
}