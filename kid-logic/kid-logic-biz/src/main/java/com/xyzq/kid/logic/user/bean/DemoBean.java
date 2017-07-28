package com.xyzq.kid.logic.user.bean;

import com.xyzq.kid.logic.user.dao.DemoDAO;
import com.xyzq.kid.logic.user.dao.po.DemoPO;
import com.xyzq.kid.logic.user.entity.DemoEntity;
import com.xyzq.simpson.utility.cache.core.ITimeLimitedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class DemoBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private DemoDAO demoDAO;
    /**
     * 范例数据库访问对象
     */
    @Resource(name = "cache")
    private ITimeLimitedCache<String, Integer> cache;


    /**
     * 方法描述
     *
     * @return 返回值
     */
    public DemoEntity call() {
        cache.set("demo", 9527);
        int name = cache.get("demo");
        System.out.println(name);
        DemoPO demoPO = demoDAO.load(1);
        DemoEntity entity = new DemoEntity();
        entity.id = demoPO.getId();
        entity.content = demoPO.getContent();
        return entity;
    }
}
