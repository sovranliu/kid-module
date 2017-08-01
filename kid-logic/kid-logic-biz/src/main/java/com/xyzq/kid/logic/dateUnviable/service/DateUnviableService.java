package com.xyzq.kid.logic.dateUnviable.service;

import com.xyzq.kid.logic.dateUnviable.bean.DateUnviableBean;
import com.xyzq.kid.logic.dateUnviable.entity.DateUnviableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by Brann on 17/7/27.
 */
@Service("dateUnviableService")
public class DateUnviableService {
    /**
     * 范例组件
     */
    @Autowired
    private DateUnviableBean dateUnviableBean;


    @Value("${file_server_upload_url}")
    private String file_server_upload_url;

    private static Logger log = Logger.getLogger(DateUnviableService.class.getName());


    /**
     * 根据ID加载DateUnviableEntity
     *
     * @return 返回值
     * @Param id
     */
    public DateUnviableEntity load(Integer id) {
        DateUnviableEntity entity =  dateUnviableBean.load(id);
        return entity;
    }

    /**
     * 根据日期字符串dataStr加载DateUnviableEntity
     *
     * @return 返回值
     * @Param unviableDate
     */
    public DateUnviableEntity findBy(String unviableDate) {
        DateUnviableEntity entity = dateUnviableBean.findBy(unviableDate);
        return entity;
    }

    /**
     * 新增不可预约日期记录
     *
     * @return Integer
     * @Param DateUnviablePO
     */
    public int insertDateUnviable(String unviableDate) {
        return dateUnviableBean.insertDateUnviable(unviableDate);
    }

    /**
     * 删除不可预约日期记录
     *
     * @return Integer
     * @Param DateUnviablePO
     */
    public int deleteDateUnviable(String unviableDate) {
        return dateUnviableBean.deleteDateUnviable(unviableDate);
    }
}
