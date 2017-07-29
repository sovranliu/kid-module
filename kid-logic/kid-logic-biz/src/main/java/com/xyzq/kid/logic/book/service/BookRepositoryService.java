package com.xyzq.kid.logic.book.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.book.dao.BookTimeRepositoryMapper;
import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.BookTimeRepository;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;

/**
 * 可预约库存服务
 * @author keyanggui
 *
 */
@Service("bookRepositoryService")
public class BookRepositoryService {
	
	@Autowired
	BookTimeRepositoryMapper bookTimeRepositoryMapper;
	
	@Autowired
	BookTimeSpanMapper bookTimeSpanMapper;
	
	/**
	 * 预约库存初始化
	 * @param bookDate
	 * @param total
	 * @return
	 */
	public boolean initRepositoryByDate(String bookDate,Integer total){
		boolean flag=false;
		try{
			List<BookTimeSpan> spanList=bookTimeSpanMapper.queryValidTimeSpan();
			if(spanList!=null&&spanList.size()>0){
				for(BookTimeSpan span:spanList){
					BookTimeRepository repo=new BookTimeRepository();
					repo.setBookdate(bookDate);
					repo.setBooktimespanid(span.getId());
					repo.setBooktotal(total);
					repo.setBookamount(total);
					repo.setCreatetime(new Date());
					repo.setLastupdatetime(new Date());
					repo.setDeleteflag("0");
					bookTimeRepositoryMapper.insertSelective(repo);
				}
			}
			flag=true;
		}catch(Exception e){
			System.out.println("init repository by date fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 修改指定预约时间库存
	 * @param id
	 * @param nTotal
	 * @return
	 */
	public boolean updateRepositoryTotalById(Integer id,Integer nTotal){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(id);
			repo.setBooktotal(nTotal);
			repo.setLastupdatetime(new Date());
			bookTimeRepositoryMapper.updateByPrimaryKey(repo);
			flag=true;
		}catch(Exception e){
			System.out.println("update repository by id fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 可预约时间开关
	 * @param id
	 * @param deleteFlag 0：可预约，1：关闭预约
	 * @return
	 */
	public boolean updateStatusById(Integer id,String deleteFlag){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(id);
			repo.setDeleteflag(deleteFlag);
			repo.setLastupdatetime(new Date());
			bookTimeRepositoryMapper.updateByPrimaryKeySelective(repo);
			flag=true;
		}catch(Exception e){
			System.out.println("update status by id fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 根据日期和票类型，查询可预约时间
	 * @param bookDate
	 * @param ticketType
	 * @return
	 */
	public List<BookTimeRepository> queryRepositoryByDate(String bookDate){
		List<BookTimeRepository> repoList=new ArrayList<>();
		try{
			repoList=bookTimeRepositoryMapper.selectByBookDate(bookDate);
		}catch(Exception e){
			System.out.println("query repository by date fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return repoList;
	}
	
	/**
	 * 根据预约日期、时间段ID、票类型，查询库存情况
	 * @param bookDate
	 * @param timeSpanId
	 * @param ticketType
	 * @return
	 */
	public BookTimeRepository queryRepositoryByDateAndTimeSpan(String bookDate,Integer timeSpanId){
		BookTimeRepository repository=null;
		try{
			repository=bookTimeRepositoryMapper.selectByBookDateAndTimeSpan(bookDate, timeSpanId);
		}catch(Exception e){
			System.out.println("query repository by date and time span id fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return repository;
	}
	
	/**
	 * 预约成功后扣减库存
	 * @param id
	 * @param status 1:扣库存，2：回退库存，空或其它默认为扣库存
	 * @return
	 */
	public boolean updateAmount(Integer id,String status){
		boolean flag=false;
		try{
			BookTimeRepository repo=bookTimeRepositoryMapper.selectByPrimaryKey(id);
			Integer amount=repo.getBookamount();
			Integer nAmount=0;
			if(StringUtils.isNullOrEmpty(status)){
				nAmount=amount-1;
			}else if("1".equals(status)){//扣库存
				nAmount=amount-1;
			}else if("2".equals(status)){//回退库存
				nAmount=amount+1;
			}else{
				nAmount=amount-1;//其它状态默认为扣库存
			}
			repo.setBookamount(nAmount);
			repo.setLastupdatetime(new Date());
			bookTimeRepositoryMapper.updateByPrimaryKeySelective(repo);
			flag=true;
		}catch(Exception e){
			System.out.println("update amount for book fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}

}
