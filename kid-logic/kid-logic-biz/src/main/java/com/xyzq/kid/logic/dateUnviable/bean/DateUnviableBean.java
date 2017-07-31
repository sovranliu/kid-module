package com.xyzq.kid.logic.dateUnviable.bean;

/**
 * Created by Brann on 17/7/27.
 */

import com.xyzq.kid.logic.dateUnviable.dao.DateUnviableDAO;
import com.xyzq.kid.logic.dateUnviable.dao.po.DateUnviablePO;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DateUnviableBean {

	@Autowired
	private DateUnviableDAO dateUnviableDAO;


	/**
	 * 根据ID加载DateUnviableEntity
	 *
	 * @return 返回值
	 * @Param id
	 */
	public DateUnviableEntity load(Integer id) {
		DateUnviablePO dateUnviablePO = dateUnviableDAO.load(id);
		if(dateUnviablePO == null){
			return  null;
		}
		DateUnviableEntity entity = new DateUnviableEntity(dateUnviablePO);
		return entity;
	}

	/**
	 * 根据日期字符串dataStr加载DateUnviableEntity
	 *
	 * @return 返回值
	 * @Param id
	 */
	public DateUnviableEntity findBy(String unviableDate) {
		DateUnviablePO dateUnviablePO = dateUnviableDAO.findBy(unviableDate);
		if(dateUnviablePO == null){
			return  null;
		}
		DateUnviableEntity entity = new DateUnviableEntity(dateUnviablePO);
		return entity;
	}

	/**
	 * 新增不可预约日期记录
	 *
	 * @return Integer
	 * @Param DateUnviablePO
	 */
	public int insertDateUnviable(String unviableDate) {
		return dateUnviableDAO.insertDateUnviable(unviableDate);
	}

	/**
	 * 删除不可预约日期记录
	 *
	 * @return Integer
	 * @Param DateUnviablePO
	 */
	public int deleteDateUnviable(String unviableDate) {
		return dateUnviableDAO.deleteDateUnviable(unviableDate);
	}
}

