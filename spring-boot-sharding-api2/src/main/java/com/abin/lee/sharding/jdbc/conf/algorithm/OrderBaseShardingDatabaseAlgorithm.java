package com.abin.lee.sharding.jdbc.conf.algorithm;

import io.shardingjdbc.core.api.algorithm.sharding.ListShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.ShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 订单分库算法
 */
public class OrderBaseShardingDatabaseAlgorithm implements ComplexKeysShardingAlgorithm {

    private static final int DB_COUNT = 2;


    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {
        Collection<Long> userIdValues = getShardingValue(shardingValues, "user_id");
        Collection<Long> idValues = getShardingValue(shardingValues, "id");
        Collection<Long> values = (userIdValues != null && userIdValues.size() > 0) ? userIdValues : idValues;
        List<String> shardingSuffix = new ArrayList<>();
        if (values != null && values.size() > 0) {
            for (Long val : values) {
                Long shardingKey = Long.parseLong(val.toString());
                String suffix = "_" + shardingKey % DB_COUNT + "";
                availableTargetNames.forEach(x -> {
                    if (x.endsWith(suffix)) {
                        shardingSuffix.add(x);
                    }
                });
            }
        }
        System.out.println("---------------------------------db--------------------------- :" + shardingSuffix);
        return shardingSuffix;
    }

    private Collection<Long> getShardingValue(Collection<ShardingValue> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Iterator<ShardingValue> iterator = shardingValues.iterator();
        while (iterator.hasNext()) {
            ShardingValue next = iterator.next();
            if (next instanceof ListShardingValue) {
                ListShardingValue value = (ListShardingValue) next;
                if (value.getColumnName().equals(key)) {
                    return value.getValues();
                }
            }
        }
        return valueSet;
    }

}
