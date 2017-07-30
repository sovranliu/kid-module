package com.xyzq.kid.finance.dao;

import com.xyzq.kid.finance.dao.po.RefundPO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 退款数据访问对象
 */
public interface RefundDAO {
    /**
     * 根据订单号查询退款对象集合
     *
     * @param orderNo 订单号
     * @return 退款持久化对象集合
     */
    public List<RefundPO> selectByOrderNo(String orderNo);

    /**
     * 根据订单号查询单条退款
     *
     * @param refundNo 退款单号
     * @return 退款持久化对象
     */
    public RefundPO loadByRefundNo(String refundNo);

    /**
     * 新增单条退款
     *
     * @param refundPO 退款持久化对象
     * @return 变更条数
     */
    public int insertRefund(RefundPO refundPO);

    /**
     * 更新退款状态为处理中
     *
     * @param refundNo 退款单号
     * @param refundId 微信返回退款ID
     * @return 记录变动条数
     */
    public int updateRefunding(@Param("refundNo") String refundNo, @Param("refundId") String refundId);

    /**
     * 更新退款状态
     *
     * @param refundNo 退款单号
     * @param state 退款状态
     * @param refundTime 退款成功时间
     * @return 记录变动条数
     */
    public int updateRefundState(@Param("refundNo") String refundNo, @Param("state") int state, @Param("refundTime") Date refundTime);
}
