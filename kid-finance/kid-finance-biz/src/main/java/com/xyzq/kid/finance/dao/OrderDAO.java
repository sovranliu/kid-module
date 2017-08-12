package com.xyzq.kid.finance.dao;

import com.xyzq.kid.finance.dao.po.OrderInfoPO;
import com.xyzq.kid.finance.dao.po.OrderPO;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

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

    /**
     * 更新订单状态为退款中
     *
     * @param orderNo 订单号
     * @return 记录变动条数
     */
    public int updateOrderRefunding(@Param("orderNo") String orderNo);

    /**
     * 更新订单状态为退款成功
     *
     * @param orderNo 订单号
     * @return 记录变动条数
     */
    public int updateOrderRefundSuccess(@Param("orderNo") String orderNo);

    /**
     * 更新订单状态为退款失败
     *
     * @param orderNo 订单号
     * @return 记录变动条数
     */
    public int updateOrderRefundFail(@Param("orderNo") String orderNo);

    /**
     * 分页查询
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param status 状态，1：未支付，2：已支付，3：已退款
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param begin 开始索引
     * @param size 查询个数
     * @return 支付订单信息列表
     */
    public List<OrderInfoPO> select(@Param("orderNo") String orderNo, @Param("openId") String openId, @Param("status") int status, @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime, @Param("begin") int begin, @Param("size") int size);

    /**
     * 记录条数
     *
     * @param orderNo 订单号
     * @param openId 微信用户开放ID
     * @param status 状态，1：未支付，2：已支付，3：已退款
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 记录条数
     */
    public int count(@Param("orderNo") String orderNo, @Param("openId") String openId, @Param("status") int status, @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);
}
