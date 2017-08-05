package com.xyzq.kid.logic.pay;

import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.kid.logic.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付监听器
 */
@Service("payListener")
public class PayListenerService implements PayListener {
    /**
     * 票务服务
     */
	@Autowired
	private TicketService ticketService;
    /**
     * 飞行日志服务
     */
	@Autowired
	private RecordService recordService;
	/**
	 * 商品类型服务
	 */
	@Autowired
	private GoodsTypeService goodsTypeService;


    /**
     * 支付回调
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param goodsType 商品类型
     * @param fee 支付金额
     * @param tag 附属数据
     */
	@Override
	public void onPay(String orderNo, String openId, int goodsType, int fee, String tag) {
		if (goodsTypeService.isRecord(goodsType)) {
			recordService.onPay(orderNo, openId, goodsType, fee, tag);
		}
		else {
			ticketService.onPay(orderNo, openId, goodsType, fee, tag);
		}
	}
}
