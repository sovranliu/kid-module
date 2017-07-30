package com.xyzq.kid.logic.config.entity;

import java.math.BigDecimal;

/**
 * 范例实体
 */
public class ConfigEntity {
    /**
     * 飞行票主键
     */
    public Integer id;
    /**
     * 名称
     */
    public String name;
    /**
     * 标题
     */
    public String title;
    /**
     * 内容
     */
    public String content;
    /**
     * 记录是否被软删
     */
    public Byte deleted;
    /**
     * 记录创建时间
     */
    public String createtime;
    /**
     * 记录变更时间
     */
    public String updatetime;
}
