package com.github.mmore.cache.autoconfigure;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.github.mmore.cache.model.CacheNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
public class JetCacheManagerAutoConf {
    private final CacheManager cacheManager;

    public JetCacheManagerAutoConf(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Bean
    public JetCacheTemplate jetCacheTemplate(Map<String, Map<String, Cache>> caches) {
        return new JetCacheTemplate(cacheManager, caches);
    }

    @Bean
    public Map<String, Map<String, Cache>> caches() {
        log.info("JetCache Default area to start config");
        Map<String, Map<String, Cache>> caches = new HashMap<>();
        // 默认先定义一个area，硬编码需要创建的cache
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.USER_CACHE);
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.COMMON_CACHE);
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.PRODUCT_CACHE);
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.ORDER_CACHE);
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.SETTLE_CACHE);
        createCache(caches, CacheNameConstant.DEFAULT_AREA, CacheNameConstant.MASSAGE_CACHE);
        return caches;
    }

    private void createCache(Map<String, Map<String, Cache>> caches, String area, String cacheName) {
        Map<String, Cache> areaCaches = caches.computeIfAbsent(area, k -> new HashMap<>());
        QuickConfig qc = QuickConfig.newBuilder(cacheName)
                .cacheType(CacheType.REMOTE)
                .build(); // 使用默认过期时间
        areaCaches.put(cacheName, cacheManager.getOrCreateCache(qc));
        log.info("JetCache has CacheName [{}] in [{}] area", cacheName, area);
    }
}
