package com.xyzq.kid.logic.cms.service;

import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.cms.bean.CMSBean;
import com.xyzq.kid.logic.cms.entity.CMSEntity;
import com.xyzq.kid.logic.ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XYZQ on 2017/8/12.
 */
@Service("cmsService")
public class CMSService {
    /**
     * 日志对象
     */
    public static Logger logger = LoggerFactory.getLogger(CMSService.class);

    @Autowired
    private CMSBean cmsBean;

    public int insertCMS(CMSEntity cmsEntity) {
        logger.info("CMSService.insertCMS[in]-cmsEntity:" + cmsEntity.toString());
        return cmsBean.insertCMS(cmsEntity);
    }

    public int updateCMS(CMSEntity cmsEntity) {
        logger.info("CMSService.cmsEntity[in]-cmsEntity:" + cmsEntity.toString());
        return cmsBean.updateCMS(cmsEntity);
    }

    public CMSEntity loadCMSById(Integer cmsId) {
        logger.info("CMSService.cmsEntity[in]-loadCMSById:" + cmsId);
        return cmsBean.loadCMSById(cmsId);
    }

    public Page<CMSEntity> getCMSCond(Integer categoryid, String title, Integer begin, Integer limit) {
        logger.info("CMSService.cmsEntity[in]-getCMSCond:categoryid[" + categoryid + "],title[" + title + "]");
        Map paramMap = new HashMap<>();
        if(null != categoryid) {
            paramMap.put("categoryid", categoryid);
        }
        if(null != title && title.length() > 0) {
            paramMap.put("title", title);
        }
        paramMap.put("begin", begin);
        paramMap.put("limit", limit);
        return cmsBean.getCMSCond(paramMap);
    }

    public List<CMSEntity> getCMSByCategoryid(Integer categoryid) {
        return cmsBean.getCMSByCategoryid(categoryid);
    }

}
