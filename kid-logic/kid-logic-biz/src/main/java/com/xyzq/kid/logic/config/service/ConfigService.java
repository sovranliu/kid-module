package com.xyzq.kid.logic.config.service;

import com.xyzq.kid.logic.config.bean.ConfigBean;
import com.xyzq.kid.logic.config.common.ConfigCommon;
import com.xyzq.kid.logic.config.dao.po.ConfigPO;
import com.xyzq.kid.logic.config.entity.ConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置服务
 */
@Service("configService")
public class ConfigService {
    /**
     * 系统信息
     */
    @Autowired
    private ConfigBean configBean;


    /**
     * 方法描述 获取价格信息
     *
     * @return 返回值
     */
    public Map getPriceInfo() {
        Map result = new HashMap();
        String[] nameArr = {ConfigCommon.PRICE_SINGLE, ConfigCommon.PRICE_GROUP, ConfigCommon.PRICE_REFUNDINSURANCE};
        List<ConfigEntity> configEntityList = configBean.selectByNameArr(nameArr);
        if(null == configEntityList) {
            return null;
        }
        for (int i = 0; i < configEntityList.size(); i++) {
            result.put(configEntityList.get(i).name, Integer.parseInt(configEntityList.get(i).content));
        }
        return result;
    }

    public String selectContentByName(String name) {
        ConfigEntity configEntity = configBean.selectByName(name);
        return configEntity.content;
    }


}
