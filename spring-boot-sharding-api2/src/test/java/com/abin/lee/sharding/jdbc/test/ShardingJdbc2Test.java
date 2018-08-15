package com.abin.lee.sharding.jdbc.test;

import com.abin.lee.sharding.jdbc.dao.OrderMapper;
import com.abin.lee.sharding.jdbc.entity.Order;
import com.abin.lee.sharding.jdbc.main.ShardingJdbcApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

/**
 * https://github.com/sharding-sphere/sharding-sphere/blob/dev/sharding-jdbc/src/test/java/io/shardingsphere/core/api/MasterSlaveDataSourceFactoryTest.java
 * https://blog.csdn.net/yanyan19880509/article/details/78008461
 * https://blog.csdn.net/yanyan19880509/article/details/78108468
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShardingJdbcApplication.class)
public class ShardingJdbc2Test {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void test() {
        Order order = new Order();
        order.setUserId((long) (Math.random() * 100000));
        order.setBusinessId((long) (Math.random() * 100000));
        orderMapper.insert(order);
    }

    @Test
    public void assertCreateDataSourceForSingleSlave() throws SQLException {
//        Map<String, DataSource> dataSourceMap = new HashMap<>(2, 1);
//        dataSourceMap.put("master_ds", new DruidDataSource("master_ds"));
//        dataSourceMap.put("slave_ds", new DruidDataSource("slave_ds"));
//        Map<String, Object> configMap = new ConcurrentHashMap<>();
//        Properties properties = new Properties();
//        properties.setProperty("sql.show", "true");
//        configMap.put("key1", "value1");
//        assertThat(MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
//                new MasterSlaveRuleConfiguration("logic_ds", "master_ds", Collections.singletonList("slave_ds")), configMap, properties), instanceOf(MasterSlaveDataSource.class));
//        MatcherAssert.assertThat(ConfigMapContext.getInstance().getMasterSlaveConfig(), is(configMap));
    }

}
