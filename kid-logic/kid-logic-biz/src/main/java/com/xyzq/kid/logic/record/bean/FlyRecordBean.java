package com.xyzq.kid.logic.record.bean;

/**
 * Created by Brann on 17/7/27.
 */

import com.xyzq.kid.logic.record.dao.FlyRecordDAO;
import com.xyzq.kid.logic.record.dao.po.FlyRecordPO;
import com.xyzq.kid.logic.record.entity.FlyRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class FlyRecordBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private FlyRecordDAO flyRecordDAO;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public FlyRecordEntity call() {
        FlyRecordPO flyRecordPO = flyRecordDAO.load(1);
        FlyRecordEntity entity = new FlyRecordEntity(flyRecordPO);
        return entity;
    }
}
