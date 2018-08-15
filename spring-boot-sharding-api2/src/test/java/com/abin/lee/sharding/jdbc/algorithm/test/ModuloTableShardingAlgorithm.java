package com.abin.lee.sharding.jdbc.algorithm.test;

import io.shardingjdbc.core.api.algorithm.sharding.ListShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abin on 2018/8/15.
 */
public final class ModuloTableShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    private static final int TABLE_COUNT = 2;

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<io.shardingjdbc.core.api.algorithm.sharding.ShardingValue> shardingValues) {
        Collection<Long> userIdValues = getShardingValue(shardingValues, "user_id");
        Collection<Long> idValues = getShardingValue(shardingValues, "order_id");
        Collection<Long> values = (userIdValues != null && userIdValues.size() > 0) ? userIdValues : idValues;
        List<String> shardingSuffix = new ArrayList<>();
        if (values != null && values.size() > 0) {
            for (Long val : values) {
                Long shardingKey = Long.parseLong(val.toString());
                // 后五位后三位两位分表
                String suffix = "_" + shardingKey % TABLE_COUNT + "";
                availableTargetNames.forEach(x -> {
                    if (x.endsWith(suffix)) {
                        shardingSuffix.add(x);
                    }
                });
            }
        }

        return shardingSuffix;
    }

    private Collection<Long> getShardingValue(Collection<io.shardingjdbc.core.api.algorithm.sharding.ShardingValue> shardingValues, final String key) {
        Collection<Long> valueSet = new ArrayList<>();
        Iterator<io.shardingjdbc.core.api.algorithm.sharding.ShardingValue> iterator = shardingValues.iterator();
        while (iterator.hasNext()) {
            io.shardingjdbc.core.api.algorithm.sharding.ShardingValue next = iterator.next();
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
