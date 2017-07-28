package com.xyzq.kid.logic.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.po.Book;

@Service("bookService")
public class BookService {
	
	@Autowired
	BookMapper bookDAO;
	
	public List<Book> getAllBooks(){
		return bookDAO.selectAll();
	}
}
