package com.abin.lee.sharding.jdbc.conf.algorithm;


import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * 订单分表算法
 *

 */
public class OrderIdShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    private static final int TABLE_COUNT = 4;


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long shardingKey = Long.parseLong(shardingValue.getValue().toString());
        long ext = shardingKey % TABLE_COUNT;
        for (String name : availableTargetNames) {
            if (name.endsWith("_" + ext)) {
                System.out.println("---------------------------------table--------------------------- :" + name);
                return name;
            }
        }
        return null;
    }
}
