package com.abin.lee.sharding.jdbc.conf;

import com.abin.lee.sharding.jdbc.conf.algorithm.OrderBaseShardingDatabaseAlgorithm;
import com.abin.lee.sharding.jdbc.conf.algorithm.OrderBaseShardingTableAlgorithm;
import com.abin.lee.sharding.jdbc.conf.algorithm.OrderIdShardingDatabaseAlgorithm;
import com.abin.lee.sharding.jdbc.conf.algorithm.OrderIdShardingTableAlgorithm;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Lists;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.algorithm.masterslave.RoundRobinMasterSlaveLoadBalanceAlgorithm;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.ComplexShardingStrategyConfiguration;
import io.shardingjdbc.core.api.config.strategy.NoneShardingStrategyConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.DataSourceHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源及分表配置
 */
@Configuration
@MapperScan(basePackages = "com.abin.lee.sharding.jdbc.dao", sqlSessionTemplateRef = "shardingSqlSessionTemplate")
public class DataSourceConfig {

    @Autowired
    private HealthAggregator healthAggregator;

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * 配置数据源0
     *
     * @return
     */
    @Bean(name = "order0_master")
    @ConfigurationProperties(prefix = "spring.order0_master")
    @Qualifier("order0_master")
    public DataSource dataSource00() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order0_master");
    }

    /**
     * 配置数据源0
     *
     * @return
     */
    @Bean(name = "order0_slave_0")
    @ConfigurationProperties(prefix = "spring.order0_slave_0")
    @Qualifier("order0_slave_0")
    public DataSource dataSource01() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order0_slave_0");
    }

    /**
     * 配置数据源0
     *
     * @return
     */
    @Bean(name = "order0_slave_1")
    @ConfigurationProperties(prefix = "spring.order0_slave_1")
    @Qualifier("order0_slave_1")
    public DataSource dataSource02() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order0_slave_1");
    }


    /**
     * 配置数据源1
     *
     * @return
     */
    @Bean(name = "order1_master")
    @ConfigurationProperties(prefix = "spring.order1_master")
    @Qualifier("order1_master")
    public DataSource dataSource10() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order1_master");
    }

    /**
     * 配置数据源1
     *
     * @return
     */
    @Bean(name = "order1_slave_0")
    @ConfigurationProperties(prefix = "spring.order1_slave_0")
    @Qualifier("order1_slave_0")
    public DataSource dataSource11() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order1_slave_0");
    }

    /**
     * 配置数据源1
     *
     * @return
     */
    @Bean(name = "order1_slave_1")
    @ConfigurationProperties(prefix = "spring.order1_slave_1")
    @Qualifier("order1_slave_1")
    public DataSource dataSource12() {
        return DruidDataSourceBuilder.create().build(environment, "spring.order1_slave_1");
    }


    @Bean(name = "dataSource")
    public DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderBaseTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderDetailTableRuleConfiguration());
//        // 默认不分库分表
        shardingRuleConfig.setDefaultDataSourceName("order1_master");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());

        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(),
                shardingRuleConfig, new ConcurrentHashMap<>(), new Properties());
