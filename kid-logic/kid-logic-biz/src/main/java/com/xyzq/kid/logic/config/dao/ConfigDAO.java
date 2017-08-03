package com.xyzq.kid.logic.config.dao;

import com.xyzq.kid.logic.config.dao.po.ConfigPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配置数据访问对象
 */
public interface ConfigDAO {
    /**
     * 一次查询所有的配置结果
     *
     * @return 配置列表
     */
    public List<ConfigPO> select();

    /**
     * 加载指定名称的配置
     *
     * @param name 配置名称
     * @return 配置持久化对象
     */
    public ConfigPO load(@Param("name") String name);

    /**
     * 更新记录
     *
     * @param name 配置名称
     * @param content 配置值
     * @return 记录变更条数
     */
    public int update(@Param("name") String name, @Param("content") String content);
}
