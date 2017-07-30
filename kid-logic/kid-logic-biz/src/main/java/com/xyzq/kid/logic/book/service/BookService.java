package com.xyzq.kid.logic.book.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.BookTimeRepositoryMapper;
import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;
import com.xyzq.kid.logic.ticket.dao.TicketDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketPO;
/**
 * 飞行预约服务
 * @author keyanggui
 *
 */
@Service("bookService")
public class BookService {
	
	@Autowired
	BookMapper bookMapper;
	
	@Autowired
	TicketDAO ticketDao;
	
	@Autowired
	BookTimeRepositoryMapper bookTimeRepositoryMapper;
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	BookTimeSpanMapper bookTimeSpanMapper;
	
	@Autowired
	BookChangeRequestService bookChangeRequestService;
	
	public List<Book> getAllBooks(){
		return bookMapper.selectAll();
	}
	
	
	/**
	 * 检查指定票在指定时间是否可以预约
	 * @param ticketId
	 * @param bookTimeId
	 * @return
	 */
	public boolean checkBook(Integer ticketId,Integer bookTimeId){
		boolean flag=false;
		try{
			TicketPO ticket=ticketDao.selectByPrimaryKey(ticketId);
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
			if(ticket!=null&&repo!=null){
				//检查预约当天飞行票是否过期
				Calendar expirTime=Calendar.getInstance();
				expirTime.setTime(ticket.getExpiredate());
				Calendar bookTime=Calendar.getInstance();
				String[] dates=repo.getBookdate().split("-");//预约日期格式 为YYYY-MM-DD
				bookTime.set(Calendar.YEAR, Integer.valueOf(dates[0]));
				bookTime.set(Calendar.MONTH, Integer.valueOf(dates[1])-1);
				bookTime.set(Calendar.DATE, Integer.valueOf(dates[2]));
				if(bookTime.before(expirTime)){
					//在有效期内，则检查所选时间段是否有库存
					if(bookRepositoryService.checkAmount(bookTimeId)){
						//有库存则可预约
						flag=true;
					}
				}
			}
		}catch(Exception e){
			System.out.println("check book fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 根据飞行票ID查询预约记录
	 * @param ticketId
	 * @return
	 */
	public Book queryBookRecByTicketId(Integer ticketId){
		Book bookRec=null;
		try{
			Map<String,Object> map=new HashMap<>();
			if(ticketId!=null){
				map.put("ticketId", ticketId);
			}
			if(bookMapper.queryBookByCond(map)!=null){
				bookRec=bookMapper.queryBookByCond(map).get(0);
			}
		}catch(Exception e){
			System.out.println("query book record by ticket id fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return bookRec;
	}
	
	public List<Book> queryBookRecByUserId(Integer userId){
		List<Book> bookList=null;
		try{
			Map<String,Object> map=new HashMap<>();
			map.put("usdrId", userId);
			bookList=bookMapper.queryBookByCond(map);
		}catch(Exception e){
			System.out.println("query book record by user id fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return bookList;
	}
	
	/**
	 * 创建预约单
	 * @param ticketId
	 * @param bookTimeId
	 * @param userId
	 * @return
	 */
	public boolean createBook(Integer ticketId,Integer bookTimeId,Integer userId){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
			if(checkBook(ticketId, bookTimeId)){
				Book book=new Book();
				book.setTicketid(ticketId);
				book.setUserid(userId);
				book.setBooktimeid(bookTimeId);
				book.setBookstatus("1");//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销
				book.setBookdate(repo.getBookdate());
				BookTimeSpan span=bookTimeSpanMapper.selectByPrimaryKey(repo.getBooktimespanid());
				book.setBooktime(span.getTimespan());
				book.setCreatetime(new Date());
				book.setLastupdatetime(new Date());
				book.setDeleteflag("0");
				book.setSmsnotified("0");
				bookMapper.updateByPrimaryKeySelective(book);
				//预约成功则扣减库存
				if(bookRepositoryService.updateAmount(bookTimeId, "1")){
					flag=true;
				}
			}
		}catch(Exception e){
			System.out.println("create book fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 改期、撤销、核销预约
	 * @param bookId
	 * @param status 1:改期，2：撤销，3：核销
	 * @param reason 变更原由
	 * @param newBookTimeId 改期选中的新时间Id
	 * @return
	 */
	public boolean updateBookStatus(Integer bookId,String status,String reason,Integer newBookTimeId){
		boolean flag=false;
		try{
			Book book=bookMapper.selectByPrimaryKey(bookId);
			if(status.equals("1")){//改期
				if(bookChangeRequestService.createRequest(bookId, status, reason, book.getUserid(), newBookTimeId)){
					flag=true;
				}
			}else if(status.equals("2")){//撤销
				if(bookChangeRequestService.createRequest(bookId, status, reason, book.getUserid(), null)){
					flag=true;
				}
			}else if(status.equals("3")){
				book.setBookstatus("5");//核销预约
				book.setLastupdatetime(new Date());
				bookMapper.updateByPrimaryKeySelective(book);
				flag=true;
			}
		}catch(Exception e){
			System.out.println("update book's status,complete or cancel,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 短信通知
	 * @param bookId
	 * @return
	 */
	public boolean notifyUser(Integer bookId){
		boolean flag=false;
		return flag;
	}
}