//        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(),
//                shardingRuleConfig, new ConcurrentHashMap<>(), new Properties());
    }


    // 订单主表
    TableRuleConfiguration getOrderBaseTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration();
        result.setLogicTable("t_order");
        result.setActualDataNodes("order_${0..1}.t_order_${0..3}");
        result.setKeyGeneratorColumnName("id");
        // 分库策略 user_id取模 4个
        result.setDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration("id,user_id", OrderBaseShardingDatabaseAlgorithm.class.getName()));
        // 分表策略 user_id取模 4个
        result.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("id,user_id", OrderBaseShardingTableAlgorithm.class.getName()));
        return result;
    }

    // 订单明细表
    TableRuleConfiguration getOrderDetailTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration();
        result.setLogicTable("t_order_item");
        result.setActualDataNodes("order_${0..1}.t_order_item_${0..3}");
        result.setKeyGeneratorColumnName("order_id");
        // 分库策略 order_id取模 4个
        result.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", OrderIdShardingDatabaseAlgorithm.class.getName()));
        // 分表策略 order_id取模 4个
        result.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", OrderIdShardingTableAlgorithm.class.getName()));
        return result;
    }

    //主从读写分离配置
    List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig0 = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig0.setName("order_0");
        masterSlaveRuleConfig0.setMasterDataSourceName("order0_master");
        masterSlaveRuleConfig0.setSlaveDataSourceNames(Arrays.asList("order0_slave_0", "order0_slave_1"));
        masterSlaveRuleConfig0.setLoadBalanceAlgorithmClassName(RoundRobinMasterSlaveLoadBalanceAlgorithm.class.getName());

        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfig1.setName("order_1");
        masterSlaveRuleConfig1.setMasterDataSourceName("order1_master");
        masterSlaveRuleConfig1.setSlaveDataSourceNames(Arrays.asList("order1_slave_0", "order1_slave_1"));
        masterSlaveRuleConfig1.setLoadBalanceAlgorithmClassName(RoundRobinMasterSlaveLoadBalanceAlgorithm.class.getName());

        return Lists.newArrayList(masterSlaveRuleConfig0, masterSlaveRuleConfig1);
    }

    Map<String, DataSource> createDataSourceMap() throws SQLException {
        Map<String, DataSource> result = new HashMap<>();

//        Map<String, DataSource> slaveDataSourceMap0 = new HashMap<>();
//        slaveDataSourceMap0.put("order0_master", dataSource00());
//        slaveDataSourceMap0.put("order0_slave_0", dataSource01());
//        slaveDataSourceMap0.put("order0_slave_1", dataSource02());
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig0 = new MasterSlaveRuleConfiguration();
//        masterSlaveRuleConfig0.setName("ms0_order0");
//        masterSlaveRuleConfig0.setMasterDataSourceName("order0_master");
//        masterSlaveRuleConfig0.setSlaveDataSourceNames(Arrays.asList("order0_slave_0", "order0_slave_1"));
//        masterSlaveRuleConfig0.setLoadBalanceAlgorithmClassName(MasterSlaveLoadBalanceAlgorithmType.ROUND_ROBIN.name());
//        DataSource masterSlaveDs0 = MasterSlaveDataSourceFactory.createDataSource(slaveDataSourceMap0, masterSlaveRuleConfig0, new ConcurrentHashMap<String, Object>());
//        result.put("order0", masterSlaveDs0);

//        Map<String, DataSource> slaveDataSourceMap1 = new HashMap<>();
//        slaveDataSourceMap1.put("order1_master", dataSource00());
//        slaveDataSourceMap1.put("order1_slave_0", dataSource01());
//        slaveDataSourceMap1.put("order1_slave_1", dataSource02());
//        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration();
//        masterSlaveRuleConfig1.setName("ms1_order1");
//        masterSlaveRuleConfig1.setMasterDataSourceName("order1_master");
//        masterSlaveRuleConfig1.setSlaveDataSourceNames(Arrays.asList("order1_slave_0", "order1_slave_1"));
//        masterSlaveRuleConfig1.setLoadBalanceAlgorithmClassName(MasterSlaveLoadBalanceAlgorithmType.ROUND_ROBIN.name());
//        DataSource masterSlaveDs01 = MasterSlaveDataSourceFactory.createDataSource(slaveDataSourceMap1, masterSlaveRuleConfig1, new ConcurrentHashMap<String, Object>());
//        result.put("order1", masterSlaveDs01);

        result.put("order0_master", dataSource00());
        result.put("order0_slave_0", dataSource01());
        result.put("order0_slave_1", dataSource02());
        result.put("order1_master", dataSource00());
        result.put("order1_slave_0", dataSource01());
        result.put("order1_slave_1", dataSource02());

        return result;
    }

    /**
     * 需要手动配置事务管理器
     *
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "shardingSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean(name = "dbHealthIndicator")
    public HealthIndicator dbHealthIndicator() {

        Map<String, DataSource> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put("order0_master", dataSource00());
        dataSourceMap.put("order0_slave_0", dataSource01());
        dataSourceMap.put("order0_slave_1", dataSource02());
        dataSourceMap.put("order1_master", dataSource00());
        dataSourceMap.put("order1_slave_0", dataSource01());
        dataSourceMap.put("order1_slave_1", dataSource02());
        if (dataSourceMap.size() == 1) {
            return new DataSourceHealthIndicator(dataSourceMap.values().iterator().next());
        } else {
            CompositeHealthIndicator composite = new CompositeHealthIndicator(this.healthAggregator);
            Iterator var3 = dataSourceMap.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, DataSource> entry = (Map.Entry) var3.next();
                composite.addHealthIndicator((String) entry.getKey(), new DataSourceHealthIndicator(entry.getValue()));
            }

            return composite;

        }
    }
}
