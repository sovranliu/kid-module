package com.xyzq.kid.logic.dateUnviable.dao;

import com.xyzq.kid.logic.dateUnviable.dao.po.DateUnviablePO;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 不可预约日期记录LGC_Book_DateUnviable表数据访问接口
 */
public interface DateUnviableDAO {

	/**
	 * 根据ID加载DateUnviableEntity
	 *
	 * @return 返回值
	 * @Param id
	 */
	DateUnviablePO load(Integer id);

	/**
	 * 根据日期字符串dataStr加载DateUnviableEntity
	 *
	 * @return 返回值
	 * @Param id
	 */
	DateUnviablePO findBy(@Param("unviableDate")String unviableDate);

	/**
	 * 新增不可预约日期记录
	 *
	 * @return Integer
	 * @Param DateUnviablePO
	 */
	int insertDateUnviable(@Param("unviableDate")String unviableDate);

	/**
	 * 删除不可预约日期记录
	 *
	 * @return Integer
	 * @Param DateUnviablePO
	 */
	int deleteDateUnviable(@Param("unviableDate")String unviableDate);

}
