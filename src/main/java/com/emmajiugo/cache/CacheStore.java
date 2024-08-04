package com.emmajiugo.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public class CacheStore<T> {
    private final Cache<String, T> cache;

    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        cache = Caffeine.newBuilder()
                .expireAfterAccess(expiryDuration, timeUnit)
                .build();
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void add(String key, T value) {
        if (StringUtils.isNotBlank(key) && null != value) {
            cache.put(key, value);
        }
    }

    public void clearCache() {
        cache.invalidateAll();
    }
}
