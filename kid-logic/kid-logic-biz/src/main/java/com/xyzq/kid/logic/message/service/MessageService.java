package com.xyzq.kid.logic.message.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.xyzq.kid.logic.message.dao.MessageMapper;
import com.xyzq.kid.logic.message.dao.po.Message;

/**
 * 在线留言及后台管理维护服务
 * @author keyanggui
 *
 */
@Service("messageService")
public class MessageService {
	@Autowired
	MessageMapper messageMapper;
	
	/**
	 * 用户留言
	 * @param userId
	 * @param content
	 * @return
	 */
	public boolean createMessage(Integer userId,String content){
		boolean flag=false;
		try{
			Message message=new Message();
			message.setUserid(userId);
			message.setMessage(content);
			message.setCreatetime(new Date());
			message.setLastupdatetime(new Date());
			message.setDeleteflag("0");
			messageMapper.insertSelective(message);
			flag=true;
		}catch(Exception e){
			System.out.println("create a message fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 用户修改留言
	 * @param userId
	 * @param messageId
	 * @param content
	 * @return
	 */
	public boolean modifiedMessage(Integer userId,Integer messageId,String content){
		boolean flag=false;
		try{
			Message message=messageMapper.selectByPrimaryKey(messageId);
			if(message!=null&&message.getUserid().equals(userId)){
				message.setMessage(content);
				message.setLastupdatetime(new Date());
				messageMapper.updateByPrimaryKeySelective(message);
				flag=true;
			}
		}catch(Exception e){
			System.out.println("modified message fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 根据用户id查询所有留言
	 * @param userId
	 * @return
	 */
	public List<Message> queryAllMessageByUserId(Integer userId){
		List<Message> msgList=null;
		try{
			Message message=new Message();
			message.setUserid(userId);
			message.setDeleteflag("0");
			msgList=messageMapper.selectBySelectiveKey(message);
		}catch(Exception e){
			System.out.println("query message by userid fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}
	
	/**
	 * 条件查询未回复的留言
	 * @param fromDate
	 * @param toDate
	 * @param sortType
	 * @param limit
	 * @return
	 */
	public List<Message> queryNoAnswerByTime(Date fromDate,Date toDate,String sortType,int limit){
		List<Message> msgList=null;
		try{
			Map<String,Object> params=new HashMap<>();
			if(fromDate!=null){
				params.put("fromDate", fromDate);
			}
			if(toDate!=null){
				params.put("toDate", toDate);
			}
			if(!StringUtils.isNullOrEmpty(sortType)){
				//lastupdatetime sort by
				if(sortType.equals("desc")||sortType.equals("asc")){
					params.put("sortType", sortType);
				}
			}
			if(limit>0){
				params.put("limit", limit);
			}
			params.put("noanswer", "Y");
			msgList=messageMapper.queryByCond(params);
		}catch(Exception e){
			System.out.println("query no answer message by time "+sortType+" fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}
	
	public List<Message> queryAllMessageByTime(Date fromDate,Date toDate,String sortType,int limit){
		List<Message> msgList=null;
		try{
			Map<String,Object> params=new HashMap<>();
			if(fromDate!=null){
				params.put("fromDate", fromDate);
			}
			if(toDate!=null){
				params.put("toDate", toDate);
			}
			if(!StringUtils.isNullOrEmpty(sortType)){
				//lastupdatetime sort by
				if(sortType.equals("desc")||sortType.equals("asc")){
					params.put("sortType", sortType);
				}
			}
			if(limit>0){
				params.put("limit", limit);
			}
			msgList=messageMapper.queryByCond(params);
		}catch(Exception e){
			System.out.println("query message by time "+sortType+" fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}
	
	/**
	 * 后台管理员回复用户留言
	 * @param messageId
	 * @param adminName
	 * @param answerContent
	 * @return
	 */
	public boolean answerMessage(Integer messageId,String adminName,String answerContent){
		boolean flag=true;
		try{
			Message message=messageMapper.selectByPrimaryKey(messageId);
			if(message!=null){
				message.setAnswer(answerContent);
				message.setAnswerperson(adminName);
				message.setAnswertime(new Date());
				message.setLastupdatetime(new Date());
				messageMapper.updateByPrimaryKeySelective(message);
				flag=true;
			}
		}catch(Exception e){
			System.out.println("admin answer user's message fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 管理员更新回复
	 * @param messageId
	 * @param adminName
	 * @param answerContent
	 * @return
	 */
	public boolean modifiedAnswer(Integer messageId,String adminName,String answerContent){
		boolean flag=false;
		try{
			Message message=messageMapper.selectByPrimaryKey(messageId);
			if(message!=null){
				message.setAnswer(answerContent);
				message.setLastupdatetime(new Date());
				message.setAnswerperson(adminName);
				messageMapper.updateByPrimaryKeySelective(message);
				flag=true;
			}
		}catch(Exception e){
			System.out.println("admin modified answer fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
}
