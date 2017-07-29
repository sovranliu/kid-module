package com.xyzq.kid.logic.record.dao;

import com.xyzq.kid.logic.record.dao.po.RecordPO;

/**
 * LGC_Fly_Record表数据访问接口
 */
public interface RecordDAO {
    /**
     * 加载指定ID的
     */
    RecordPO load(int id);

    /**
     * 根据飞行票ID和购买状态字段查询record飞行日志
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    RecordPO findBy(int ticketID,String purchased);

    /**
     * 购买飞行日志
     * @Param RecordPO
     * @return int
     */
    int buyRecord(RecordPO record);

}
