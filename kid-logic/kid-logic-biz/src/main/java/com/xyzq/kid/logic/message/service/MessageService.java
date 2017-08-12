package com.xyzq.kid.logic.message.service;

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
import com.xyzq.kid.logic.message.dao.MessageMapper;
import com.xyzq.kid.logic.message.dao.po.Message;

/**
 * 在线留言及后台管理维护服务
 * @author keyanggui
 *
 */
@Service("messageService")
public class MessageService {
	
	static Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	MessageMapper messageMapper;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
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
			logger.error("create a message fail ,caused by "+e.getMessage());
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
			logger.error("modified message fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 根据用户id查询所有留言
	 * @param userId
	 * @return
	 */
	public Message queryLatestAnswer(Integer userId){
		Message msg=null;
		List<Message> msgList=null;
		try{
			Map<String,Object> map=new HashMap<>();
			map.put("userId", userId);
			map.put("reply", "true");
			msgList=messageMapper.queryByCond(map);
			if(msgList!=null&&msgList.size()>0){
				msg=msgList.get(0);
			}
		}catch(Exception e){
			logger.error("query answer by userid fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msg;
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
			logger.error("query no answer message by time "+sortType+" fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}
	
	public List<Message> queryByCondLimit(Integer userId,String fromDate,String toDate,Integer begin,Integer limit){
		List<Message> msgList=null;
		try{
			Map<String,Object> map=new HashMap<>();
			if(userId!=null){
				map.put("userId", userId);
			}
			if(!StringUtils.isNullOrEmpty(fromDate)){
				Date startDate=sdf.parse(fromDate);
				map.put("fromDate", startDate);
			}
			if(!StringUtils.isNullOrEmpty(toDate)){
				Date endDate=sdf.parse(toDate);
				map.put("toDate", endDate);
			}
			if(begin!=null&&limit!=null){
				map.put("pageStart", (begin-1)*limit);
				map.put("limit", limit);
			}
			msgList=messageMapper.queryByCond(map);
		}catch(Exception e){
			logger.error("query message list by conditions limit fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}
	
	public int getCountByCond(Integer userId,String fromDate,String toDate){
		List<Message> msgList=null;
		int count=0;
		try{
			Map<String,Object> map=new HashMap<>();
			if(userId!=null){
				map.put("userId", userId);
			}
			if(!StringUtils.isNullOrEmpty(fromDate)){
				Date startDate=sdf.parse(fromDate);
				map.put("fromDate", startDate);
			}
			if(!StringUtils.isNullOrEmpty(toDate)){
				Date endDate=sdf.parse(toDate);
				map.put("toDate", endDate);
			}
			msgList=messageMapper.queryByCond(map);
			count=msgList.size();
		}catch(Exception e){
			logger.error("query count by conditions fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return count;
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
			logger.error("query message by time "+sortType+" fail ,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return msgList;
	}

	public Page<Message> queryByCondPage(Integer userId,String beginTime,String endTime,Integer begin,Integer limit){
		List<Message> msgList = new ArrayList<>();
        List<Message> resultList=queryByCondLimit(userId,beginTime,endTime,begin,limit);
        if(null != resultList) {
            for (int i = 0; i < resultList.size(); i++) {
            	msgList.add(resultList.get(i));
            }
        }
        int sum = getCountByCond(userId,beginTime,endTime);
        Page<Message> result = new Page<>();
        result.setCurrentPage(begin);
        result.setPageSize(limit);
        result.setRows(sum);
        result.setResultList(resultList);
        return result;
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
			logger.error("admin answer user's message fail,caused by "+e.getMessage());
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
			logger.error("admin modified answer fail,caused by "+e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
}
