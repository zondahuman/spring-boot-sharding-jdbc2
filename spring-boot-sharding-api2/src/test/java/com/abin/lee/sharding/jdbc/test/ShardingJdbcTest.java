package com.abin.lee.sharding.jdbc.test;

import com.abin.lee.sharding.jdbc.dao.OrderMapper;
import com.abin.lee.sharding.jdbc.entity.Order;
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
    public void test1() {
        Order order = new Order();
        order.setUserId((long) (Math.random() * 100000));
        order.setBusinessId((long) (Math.random() * 100000));
        order.setOrderCount((int)(Math.random() * 20));
        order.setOrderPrice(new BigDecimal(100));
        orderMapper.insert(order);
    }



    @Test
    public void test2() {
        Order order = orderMapper.selectByPrimaryKey((long) 60771);
        System.out.println("-----------------------------------------------------");
        System.out.println(order.getId() + "--------= " + order.getUserId());
        System.out.println("-----------------------------------------------------");

    }

}
