package com.xyzq.kid.logic.record.bean;

/**
 * Created by Brann on 17/7/27.
 */

import com.xyzq.kid.logic.record.dao.RecordDAO;
import com.xyzq.kid.logic.record.dao.po.RecordPO;
import com.xyzq.kid.logic.record.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RecordBean {

    @Autowired
    private RecordDAO recordDAO;


    /**
     * 根据ID加载RecordEntity
     * @Param id
     * @return 返回值
     */
    public RecordEntity load(Integer id) {
        RecordPO recordPO = recordDAO.load(id);
        RecordEntity entity = new RecordEntity(recordPO);
        return entity;
    }
    /**
     * 根据飞行票ID和购买状态字段查询record
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    public List<RecordEntity> findBy(Integer ticketID, String purchased){
        List<RecordPO> recordPOList = recordDAO.findBy(ticketID,purchased);
        List<RecordEntity> recordEntityList= new ArrayList<>();
        for (RecordPO recordPO :recordPOList
                ) {
            RecordEntity entity = new RecordEntity(recordPO);
            recordEntityList.add(entity);
        }

        return recordEntityList;
    }
    /**
     * 购买飞行日志
     * @Param RecordPO
     * @return Integer
     */
    public int buyRecord(Integer id){
        return recordDAO.buyRecord(id);
    }
    /**
     * 购买飞行日志
     * @Param RecordPO
     * @return Integer
     */
    public int buyRecords(Integer ticketId){
        return recordDAO.buyRecords(ticketId);
    }
}

