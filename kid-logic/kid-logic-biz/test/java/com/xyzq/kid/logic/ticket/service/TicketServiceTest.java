package com.xyzq.kid.logic.ticket.service;

import com.xyzq.kid.CommonTool;
import com.xyzq.kid.logic.Page;
import com.xyzq.kid.logic.ticket.entity.TicketEntity;
import com.xyzq.kid.logic.ticket.entity.TicketRefundEntity;
import com.xyzq.kid.logic.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by XYZQ on 2017/7/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:config/spring/applicationContext-dao.xml"})
public class TicketServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @Test
    public void buySingleTickets() throws Exception {
        ticketService.onPay("12345678", "myj_test_new", 20005, 80000, "myj_test_new");
    }


    @Test
    public void handselTickets() throws Exception {
        String res =  ticketService.handselTickets(61, "13666666666", "1388888888");
        System.out.println();
    }

    @Test
    public void useTickets() throws Exception {
        Assert.assertSame("fail!", ticketService.useTickets(36), "success");
        Assert.assertSame("fail!", ticketService.useTickets(37), "ticket expire!");
    }

    @Test
    public void extendTickets() throws Exception {
        Assert.assertSame("fail!", ticketService.extendTickets(36, "2017-10-01"), "ticket status error!");
        Assert.assertSame("fail!", ticketService.extendTickets(37, "2017-10-01"), "ticket expire!");
        Assert.assertSame("fail!", ticketService.extendTickets(38, "2017-06-01"), "wrong extendDate!");
        Assert.assertSame("fail!", ticketService.extendTickets(39, "2017-10-01"), "success");
    }

    @Test
    public void getTicketsInfoBySerialno() throws Exception {
        TicketEntity ticketEntity = ticketService.getTicketsInfoBySerialno("20170730110949896");
        Assert.assertSame("fail!", ticketEntity.id, 40);
    }

    @Test
    public void getTicketsInfoByOwnerMobileNo() throws Exception {

    }

    @Test
    public void getTicketsInfoByPayerOpenID() throws Exception {

    }

    @Test
    public void getTicketHistoryBuTickedId() throws Exception {

    }

    @Test
    public void insertSelective() throws Exception {
        for (int i = 2; i < 101; i++) {
            TicketRefundEntity ticketRefundEntity = new TicketRefundEntity();
            ticketRefundEntity.status = TicketRefundEntity.REFUND_STATUS_NEW;
            ticketRefundEntity.ticketid = i;
            ticketRefundEntity.deleted = CommonTool.STATUS_NORMAL;
            ticketService.insertRefundSelective(ticketRefundEntity);
        }
    }

    @Test
    public void getTicketRefunding() throws Exception {
        Page<TicketRefundEntity> result = ticketService.getTicketRefunding(1, 20);
        System.out.println("begin:" + result.getBegin());
        System.out.println("end:" + result.getEnd());
        System.out.println("current:" + result.getCurrentPage());
        System.out.println("pagesize" + result.getPageSize());
        System.out.println("rows" + result.getRows());
        System.out.println("Totalpage" + result.getTotalPage());
    }

    @Test
    public void queryTicketByCond() throws Exception {
        Page<TicketEntity>  result1 = ticketService.queryTicketByCond("2017080307061687847bf", null, null, null, null, 1, 10);
        Page<TicketEntity>  result2 = ticketService.queryTicketByCond(null, "1388888888", null, null, null, 1, 10);
        Page<TicketEntity>  result3 = ticketService.queryTicketByCond(null, null, "2017-08-01", "2017-08-02", null, 1, 10);
        Page<TicketEntity>  result4 = ticketService.queryTicketByCond(null, null, null, "2017-08-02", null, 1, 10);
        Page<TicketEntity>  result5 = ticketService.queryTicketByCond(null, null, "2017-08-04", null, null, 1, 10);
        Page<TicketEntity>  result6 = ticketService.queryTicketByCond(null, null, null, null, 1, 1, 10);
        System.out.println();
    }

    @Test
    public void loadRefundByTicketId() throws  Exception {
        TicketRefundEntity ticketRefundEntity = ticketService.loadRefundByTicketId(1);
        System.out.println();
    }

}