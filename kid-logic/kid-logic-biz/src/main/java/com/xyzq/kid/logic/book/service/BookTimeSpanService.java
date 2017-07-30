package com.xyzq.kid.logic.book.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyzq.kid.logic.book.dao.BookTimeSpanMapper;
import com.xyzq.kid.logic.book.dao.po.BookTimeSpan;

/**
 * 维护可预约时间区间
 * @author keyanggui
 *
 */
@Service("bookTimeSpanService")
public class BookTimeSpanService {
	@Autowired
	BookTimeSpanMapper bookTimeSpanMapper;
	
	/**
	 * 可预约时间区间初始化,根据开始整点小时数，结束整点小时数和每个飞行时间间隔，初始化可预约时间区间
	 * 时间格式 09:30,10:00
	 * @param startHour
	 * @param endHour
	 * @param intervl
	 * @return
	 */
	public boolean initTimeSpan(int startHour,int endHour,int intervl){
		boolean flag=false;
		try{
			if(60%intervl==0){
				for(int i=startHour;i<endHour;i++){
					int spans=60/intervl;
					for(int j=0;j<spans;j++){
						String bookTime=null;
						if(j<10){
							if(j*intervl==0){
								bookTime="0"+String.valueOf(i)+":"+"00";
							}else{
								bookTime="0"+String.valueOf(i)+":"+String.valueOf(j*intervl);
							}
						}else{
							if(j*intervl==0){
								bookTime=String.valueOf(i)+":"+"00";
							}else{
								bookTime=String.valueOf(i)+":"+String.valueOf(j*intervl);
							}
						}
						
						BookTimeSpan timeSpan=new BookTimeSpan();
						timeSpan.setTimespan(bookTime);;
						timeSpan.setDeleteflag("0");
						timeSpan.setCreatetime(new Date());
						timeSpan.setLastupdatetime(new Date());
						bookTimeSpanMapper.insertSelective(timeSpan);
					}
				}
				flag=true;
			}
		}catch(Exception e){
			System.out.println("book time span init fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 查询所有可预约时间区间
	 * @return
	 */
	public List<BookTimeSpan> queryAllTimeSpan(){
		List<BookTimeSpan> spanList=null;
		try{
			spanList=queryValidTimeSpans();
		}catch(Exception e){
			System.out.println("query all time span fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return spanList;
	}
	
	/**
	 * 查询有效的可预约时间段
	 * @return
	 */
	public List<BookTimeSpan> queryValidTimeSpans(){
		List<BookTimeSpan> spanList=null;
		Map<String ,Object> map=new HashMap<>();
		map.put("deleteFlag", "0");
		try{
			spanList=bookTimeSpanMapper.queryByCond(map);
		}catch(Exception e){
			System.out.println("query valid time spans fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return spanList;
	}
	
	/**
	 * 修改可预约时间区间状态
	 * 0:打开，1：关闭，默认为打开
	 * @param status
	 * @param id
	 * @return
	 */
	public boolean updateStatusById(String status,Integer id){
		boolean flag=false;
		try{
			BookTimeSpan timeSpan=bookTimeSpanMapper.selectByPrimaryKey(id);
			if(timeSpan!=null){
				timeSpan.setDeleteflag(status);
				timeSpan.setLastupdatetime(new Date());
				bookTimeSpanMapper.updateByPrimaryKeySelective(timeSpan);
				flag=true;
			}else{
				System.out.println("could not find the book time span record");
			}
		}catch(Exception e){
			System.out.println("update book time span status fail,caused by "+e.getMessage() );
			e.printStackTrace();
		}
		return flag;
	}
}
