package com.xyzq.kid.logic.user.dao;

import com.xyzq.kid.logic.user.dao.po.DemoPO;

/**
 * 范例表数据访问接口
 */
public interface DemoDAO {
    /**
     * 加载指定ID的
     */
    public DemoPO load(int id);
}
