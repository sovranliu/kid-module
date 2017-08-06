package com.xyzq.kid.logic.book.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.BookTimeRepositoryMapper;
import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;
import com.xyzq.kid.logic.ticket.dao.TicketDAO;
import com.xyzq.kid.logic.ticket.dao.po.TicketPO;
import com.xyzq.kid.logic.ticket.dao.po.TicketRefundPO;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import com.xyzq.kid.logic.user.entity.UserEntity;
import com.xyzq.kid.logic.user.service.UserService;
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
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	UserService userService;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
	
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
				expirTime.add(Calendar.HOUR_OF_DAY, 23);
				expirTime.add(Calendar.MINUTE, 59);
				expirTime.add(Calendar.SECOND, 59);
				Calendar bookTime=Calendar.getInstance();
//				String[] dates=repo.getBookdate().split("-");//预约日期格式 为YYYY-MM-DD
				Date bookDate=sdf.parse(repo.getBookdate()+" 00:00:00");
				bookTime.setTime(bookDate);
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
			if(bookMapper.queryBookByCond(map)!=null&&bookMapper.queryBookByCond(map).size()>0){
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
	 * 创建预约单(检验)
	 * @param ticketId
	 * @param bookTimeId
	 * @param userId
	 * @return
	 */
	public boolean createBook(String serialNum,Integer bookTimeId,Integer userId){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
			TicketEntity ticket=ticketService.getTicketsInfoBySerialno(serialNum);
			if(ticket!=null&&checkBook(ticket.id, bookTimeId)){
				Book book=new Book();
				book.setTicketid(ticket.id);
				book.setUserid(userId);
				book.setBooktimeid(bookTimeId);
				book.setBookstatus("1");//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
				book.setBookdate(repo.getBookdate());
				BookTimeSpan span=bookTimeSpanMapper.selectByPrimaryKey(repo.getBooktimespanid());
				book.setBooktime(span.getTimespan());
				book.setCreatetime(new Date());
				book.setLastupdatetime(new Date());
				book.setDeleteflag("0");
				book.setSmsnotified("0");
				bookMapper.insertSelective(book);
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
	 * 创建预约单(强制预约)
	 * @param ticketId
	 * @param bookTimeId
	 * @param userId
	 * @return
	 */
	public boolean createBookNoCheck(String serialNum,Integer bookTimeId,Integer userId){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
			TicketEntity ticket=ticketService.getTicketsInfoBySerialno(serialNum);
			Book book=new Book();
			book.setTicketid(ticket.id);
			book.setUserid(userId);
			book.setBooktimeid(bookTimeId);
			book.setBookstatus("1");//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
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
				if(bookChangeRequestService.createRequest(bookId, status, reason, book.getUserid(), newBookTimeId,"1")){
					flag=true;
				}
			}else if(status.equals("2")){//撤销
				if(bookChangeRequestService.createRequest(bookId, status, reason, book.getUserid(), null,"1")){
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
		
	
	/**
	 * 按条件查询
	 * @param mobileNo
	 * @param ticketSerialNo
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	public List<Book> queryByCondLimt(String mobileNo,String ticketSerialNo,String status,String startDate,String endDate,Integer currentPage,Integer limit){
		List<Book> bookList=null;
		try{
			Date start=null;
			if(!StringUtils.isNullOrEmpty(startDate)){
				start=sdf2.parse(startDate);
			}
			Date end=null;
			if(!StringUtils.isNullOrEmpty(startDate)){
				end=sdf2.parse(endDate);
			}
			Map<String,Object> map=new HashMap<>();
			if(!StringUtils.isNullOrEmpty(status)){
				map.put("status", status);
			}
			if(!StringUtils.isNullOrEmpty(mobileNo)){
				UserEntity user=userService.selectByMolieNo(mobileNo);
				if(user!=null){
					map.put("userId", user.id);
				}
			}
			if(!StringUtils.isNullOrEmpty(ticketSerialNo)){
				TicketEntity ticket=ticketService.getTicketsInfoBySerialno(ticketSerialNo);
				if(ticket!=null){
					map.put("ticketId", ticket.id);
				}
			}
			if(startDate!=null){
				map.put("startDate", start);
			}
			if(endDate!=null){
				map.put("endDate", end);
			}
			if(currentPage!=null&&currentPage>0&&limit!=null){
				Integer pageStart=(currentPage-1)*limit;
				map.put("pageStart", pageStart);
				map.put("limit", limit);
			}
			bookList=bookMapper.queryBookByCond(map);
		}catch(Exception e){
			System.out.println("query by condition fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	/**
	 * 统计查询结果数
	 * @param mobileNo
	 * @param ticketSerialNo
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer getCountByCond(String mobileNo,String ticketSerialNo,String status,String startDate,String endDate){
		Integer count=0;
		try{
			Date start=null;
			if(!StringUtils.isNullOrEmpty(startDate)){
				start=sdf2.parse(startDate);
			}
			Date end=null;
			if(!StringUtils.isNullOrEmpty(startDate)){
				end=sdf2.parse(endDate);
			}
			Map<String,Object> map=new HashMap<>();
			if(!StringUtils.isNullOrEmpty(status)){
				map.put("status", status);
			}
			if(!StringUtils.isNullOrEmpty(mobileNo)){
				UserEntity user=userService.selectByMolieNo(mobileNo);
				if(user!=null){
					map.put("userId", user.id);
				}
			}
			if(!StringUtils.isNullOrEmpty(ticketSerialNo)){
				TicketEntity ticket=ticketService.getTicketsInfoBySerialno(ticketSerialNo);
				if(ticket!=null){
					map.put("ticketId", ticket.id);
				}
			}
			if(startDate!=null){
				map.put("startDate", start);
			}
			if(endDate!=null){
				map.put("endDate", end);
			}
			List<Book> bookList=bookMapper.queryBookByCond(map);
			if(bookList!=null){
				count=bookList.size();
			}
		}catch(Exception e){
			System.out.println("query by condition fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 按条件分页查询
	 * @param mobileNo
	 * @param ticketSerialNo
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param begin
	 * @param limit
	 * @return
	 */
	public Page<Book> queryByCondPage(String mobileNo,String ticketSerialNo,String startDate,String endDate,String status,Integer begin,Integer limit){
		List<Book> bookList = new ArrayList<>();
        List<Book> resultList=queryByCondLimt(mobileNo, ticketSerialNo, status, startDate, endDate, begin, limit);
        if(null != resultList) {
            for (int i = 0; i < resultList.size(); i++) {
            	bookList.add(resultList.get(i));
            }
        }
        int sum = getCountByCond(mobileNo, ticketSerialNo, status, startDate, endDate);
        Page<Book> result = new Page<>();
        result.setCurrentPage(begin);
        result.setPageSize(limit);
        result.setRows(sum);
        result.setResultList(resultList);
        return result;
	}
}
