package com.xyzq.kid.logic.record.dao;

import com.xyzq.kid.logic.record.dao.po.RecordPO;

/**
 * LGC_Fly_Record表数据访问接口
 */
public interface RecordDAO {
    /**
     * 加载指定ID的
     */
    public RecordPO load(int id);
}
