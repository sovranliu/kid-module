package com.xyzq.kid.logic.cms.bean;

import com.xyzq.kid.CommonTool;

import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.cms.dao.po.CMSPO;
import com.xyzq.kid.logic.cms.dao.CMSDAO;
import com.xyzq.kid.logic.cms.entity.CMSEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 这是一个范例Java逻辑功能Bean
 */
@Component
public class CMSBean {
    /**
     * 范例数据库访问对象
     */
    @Autowired
    private CMSDAO cmsDAO;

    public int insertCMS(CMSEntity cmsEntity) {
        CMSPO cmspo = CMSEntityToPO(cmsEntity);
        cmspo.setDeleted(CommonTool.STATUS_NORMAL);
        cmsDAO.insertSelective(cmspo);
        return cmspo.getId();
    }

    public int updateCMS(CMSEntity cmsEntity) {
        return cmsDAO.updateByPrimaryKeySelective(CMSEntityToPO(cmsEntity));
    }

    public CMSEntity loadCMSById(Integer cmsId) {
        return CMSPOToEntity(cmsDAO.selectByPrimaryKey(cmsId));
    }

    public Page<CMSEntity> getCMSCond(Map paramMap) {
        List<CMSEntity> cmsEntityList = new ArrayList<>();
        List<CMSPO> cmspoList = cmsDAO.queryCMSByCond(paramMap);
        if(null != cmspoList) {
            for (int i = 0; i < cmspoList.size(); i++) {
                cmsEntityList.add(CMSPOToEntity(cmspoList.get(i)));
            }
        }
        int sum = cmsDAO.queryCMSByCondCount(paramMap);

        Page result = new Page();
        result.setCurrentPage((Integer)paramMap.get("begin") + 1);
        result.setPageSize((Integer)paramMap.get("limit"));
        result.setRows(sum);
        result.setResultList(cmsEntityList);

        return result;
    }

    public List<CMSEntity> getCMSByCategoryid(Integer categoryid) {
        List<CMSEntity> cmsEntityList = new ArrayList<>();
        Map paramMap = new HashMap<>();
        if(null != categoryid) {
            paramMap.put("categoryid", categoryid);
        }
        paramMap.put("begin", 0);
        paramMap.put("limit", 10000);
        List<CMSPO> cmspoList = cmsDAO.queryCMSByCond(paramMap);
        if(null != cmspoList) {
            for (int i = 0; i < cmspoList.size(); i++) {
                cmsEntityList.add(CMSPOToEntity(cmspoList.get(i)));
            }
        }
        return cmsEntityList;
    }
   

    /**
     * CMSPO 转化为 CMSEntity
     * @param po
     * @return CMSEntity
     */
    public CMSEntity CMSPOToEntity(CMSPO po) {
        if(null == po) {
            return null;
        }
        CMSEntity entity = new CMSEntity();
        if(null != po.getId()) {
            entity.id = po.getId();
        }
        if(null != po.getCategoryid()) {
            entity.categoryid = po.getCategoryid();
        }
        if(null != po.getTitle()) {
            entity.title = po.getTitle();
        }
        if(null != po.getImageurl()) {
            entity.imageurl = po.getImageurl();
        }
        if(null != po.getLink()) {
            entity.link = po.getLink();
        }
        if(null != po.getDeleted()) {
            entity.deleted = po.getDeleted();
        }
        if(null != po.getCreatetime()) {
            entity.createtime = CommonTool.dataToStringYMDHMS(po.getCreatetime());
        }
        if(null != po.getUpdatetime()) {
            entity.updatetime = CommonTool.dataToStringYMDHMS(po.getUpdatetime());
        }
        return entity;
    }

    /**
     * CMSEntity 转化为 CMSPO
     * @param entity
     * @return CMSPO
     */
    public CMSPO CMSEntityToPO(CMSEntity entity) {
        CMSPO po = new CMSPO();
        if(null != entity.id ) {
            po.setId(entity.id);
        }
        if(null != entity.categoryid ) {
            po.setCategoryid(entity.categoryid );
        }
        if(null != entity.title ) {
            po.setTitle(entity.title);
        }
        if(null != entity.imageurl ) {
            po.setImageurl(entity.imageurl);
        }
        if(null != entity.link ) {
            po.setLink(entity.link);
        }
        if(null != entity.deleted) {
            po.setDeleted(entity.deleted);
        }
        if(null != entity.createtime) {
            po.setCreatetime(CommonTool.stringToDataYMDHMS(entity.createtime));
        }
        if(null != entity.updatetime) {
            po.setUpdatetime(CommonTool.stringToDataYMDHMS(entity.updatetime));
        }
        return po;
    }
}
