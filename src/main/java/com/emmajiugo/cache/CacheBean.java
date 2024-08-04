package com.emmajiugo.cache;

import com.emmajiugo.entity.Customer;
import com.emmajiugo.entity.Rule;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheBean {
    public static CacheStore<List<Rule>> ruleCache() {
        return new CacheStore<>(6, TimeUnit.HOURS);
    }

    public static CacheStore<Customer> customerCache() {
        return new CacheStore<>(6, TimeUnit.HOURS);
    }
}
