package com.xyzq.kid.finance.dao;

import com.xyzq.kid.finance.dao.po.ReceiptPO;

/**
 * 收款数据访问对象
 */
public interface ReceiptDAO {
    /**
     * 根据订单号查询单条收款
     *
     * @param orderNo 订单号
     * @return 收款持久化对象
     */
    public ReceiptPO loadByOrderNo(String orderNo);

    /**
     * 新增单条收款
     *
     * @param receiptPO 收款持久化对象
     * @return 变更条数
     */
    public int insertReceipt(ReceiptPO receiptPO);
}
