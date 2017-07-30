package com.xyzq.kid.finance.service.exception;

/**
 * 退款号重复异常
 */
public class RefundExistException extends RuntimeException {
    /**
     * 退款号
     */
    public String refundNo;


    /**
     * 构造函数
     *
     * @param refundNo 退款号
     */
    public RefundExistException(String refundNo) {
        super("refund number exist : " + refundNo);
        this.refundNo = refundNo;
    }
}
