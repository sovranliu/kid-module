package com.xyzq.kid.logic.config.service;

import com.xyzq.kid.logic.config.common.ConfigCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品类型服务
 *
 * 12345，第1位表示商品类型（1:单人票、2:团体票、3:飞行日志），第2位表示单人票是否购买保险（0:不包括，1:包括），第3-5位表示团体票票张数。
 */
@Service("goodsTypeService")
public class GoodsTypeService {
    /**
     * 系统配置服务
     */
    @Autowired
    private ConfigService configService;


    /**
     * 生成单人票商品类型
     *
     * @param containsInsurance 是否包含保险
     * @return 商品类型
     */
    public int makeSingleTicket(boolean containsInsurance) {
        if(containsInsurance) {
            return 11000;
        }
        else {
            return 10000;
        }
    }

    /**
     * 生成团体票商品类型
     *
     * @param ticketCount 票个数
     * @return 商品类型
     */
    public int makeGroupTicket(int ticketCount) {
        return 20000 + ticketCount;
    }

    /**
     * 生成飞行日志商品类型
     *
     * @return 商品类型
     */
    public int makeRecord() {
        return 30000;
    }

    /**
     * 判断商品类型是否是单人票
     *
     * @param goodsType 商品类型
     * @return 是否是单人票
     */
    public boolean isSingleTicket(int goodsType) {
        return 1 == (goodsType / 10000);
    }

    /**
     * 判断商品类型是否是团体票
     *
     * @param goodsType 商品类型
     * @return 是否是团体票
     */
    public boolean isGroupTicket(int goodsType) {
        return 2 == (goodsType / 10000);
    }

    /**
     * 判断商品类型是否是飞行日志
     *
     * @param goodsType 商品类型
     * @return 是否是飞行日志
     */
    public boolean isRecord(int goodsType) {
        return 3 == (goodsType / 10000);
    }

    /**
     * 是否包含保险
     *
     * @param goodsType 商品类型
     * @return 是否包含保险
     */
    public boolean containsInsurance(int goodsType) {
        return (goodsType % 10000) >= 1000;
    }

    /**
     * 计算商品类型中包含的票数
     *
     * @param goodsType 商品类型
     * @return 票数
     */
    public int calculateTicketCount(int goodsType) {
        if(isSingleTicket(goodsType)) {
            return 1;
        }
        else if(isGroupTicket(goodsType)) {
            return goodsType % 1000;
        }
        return 0;
    }

    /**
     * 计算指定商品类型的当前价格
     *
     * @param goodsType 商品类型
     * @return 商品价格（分）
     */
    public int calculateFee(int goodsType) {
        if(isSingleTicket(goodsType)) {
            if(containsInsurance(goodsType)) {
                return configService.fetch(ConfigCommon.FEE_SINGLETICKET, Integer.class) + configService.fetch(ConfigCommon.FEE_INSURANCE, Integer.class);
            }
            else {
                return configService.fetch(ConfigCommon.FEE_SINGLETICKET, Integer.class);
            }
        }
        else if(isGroupTicket(goodsType)) {
            return configService.fetch(ConfigCommon.FEE_GROUPTICKET, Integer.class);
        }
        else if(isRecord(goodsType)) {
            return configService.fetch(ConfigCommon.FEE_RECORD, Integer.class);
        }
        return 0;
    }
}
