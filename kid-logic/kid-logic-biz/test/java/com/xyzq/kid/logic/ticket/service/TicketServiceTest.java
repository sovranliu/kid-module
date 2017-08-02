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
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.ownermobileno = "13999999999";
        ticketEntity.payeropenid = "myjtest_single";
        ticketEntity.price = new BigDecimal(560);
        ticketEntity.expiredate = "2017-09-30";
        ticketEntity.insurance = true;
        ticketEntity.orderno = "0000000001";
        ticketService.buySingleTickets(ticketEntity);
    }

    @Test
    public void buyGroupleTickets() throws Exception {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.ownermobileno = "1388888888";
        ticketEntity.payeropenid = "myjtest";
        ticketEntity.price = new BigDecimal(380);
        ticketEntity.expiredate = "2017-09-30";
        ticketEntity.insurance = true;
        ticketEntity.orderno = "0000000001";
        ticketService.buyGroupleTickets(ticketEntity, 4);
    }

    @Test
    public void handselTickets() throws Exception {
        Assert.assertSame("fail!", ticketService.handselTickets(36, "13666666666"), "success");
        Assert.assertSame("fail!", ticketService.handselTickets(37, "1388888888"), "can not handsel to self!");
        Assert.assertSame("fail!", ticketService.handselTickets(40, "13666666666"), "single ticket can not handsel!");
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

}