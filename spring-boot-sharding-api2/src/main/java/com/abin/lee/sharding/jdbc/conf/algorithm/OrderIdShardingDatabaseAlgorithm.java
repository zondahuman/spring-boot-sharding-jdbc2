package com.abin.lee.sharding.jdbc.conf.algorithm;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * 订单ID分库算法
 *
 * @author syniu
 * @version OrderBaseShardingTableAlgorithm.java, 2018/5/17 17:02
 */
public class OrderIdShardingDatabaseAlgorithm implements PreciseShardingAlgorithm<Long> {

    private static final int DB_COUNT = 2;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long shardingKey = Long.parseLong(shardingValue.getValue().toString());
        long ext = shardingKey % DB_COUNT;
        for (String name : availableTargetNames) {
            if (name.endsWith("_" + ext)) {
                return name;
            }
        }
        return null;
    }
}
