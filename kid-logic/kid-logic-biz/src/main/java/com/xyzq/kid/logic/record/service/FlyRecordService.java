package com.xyzq.kid.logic.record.service;

import com.xyzq.kid.logic.record.bean.FlyRecordBean;
import com.xyzq.kid.logic.record.entity.FlyRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Brann on 17/7/27.
 */
@Service("flyRecordService")
public class FlyRecordService {
    /**
     * 范例组件
     */
    @Autowired
    private FlyRecordBean flyRecordBean;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public FlyRecordEntity call() {
        return flyRecordBean.call();
    }
}
