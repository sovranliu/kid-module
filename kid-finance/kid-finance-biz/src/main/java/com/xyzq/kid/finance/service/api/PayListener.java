package com.xyzq.kid.finance.service.api;

/**
 * 支付监听器
 */
public interface PayListener {
    /**
     * 支付回调
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param goodsType 商品类型
     * @param fee 支付金额
     * @param tag 附属数据
     */
    public void onPay(String orderNo, String openId, int goodsType, int fee, String tag);
}
