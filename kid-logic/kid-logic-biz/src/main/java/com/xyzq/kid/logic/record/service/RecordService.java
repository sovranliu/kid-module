package com.xyzq.kid.logic.record.service;

import com.xyzq.kid.logic.record.bean.RecordBean;
import com.xyzq.kid.logic.record.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Brann on 17/7/27.
 */
@Service("recordService")
public class RecordService {
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
    public RecordEntity call() {
        return recordBean.call();
    }
}
