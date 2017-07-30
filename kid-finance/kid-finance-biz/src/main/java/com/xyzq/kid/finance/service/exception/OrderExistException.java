package com.xyzq.kid.finance.service.exception;

/**
 * 订单号重复异常
 */
public class OrderExistException extends RuntimeException {
    /**
     * 订单号
     */
    public String orderNo;


    /**
     * 构造函数
     *
     * @param orderNo 订单号
     */
    public OrderExistException(String orderNo) {
        super("order number exist : " + orderNo);
        this.orderNo = orderNo;
    }
}
