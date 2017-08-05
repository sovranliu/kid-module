package com.xyzq.kid.logic.pay;

import com.xyzq.kid.finance.service.api.PayListener;
import com.xyzq.kid.logic.config.service.GoodsTypeService;
import com.xyzq.kid.logic.record.service.RecordService;
import com.xyzq.kid.logic.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hedongyang
 * DATE @ 2017/8/6.
 */
public class PayListenerService implements PayListener {
	@Autowired
	private TicketService ticketService;
	@Autowired
	private RecordService recordService;
	/**
	 * 票价服务
	 */
	@Autowired
	private GoodsTypeService goodsTypeService;

	@Override
	public void onPay(String orderNo, String openId, int goodsType, int fee, String tag) {
		if (goodsTypeService.isRecord(goodsType)) {
			recordService.onPay(orderNo, openId, goodsType, fee, tag);
		} else {
			ticketService.onPay(orderNo, openId, goodsType, fee, tag);
		}
	}
}
