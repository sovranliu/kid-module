package com.xyzq.kid.logic.record.bean;

/**
 * Created by Brann on 17/7/27.
 */

import com.xyzq.kid.logic.record.dao.RecordDAO;
import com.xyzq.kid.logic.record.dao.po.RecordPO;
import com.xyzq.kid.logic.record.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class RecordBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private RecordDAO recordDAO;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public RecordEntity call() {
        RecordPO recordPO = recordDAO.load(1);
        RecordEntity entity = new RecordEntity(recordPO);
        return entity;
    }
    /**
     * 根据飞行票ID和购买状态字段查询record
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    public RecordEntity findBy(int ticketID,String purchased){
        RecordPO recordPO = recordDAO.findBy(ticketID,purchased);
        RecordEntity entity = new RecordEntity(recordPO);
        return entity;
    }
    /**
     * 购买飞行日志
     * @Param RecordPO
     * @return int
     */
    public int buyRecord(RecordPO recordPO){
        return recordDAO.buyRecord(recordPO);
    }
}
