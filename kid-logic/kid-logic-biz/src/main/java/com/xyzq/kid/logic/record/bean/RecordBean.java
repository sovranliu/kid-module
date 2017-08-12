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
        if(recordPO == null){
            return null;
        }
        RecordEntity entity = new RecordEntity(recordPO);
        return entity;
    }
    /**
     * 根据飞行票ID和购买状态字段查询record
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    public List<RecordEntity> findBy(String serialNo, String purchased){
        List<RecordPO> recordPOList = recordDAO.findBy(serialNo, purchased);
        List<RecordEntity> recordEntityList= new ArrayList<>();
        for (RecordPO recordPO :recordPOList) {
            RecordEntity entity = new RecordEntity(recordPO);
            recordEntityList.add(entity);
        }
        return recordEntityList;
    }

    /**
     * 根据飞行票ID和购买状态字段查询record
     * @Param usedTIcketSerialNoList
     * @Param purchased
     * @return RecordEntity
     */
    public List<RecordEntity> findBy(List<String> usedTIcketSerialNoList, String purchased){
        List<RecordPO> recordPOList = recordDAO.findBy(usedTIcketSerialNoList,purchased);
        List<RecordEntity> recordEntityList= new ArrayList<>();
        for (RecordPO recordPO :recordPOList ) {
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
    public int buyRecords(String serialNo){
        return recordDAO.buyRecords(serialNo);
    }
    /**
     * 新增飞行日志
     * @Param RecordPO
     * @return int
     */
    public int addRecord(RecordPO recordPO){
        return recordDAO.addRecord(recordPO);
    }
    /**
     * 删除飞行日志
     * @Param RecordPO
     * @return int
     */
    public int deleteRecord(Integer id){
        return recordDAO.deleteRecord(id);
    }
    /**
     * 更新飞行日志
     * @Param RecordPO
     * @return int
     */
    public void saveRecords(String serialNo,List<Integer> ids){
        recordDAO.saveRecords(serialNo,ids);
    }
}

