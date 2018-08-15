package com.abin.lee.sharding.jdbc.test;

import com.abin.lee.sharding.jdbc.common.generator.SnowflakeIdWorker;
import com.abin.lee.sharding.jdbc.dao.OrderMapper;
import com.abin.lee.sharding.jdbc.entity.Order;
import com.abin.lee.sharding.jdbc.entity.OrderItem;
import com.abin.lee.sharding.jdbc.main.ShardingJdbcApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShardingJdbcApplication.class)
public class ShardingJdbcTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testCreateOrder1() {
        Order order = new Order();
        long orderId = SnowflakeIdWorker.getId(2L);
        long userId = SnowflakeIdWorker.getId(2L);
        order.setId(orderId);
        order.setUserId(userId);
        order.setBusinessId((long) (Math.random() * 100000));
        order.setOrderCount((int) (Math.random() * 20));
        order.setOrderPrice(new BigDecimal(100));
        orderMapper.insert(order);
        OrderItem orderItem = new OrderItem();
        long itemId = SnowflakeIdWorker.getId(2L);
        orderItem.setId(itemId);
        orderItem.setBusinessDesc("business desc" + (long) (Math.random() * 1000));
        orderItem.setBusinessName("味多美" + (long) (Math.random() * 1000));
        orderItem.setOrderId(orderId);
        orderItem.setUserId(userId);

    }


    @Test
    public void test2() {
        Order order = orderMapper.selectByPrimaryKey((long) 60771);
        System.out.println("-----------------------------------------------------");
        System.out.println(order.getId() + "--------= " + order.getUserId());
        System.out.println("-----------------------------------------------------");

    }

}
