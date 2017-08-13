package com.xyzq.kid.logic.book.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.book.dao.BookChangeRequestMapper;
import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.BookTimeRepositoryMapper;
import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookChangeRequest;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;
import com.xyzq.kid.logic.ticket.service.TicketService;

/**
 * 预约变更请求服务
 * 包括改期、撤销
 * @author keyanggui
 *
 */
@Service("bookChangeRequestService")
public class BookChangeRequestService {
	
	static Logger logger = LoggerFactory.getLogger(BookChangeRequestService.class);
	
	@Autowired
	BookChangeRequestMapper bookChangeRequestMapper;
	
	@Autowired
	BookMapper bookMapper;
	
	@Autowired
	BookRepositoryService bookRepositoryService;
	
	@Autowired
	BookTimeRepositoryMapper bookTimeRepositoryMapper;
	
	@Autowired
	BookTimeSpanMapper bookTimeSpanMapper;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	BookTimeSpanService bookTimeSpanService;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	public BookChangeRequest selectByPk(Integer id){
		return bookChangeRequestMapper.selectByPrimaryKey(id);
	}

	/**
	 * 创建预约改期申请单
	 * @param bookId
	 * @param requestType 1：改期，2：撤销
	 * @param reason
	 * @param userId
	 * @param bookTimeId
	 * @param requestBy 1:用户，2管理员
	 * @return
	 */
	public boolean createRequest(Integer bookId,String requestType,String reason,Integer userId,Integer bookTimeId,String requestBy){
		boolean flag=false;
		try{
			BookChangeRequest request=new BookChangeRequest();
			request.setBookid(bookId);
			request.setUserid(userId);
			request.setReqesttype(requestType);
			if(requestType.equals("1")){//改期，则需指定新的预约时间
				request.setBooktimeid(bookTimeId);
			}
			if(requestBy.equals("1")){//用户申请，则需审批 
				request.setStatus("1"); //1：申请中，2：通过，3：拒绝
				request.setRequestreason(reason);
			}else if(requestBy.equals("2")){//管理员操作，则直接通过
				request.setStatus("2"); //1：申请中，2：通过，3：拒绝
				request.setRequestreason("管理员代用户操作");
			}else{//默认为用户申请
				request.setStatus("1"); //1：申请中，2：通过，3：拒绝
			}
			request.setCreatetime(new Date());
			request.setLastupdatetime(new Date());
			request.setDeleteflag("0");
			bookChangeRequestMapper.insertSelective(request);
			Book book=bookMapper.selectByPrimaryKey(bookId);
			//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
			if(requestType.equals("1")){
				if(requestBy.equals("2")){
					book.setBookstatus("3");//改期通过
					book.setBooktimeid(bookTimeId);
					BookTimeRepository repo=bookRepositoryService.queryByPrimaryKey(bookTimeId);
					BookTimeSpan span=bookTimeSpanService.queryByPrimaryKey(repo.getBooktimespanid());
					book.setBookdate(repo.getBookdate());
					book.setBooktime(span.getTimespan());
				}else{
					book.setBookstatus("2");//改期申请中
				}
			}else if(requestType.equals("2")){
				if(requestBy.equals("2")){
					book.setBookstatus("7");//撤销通过
				}else{
					book.setBookstatus("6");//撤销申请中
				}
			}
			book.setLastupdatetime(new Date());
			bookMapper.updateByPrimaryKeySelective(book);
			if(requestType.equals("1")){//改期
				BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
				if(requestBy.equals("2")){//管理员操作，直接改期成功，则直接扣减新预约时间库存，回退原时间库存
					if(bookRepositoryService.updateAmount(bookTimeId, "1")&&bookRepositoryService.updateAmount(book.getBooktimeid(), "2")){//1：扣减库存，2：回退库存
						flag=true;
					}
				}else{//用户申请，则为申请中
					if(repo!=null){
						//改期申请中，占用一个新预约时间库存
						if(bookRepositoryService.updateAmount(bookTimeId, "1")){//1：扣减库存，2：回退库存
							flag=true;
						}
					}
				}
			}else if(requestType.equals("2")){
				if(requestBy.equals("2")){//管理员撤销，则直接回通库存
					if(bookRepositoryService.updateAmount(book.getBooktimeid(), "2")){
						ticketService.recoverTickets(book.getTicketid());
						flag=true;
					}
				}else{
					//撤销申请中
					flag=true;	
				}
			}
		}catch(Exception e){
			logger.error("create book change request fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 管理员审批
	 * @param requestId
	 * @param approveStatus 1：请中，2：通过，3：拒绝
	 * @param adminId
	 * @param comments
	 * @return
	 */
	public boolean approveRequest(Integer requestId,String approveStatus,Integer adminId,String comments){
		boolean flag=false;
		try{
			BookChangeRequest request=bookChangeRequestMapper.selectByPrimaryKey(requestId);
			if(request!=null){
				request.setApproval(String.valueOf(adminId));
				request.setApprovecomments(comments);
				request.setApprovetime(new Date());
				request.setStatus(approveStatus);
				request.setLastupdatetime(new Date());
				bookChangeRequestMapper.updateByPrimaryKeySelective(request);
				if(approveStatus.equals("2")){
					//审批通过，则修改原预约单为改期申请通过，并回退该时间库存
					Book book=bookMapper.selectByPrimaryKey(request.getBookid());
					if(request.getReqesttype().equals("1")){
						book.setBookstatus("3");
					}else if(request.getReqesttype().equals("2")){
						book.setBookstatus("7");
					}
					book.setBooktimeid(request.getBooktimeid());
					book.setLastupdatetime(new Date());
					BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(book.getBooktimeid());
					BookTimeSpan span=bookTimeSpanMapper.selectByPrimaryKey(repo.getBooktimespanid());
					book.setBookdate(repo.getBookdate());
					book.setBooktime(span.getTimespan());
					bookMapper.updateByPrimaryKeySelective(book);
					
					//回退原时间库存
					if(bookRepositoryService.updateAmount(book.getBooktimeid(), "2")){
						flag=true;
					}
					if(request.getReqesttype().equals("2")){
						//撤销预约申请审批通过，则将票状态变更为未使用状态
						ticketService.recoverTickets(book.getTicketid());
					}
				}else if(approveStatus.equals("3")){
					//审批拒绝，则修改原预约单为改期申请拒绝，改期则回退新占用的时间库存
					Book book=bookMapper.selectByPrimaryKey(request.getBookid());
					if(request.getReqesttype().equals("1")){
						book.setBookstatus("4");
					}else if(request.getReqesttype().equals("2")){
						book.setBookstatus("8");
					}
					book.setLastupdatetime(new Date());
					bookMapper.updateByPrimaryKeySelective(book);
					//改期则回退新占用的时间库存
					if(request.getReqesttype().equals("1")){
						if(bookRepositoryService.updateAmount(request.getBooktimeid(), "2")){
							flag=true;
						}
					}else if(request.getReqesttype().equals("2")){
						//撤销
						flag=true;
					}
				}
			}
		}catch(Exception e){
			logger.error("approve request fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 查询各类型各状态申请单
	 * @param status
	 * @param requestType
	 * @return
	 * @throws ParseException 
	 */
	public List<BookChangeRequest> queryByCondLimt(Integer bookId ,String startDate,String endDate,String status,String requestType,Integer begin,Integer limit) throws ParseException{
		List<BookChangeRequest> reqList=null;
		Map<String,Object> map=new HashMap<>();
		
		if(bookId!=null){
			map.put("bookId", bookId);
		}
		
		if(!StringUtils.isNullOrEmpty(startDate)){
			Date start=sdf.parse(startDate);
			map.put("startDate", start);
		}
		
		if(!StringUtils.isNullOrEmpty(endDate)){
			Date end=sdf.parse(endDate);
			map.put("endDate", end);
		}
		
		if(!StringUtils.isNullOrEmpty(requestType)){
			map.put("requestType",requestType );
		}
		if(!StringUtils.isNullOrEmpty(status)){
			map.put("status", status);
		}
		if(begin!=null&&begin>0){
			Integer pageStart=(begin-1)*limit;
			map.put("begin", pageStart);
			map.put("limit", limit);
		}
		try{
			reqList=bookChangeRequestMapper.queryRequestByCond(map);
		}catch(Exception e){
			logger.error("query Request by condition fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return reqList;
	}
	
	/**
	 * 查询各类型各状态申请单
	 * @param status
	 * @param requestType
	 * @return
	 * @throws ParseException 
	 */
	public Integer getCountByCond(Integer bookId ,String startDate,String endDate,String status,String requestType,Integer begin,Integer limit) throws ParseException{
		Integer count=0; 
		List<BookChangeRequest> reqList=null;
		Map<String,Object> map=new HashMap<>();
		
		if(bookId!=null){
			map.put("bookId", bookId);
		}
		
		if(!StringUtils.isNullOrEmpty(startDate)){
			Date start=sdf.parse(startDate);
			map.put("startDate", start);
		}
		
		if(!StringUtils.isNullOrEmpty(endDate)){
			Date end=sdf.parse(endDate);
			map.put("endDate", end);
		}
		
		if(!StringUtils.isNullOrEmpty(requestType)){
			map.put("requestType",requestType );
		}
		if(!StringUtils.isNullOrEmpty(status)){
			map.put("status", status);
		}
		if(begin!=null&&begin>0){
			Integer pageStart=(begin-1)*limit;
			map.put("begin", pageStart);
			map.put("limit", limit);
		}
		try{
			reqList=bookChangeRequestMapper.queryRequestByCond(map);
			if(reqList!=null&&reqList.size()>0){
				count=reqList.size();
			}
		}catch(Exception e){
			logger.error("query Request by condition fail ,caused by "+e.getMessage());
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
	 * @throws ParseException 
	 */
	public Page<BookChangeRequest> queryByCondPage(Integer bookId ,String startDate,String endDate,String status,String requestType,Integer begin,Integer limit) throws ParseException{
        List<BookChangeRequest> resultList=queryByCondLimt( bookId , startDate, endDate, status, requestType, begin, limit);
        int sum = getCountByCond( bookId , startDate, endDate, status, requestType, begin, limit);
        Page<BookChangeRequest> result = new Page<>();
        result.setCurrentPage(begin);
        result.setPageSize(limit);
        result.setRows(sum);
        result.setResultList(resultList);
        return result;
	}
}
