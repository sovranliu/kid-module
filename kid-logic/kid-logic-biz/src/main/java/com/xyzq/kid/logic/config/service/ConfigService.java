package com.xyzq.kid.logic.config.service;

import com.xyzq.kid.logic.config.common.ConfigCommon;
import com.xyzq.kid.logic.config.dao.ConfigDAO;
import com.xyzq.kid.logic.config.dao.po.ConfigPO;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.simpson.base.text.Text;
import com.xyzq.simpson.base.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 系统配置服务
 */
@Service("configService")
public class ConfigService {
    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(ConfigService.class);
    /**
     * 配置数据库访问对象
     */
    @Autowired
    private ConfigDAO configDAO;


    /**
     * 查找所有的系统配置
     *
     * @return 系统配置列表
     */
    public List<ConfigEntity> find() {
        LinkedList<ConfigEntity> result = new LinkedList<ConfigEntity>();
        for(ConfigPO configPO : configDAO.select()) {
            ConfigEntity configEntity = convert(configPO);
            if(null == configEntity) {
                continue;
            }
            result.add(configEntity);
        }
        return result;
    }

    /**
     * 加载指定名称的配置
     *
     * @param name 配置名称
     * @return 配置实体
     */
    public ConfigEntity load(String name) {
        return convert(configDAO.load(name));
    }

    /**
     * 获取指定名称和类型的值
     *
     * @param name 名称
     * @param clazz 类型
     * @return 类型转换后的值
     */
    public <T>T fetch(String name, Class<T> clazz) {
        ConfigEntity configEntity = load(name);
        if(null == configEntity || null == configEntity.content) {
            return null;
        }
        if(null == clazz || String.class.equals(clazz)) {
            return (T) configEntity.content;
        }
        else if(Boolean.class.equals(clazz)) {
            return (T) Boolean.valueOf(configEntity.content);
        }
        else if(Byte.class.equals(clazz)) {
            return (T) Byte.valueOf(configEntity.content);
        }
        else if(Short.class.equals(clazz)) {
            return (T) Short.valueOf(configEntity.content);
        }
        else if(Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(configEntity.content);
        }
        else if(Long.class.equals(clazz)) {
            return (T) Long.valueOf(configEntity.content);
        }
        else if(Float.class.equals(clazz)) {
            return (T) Float.valueOf(configEntity.content);
        }
        else if(Double.class.equals(clazz)) {
            return (T) Double.valueOf(configEntity.content);
        }
        else if(BigDecimal.class.equals(clazz)) {
            return (T) (new BigDecimal(configEntity.content));
        }
        throw new RuntimeException("fetch config with unsupported type, type = " + clazz);
    }

    /**
     * 获取指定名称的值
     *
     * @param name 名称
     * @return 类型转换后的值
     */
    public String fetch(String name) {
        return fetch(name, String.class);
    }

    /**
     * 更新指定配置的值
     *
     * @param name 配置名称
     * @param content 配置值
     * @return 更新结果
     */
    public boolean alter(String name, Object content) {
        ConfigEntity configEntity = load(name);
        if(null == configEntity) {
            return false;
        }
        String value = null;
        if(null == content) {
            value = "";
        }
        else {
            value = String.valueOf(content);
        }
        if(Text.isBlank(configEntity.pattern)) {
            configDAO.update(name, value);
            return true;
        }
        Pattern pattern = Pattern.compile(configEntity.pattern);
        if(pattern.matcher(value).find()) {
            configDAO.update(name, value);
            return true;
        }
        else {
            logger.error("alter config failed, name = " + name + ", content = " + value);
            return false;
        }
    }

    /**
     * 将配置持久对象转换成配置实体
     *
     * @param configPO 配置持久对象
     * @return 配置实体
     */
    private static ConfigEntity convert(ConfigPO configPO) {
        if(null == configPO) {
            return null;
        }
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.name = configPO.getName();
        configEntity.title = configPO.getTitle();
        configEntity.content = configPO.getContent();
        configEntity.pattern = configPO.getPattern();
        configEntity.createTime = DateTime.parse(configPO.getCreateTime());
        configEntity.updateTime = DateTime.parse(configPO.getUpdateTime());
        return configEntity;
    }

    /**
     * 获取价格信息
     *
     * @return 价格信息
     */
    public Map<String, Integer> getPriceInfo() {
        Map<String, Integer> result = new HashMap<String, Integer>();
        ConfigEntity configEntity = null;
        configEntity = load(ConfigCommon.FEE_SINGLETICKET);
        result.put(configEntity.name, Integer.valueOf(configEntity.content));
        configEntity = load(ConfigCommon.FEE_GROUPTICKET);
        result.put(configEntity.name, Integer.valueOf(configEntity.content));
        configEntity = load(ConfigCommon.FEE_INSURANCE);
        result.put(configEntity.name, Integer.valueOf(configEntity.content));
        return result;
    }
}
