package com.xyzq.kid.logic.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.po.Book;
/**
 * 飞行预约服务
 * @author keyanggui
 *
 */
@Service("bookService")
public class BookService {
	
	@Autowired
	BookMapper bookMapper;
	
	public List<Book> getAllBooks(){
		return bookMapper.selectAll();
	}
}
