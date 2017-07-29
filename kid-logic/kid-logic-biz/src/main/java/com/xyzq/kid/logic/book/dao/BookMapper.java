package com.xyzq.kid.logic.book.dao;

import java.util.List;
import java.util.Map;

import com.xyzq.kid.logic.book.dao.po.Book;

public interface BookMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Book record);

    int insertSelective(Book record);

    Book selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);
    
    List<Book> queryBookByCond(Map params);
    
    List<Book> selectAll();
}