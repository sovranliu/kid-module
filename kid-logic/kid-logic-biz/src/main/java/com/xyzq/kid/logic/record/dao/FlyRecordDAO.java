package com.xyzq.kid.logic.record.dao;

import com.xyzq.kid.logic.record.dao.po.FlyRecordPO;

/**
 * LGC_Fly_Record表数据访问接口
 */
public interface FlyRecordDAO {
    /**
     * 加载指定ID的
     */
    public FlyRecordPO load(int id);
}
