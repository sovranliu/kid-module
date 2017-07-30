package com.xyzq.kid.logic.config.bean;

import com.xyzq.kid.logic.config.dao.ConfigDAO;
import com.xyzq.kid.logic.config.dao.po.ConfigPO;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import com.xyzq.kid.CommonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class ConfigBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private ConfigDAO configDAO;

    /**
     * 根据单个名称查系统参数
     *
     * @return 返回值
     */
    public ConfigEntity selectByName(String name) {
        ConfigPO configPO = configDAO.selectByName(name);
        return ConfigPOToEntity(configPO);
    }

    /**
     * 根据名称数组查系统参数
     *
     * @return 返回值
     */
    public List<ConfigEntity> selectByNameArr(String[] name) {
        List<ConfigPO> configPOList = configDAO.selectByNameArr(name);
        List<ConfigEntity> configEntityList = new ArrayList<>();
        if(null != configPOList && configPOList.size() > 0) {
            for (int i = 0; i < configPOList.size(); i++) {
                configEntityList.add(ConfigPOToEntity(configPOList.get(i)));
            }
        }
        return configEntityList.size() == 0 ? null : configEntityList;
    }


    /**
     * 根据主键查系统参数
     *
     * @return 返回值
     */
    public ConfigEntity selectByPrimaryKey(int id) {
        ConfigPO configPO = configDAO.selectByPrimaryKey(id);
        return ConfigPOToEntity(configPO);
    }

    /**
     * 根据主键删除系统参数
     * @param id
     * @return
     */
    public int deleteByPrimaryKey(Integer id) {
        return configDAO.deleteByPrimaryKey(id);
    }

    /**
     * 全量新增系统参数
     * @param entity
     * @return
     */
    public int insert(ConfigEntity entity){
        return configDAO.insert(ConfigEntityToPO(entity));
    }

    /**
     * 选择性新增系统参数
     * @param entity
     * @return
     */
    public int insertSelective(ConfigEntity entity){
        return configDAO.insertSelective(ConfigEntityToPO(entity));
    }

    /**
     * 根据主键选择性更新系统参数
     * @param entity
     * @return
     */
    public int updateByPrimaryKeySelective(ConfigEntity entity){
        return configDAO.updateByPrimaryKeySelective(ConfigEntityToPO(entity));
    }

    /**
     * 根据主键全量更新系统参数
     * @param entity
     * @return
     */
    public int updateByPrimaryKey(ConfigEntity entity){
        return configDAO.updateByPrimaryKey(ConfigEntityToPO(entity));
    }

    /**
     * ConfigPO 转化为 ConfigEntity
     * @param po
     * @return ConfigEntity
     */
    public ConfigEntity ConfigPOToEntity(ConfigPO po) {
        if(null == po) {
            return null;
        }
        ConfigEntity entity = new ConfigEntity();
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getName()) {
            entity.name = po.getName();
        }
        if(null != po.getTitle()) {
            entity.title = po.getTitle();
        }
        if(null != po.getContent()) {
            entity.content = po.getContent();
        }
        if(null != po.getDeleted()) {
            entity.deleted = po.getDeleted();
        }
        if(null != po.getCreatetime()) {
            entity.createtime = CommonTool.DataToStringYMDHMS(po.getCreatetime());
        }
        if(null != po.getUpdatetime()) {
            entity.updatetime = CommonTool.DataToStringYMDHMS(po.getUpdatetime());
        }
        return entity;
    }

    /**
     * ConfigEntity 转化为 ConfigPO
     * @param entity
     * @return ConfigPO
     */
    public ConfigPO ConfigEntityToPO(ConfigEntity entity) {
        ConfigPO po = new ConfigPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.name ) {
            po.setName(entity.name);
        }
        if(null != entity.title ) {
            po.setTitle(entity.title);
        }
        if(null != entity.content ) {
            po.setContent(entity.content);
        }
        if(null != entity.deleted ) {
            po.setDeleted(entity.deleted);
        }
//        if(null != entity.createtime) {
//            po.setCreatetime(CommonTool.StringToDataYMDHMS(entity.createtime));
//        }
//        if(null != entity.updatetime) {
//            po.setUpdatetime(CommonTool.StringToDataYMDHMS(entity.updatetime));
//        }
        return po;
    }

}
