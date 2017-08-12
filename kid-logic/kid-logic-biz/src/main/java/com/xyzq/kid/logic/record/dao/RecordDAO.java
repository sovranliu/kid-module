package com.xyzq.kid.logic.record.dao;

import com.xyzq.kid.logic.record.dao.po.RecordPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * LGC_Fly_Record表数据访问接口
 */
public interface RecordDAO {
    /**
     * 加载指定ID的
     */
    RecordPO load(Integer id);

    /**
     * 根据飞行票ID和购买状态字段查询record飞行日志
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    List<RecordPO> findBy(@Param("usedTIcketSerialNoList")List<String> usedTIcketSerialNoList, @Param("purchased")String purchased);
    /**
     * 根据飞行票ID和购买状态字段查询record飞行日志
     * @Param ticketID
     * @Param purchased
     * @return RecordEntity
     */
    List<RecordPO> findBy(@Param("serialNo")String serialNo, @Param("purchased")String purchased);

    /**
     * 购买飞行日志
     * @Param id
     * @return Integer
     */
    Integer buyRecord(Integer id);

    /**
     * 根据飞行票购买所有的飞行日志
     * @Param RecordPO
     * @return Integer
     */
    int buyRecords(String serialNo);

    /**
     * 新增飞行日志
     * @Param RecordPO
     * @return int
     */
    int addRecord(RecordPO recordPO);
    /**
     * 删除飞行日志
     * @Param RecordPO
     * @return int
     */
    int deleteRecord(Integer id);
    /**
     * 保存飞行日志
     * @Param RecordPO
     * @return int
     */
    void saveRecords(@Param("serialNo")String serialNo,@Param("ids")List<Integer> ids);
}
