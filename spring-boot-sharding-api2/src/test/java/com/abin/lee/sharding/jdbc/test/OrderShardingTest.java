package com.abin.lee.sharding.jdbc.test;

import com.abin.lee.sharding.jdbc.common.generator.SnowflakeIdWorker;
import com.abin.lee.sharding.jdbc.common.util.JsonUtil;
import com.abin.lee.sharding.jdbc.dao.OrderItemMapper;
import com.abin.lee.sharding.jdbc.dao.OrderMapper;
import com.abin.lee.sharding.jdbc.entity.Order;
import com.abin.lee.sharding.jdbc.entity.OrderItem;
import com.abin.lee.sharding.jdbc.main.ShardingJdbcApplication;
import com.abin.lee.sharding.jdbc.vo.OrderItemMixedBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShardingJdbcApplication.class)
public class OrderShardingTest {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;


    @Test
//    @Transactional
    public void testCreateOrder1() {
        Order order = new Order();
        long userId = 29965307745927172L;
//        long userId = SnowflakeIdWorker.getId(4L);
        long orderId = SnowflakeIdWorker.getId(userId);
        order.setId(orderId);
        order.setUserId(userId);
        order.setBusinessId((long) (Math.random() * 100000));
        order.setOrderCount((int) (Math.random() * 20));
        order.setOrderPrice(new BigDecimal(100));
        orderMapper.insert(order);
        OrderItem orderItem = new OrderItem();
        long itemId = SnowflakeIdWorker.getId(4L);
        orderItem.setId(itemId);
        String businessDesc = "business-desc695" ;
//        String businessDesc = "business-desc" + (long) (Math.random() * 1000) ;
        orderItem.setBusinessDesc(businessDesc);
        String businessName = "fish-755" ;
//        String businessName = "fish-" + (long) (Math.random() * 1000) ;
        orderItem.setBusinessName(businessName);
        orderItem.setOrderId(orderId);
        orderItem.setUserId(userId);
        orderItemMapper.insert(orderItem);
    }


    @Test
    public void testSelectById() {
        int orderCount = orderMapper.selectById(29964856509595652L);
        System.out.println("-----------------------------------------------------");
        System.out.println(orderCount + "--------= " + orderCount);
        System.out.println("-----------------------------------------------------");
    }

    @Test
    public void testSelectByUserId() {
        int orderCount = orderMapper.selectByUserId(29965307745927172L);
        System.out.println("-----------------------------------------------------");
        System.out.println(orderCount + "--------= " + orderCount);
        System.out.println("-----------------------------------------------------");
    }

    @Test
    public void testSelectListByUserId() {
        List<OrderItemMixedBean> orderItemMixedBeanList = orderMapper.selectListByUserId(29965307745927172L);
        System.out.println("-----------------------------------------------------");
        System.out.println("orderItemMixedBeanList--------= " + JsonUtil.toJson(orderItemMixedBeanList));
        System.out.println("-----------------------------------------------------");
    }


    @Test
    public void testGenetorId() {
        long sequenceMask = -1L ^ (-1L << 8L);
        System.out.println("-----------------------------------------------------");
        System.out.println( "-----------------------sequenceMask = " + sequenceMask);
        System.out.println("-----------------------------------------------------");

    }



}
