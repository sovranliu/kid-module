package com.xyzq.kid.logic.book.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyzq.kid.logic.book.dao.BookChangeRequestMapper;
import com.xyzq.kid.logic.book.dao.BookMapper;
import com.xyzq.kid.logic.book.dao.BookTimeRepositoryMapper;
import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.Book;
import com.xyzq.kid.logic.book.dao.po.BookChangeRequest;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;

/**
 * 预约变更请求服务
 * 包括改期、撤销
 * @author keyanggui
 *
 */
@Service("bookChangeRequestService")
public class BookChangeRequestService {
	
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

	/**
	 * 创建预约改期申请单
	 * @param bookId
	 * @param requestType 1：改期，2：撤销
	 * @param reason
	 * @param userId
	 * @param bookTimeId
	 * @return
	 */
	public boolean createRequest(Integer bookId,String requestType,String reason,Integer userId,Integer bookTimeId){
		boolean flag=false;
		try{
			BookChangeRequest request=new BookChangeRequest();
			request.setBookid(bookId);
			request.setUserid(userId);
			request.setReqesttype(requestType);
			if(requestType.equals("1")){//改期，则需指定新的预约时间
				request.setBooktimeid(bookTimeId);
			}
			request.setRequestreason(reason);
			request.setStatus("1"); //1：申请中，2：通过，3：拒绝
			request.setCreatetime(new Date());
			request.setLastupdatetime(new Date());
			request.setDeleteflag("0");
			bookChangeRequestMapper.insertSelective(request);
			Book book=bookMapper.selectByPrimaryKey(bookId);
			//1：已预约，2：改期申请中，3：改期通过，4：改期拒绝，5：核销完成，6：撤销申请中，7：撤销通过，8：拒绝撤销
			if(requestType.equals("1")){
				book.setBookstatus("2");
			}else if(requestType.equals("2")){
				book.setBookstatus("6");
			}
			book.setLastupdatetime(new Date());
			bookMapper.updateByPrimaryKeySelective(book);
			if(requestType.equals("1")){
				BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(bookTimeId);
				if(repo!=null){
					//改期申请中，占用一个新预约时间库存
					if(bookRepositoryService.updateAmount(bookTimeId, "1")){//1：扣减库存，2：回退库存
						flag=true;
					}
				}
			}else if(requestType.equals("2")){
				//撤销申请中
				flag=true;
			}
		}catch(Exception e){
			System.out.println("create book change request fail,caused by "+e.getMessage());
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
			System.out.println("approve request fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	public List<BookChangeRequest> queryRequestByCond(Map params){
		List<BookChangeRequest> reqList=null;
		try{
			reqList=bookChangeRequestMapper.queryRequestByCond(params);
		}catch(Exception e){
			System.out.println("query Request by condition fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return reqList;
	}
}
