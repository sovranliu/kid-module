package com.xyzq.kid.finance.dao;

import com.xyzq.kid.finance.dao.po.OrderPO;
import org.apache.ibatis.annotations.Param;

/**
 * 订单数据访问对象
 */
public interface OrderDAO {
    /**
     * 根据订单号查询单条订单
     *
     * @param orderNo 订单号
     * @return 订单持久化对象
     */
    public OrderPO loadByOrderNo(String orderNo);

    /**
     * 新增单条订单
     *
     * @param orderPO 订单持久化对象
     * @return 变更条数
     */
    public int insertOrder(OrderPO orderPO);

    /**
     * 更新订单状态为待支付
     *
     * @param orderNo 订单号
     * @param prepayId 预支付ID
     * @return 记录变动条数
     */
    public int updateOrderSuccess(@Param("orderNo") String orderNo, @Param("prepayId") String prepayId);

    /**
     * 更新订单状态为关闭
     *
     * @param orderNo 订单号
     * @return 记录变动条数
     */
    public int updateOrderClosed(@Param("orderNo") String orderNo);

    /**
     * 更新订单状态为支付成功
     *
     * @param orderNo 订单号
     * @return 记录变动条数
     */
    public int updateOrderPaid(@Param("orderNo") String orderNo);
}
