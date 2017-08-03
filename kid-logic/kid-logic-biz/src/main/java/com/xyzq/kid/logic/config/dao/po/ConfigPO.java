package com.xyzq.kid.logic.config.dao.po;

import java.util.Date;

/**
 * 配置持久化对象
 */
public class ConfigPO {
    /**
     * 参数ID
     */
    private Integer id;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数标题
     */
    private String title;
    /**
     * 内容值
     */
    private String content;
    /**
     * 校验参数值合法性的正则表达式
     */
    private String pattern;
    /**
     * 记录是否被软删
     */
    private Byte deleted;
    /**
     * 记录变更时间
     */
    private Date createTime;
    /**
     * 记录变更时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
