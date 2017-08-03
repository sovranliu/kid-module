package com.xyzq.kid.logic.config.entity;

import com.xyzq.simpson.base.time.DateTime;

/**
 * 配置参数实体
 */
public class ConfigEntity {
    /**
     * 参数名称
     */
    public String name;
    /**
     * 参数标题
     */
    public String title;
    /**
     * 内容值
     */
    public String content;
    /**
     * 校验参数值合法性的正则表达式
     */
    public String pattern;
    /**
     * 记录变更时间
     */
    public DateTime createTime;
    /**
     * 记录变更时间
     */
    public DateTime updateTime;
}
