package com.github.mmore.cache.autoconfigure;

import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JetCacheTemplate {
    private final CacheManager cacheManager;
    private final Map<String, Map<String, Cache>> caches;

    public JetCacheTemplate(CacheManager cacheManager, Map<String, Map<String, Cache>> caches) {
        this.cacheManager = cacheManager;
        this.caches = caches;
    }

    private <K, V> Cache<K, V> getCache(String area, String cacheName) {
        if (StrUtil.isBlank(area)) {
            area = "default";
            log.debug("Area is blank, using default area: default");
        }
        if (StrUtil.isBlank(cacheName)) {
            log.warn("CacheName is blank, cannot get cache.");
            return null;
        }

        Map<String, Cache> cacheMap = caches.get(area);
        if (cacheMap == null) {
            log.warn("Area [{}] not found.", area);
            return null;
        }
        Cache<K, V> cache = (Cache<K, V>) cacheMap.get(cacheName);
        if (cache == null) {
            log.warn("Cache [{}] in area [{}] not found.", cacheName, area);
            return null;
        }
        return cache;
    }

    private <K, V> Cache<K, V> getCache(String cacheName) {
        return getCache(null, cacheName);
    }

    public <K, V> V get(String cacheName, K key) {
        return get(null, cacheName, key);
    }

    public <K, V> V get(String area, String cacheName, K key) {
        Cache<K, V> cache = getCache(area, cacheName);
        return cache != null ? cache.get(key) : null;
    }

    public <K, V> void put(String cacheName, K key, V value) {
        getCache(cacheName).put(key, value);
    }

    public <K, V> void put(String area, String cacheName, K key, V value) {
        getCache(area, cacheName).put(key, value);
    }

    public <K, V> void put(String cacheName, K key, V value, long expire, TimeUnit unit) {
        getCache(cacheName).put(key, value, expire, unit);
    }

    public <K, V> void put(String area, String cacheName, K key, V value, long expire, TimeUnit unit) {
        getCache(area, cacheName).put(key, value, expire, unit);
    }

    public <K, V> void remove(String cacheName, K key) {
        getCache(cacheName).remove(key);
    }

    public <K, V> void remove(String area, String cacheName, K key) {
        getCache(area, cacheName).remove(key);
    }

    public <K, V> void putAll(String cacheName, Map<? extends K, ? extends V> map) {
        getCache(cacheName).putAll(map);
    }

    public <K, V> void putAll(String area, String cacheName, Map<? extends K, ? extends V> map) {
        getCache(area, cacheName).putAll(map);
    }

    public <K, V> void putAll(String cacheName, Map<? extends K, ? extends V> map, long expire, TimeUnit unit) {
        getCache(cacheName).putAll(map, expire, unit);
    }

    public <K, V> void putAll(String area, String cacheName, Map<? extends K, ? extends V> map, long expire, TimeUnit unit) {
        getCache(area, cacheName).putAll(map, expire, unit);
    }

}
