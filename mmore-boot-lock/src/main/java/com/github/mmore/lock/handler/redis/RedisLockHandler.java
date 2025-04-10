package com.github.mmore.lock.handler.redis;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.github.mmore.cache.model.CacheNameConstant;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.lock.handler.LockHandler;
import com.github.mmore.lock.handler.LockType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class RedisLockHandler implements LockHandler {

    @Resource
    @Lazy
    private CacheManager cacheManager;

    @PostConstruct
    public void init() {
        log.info("初始化redis加锁");
    }

    @Override
    public <T> T tryLock(String keyPrefix, String lockKey, long expire, TimeUnit timeUnit, Function<String, T> func) {
        log.info("使用redis开始加锁, key={}", lockKey);

        Cache<String, Long> cache = cacheManager.getCache(CacheNameConstant.COMMON_CACHE);
        String key = getKey(keyPrefix, lockKey);
        try (AutoReleaseLock autoLock = cache.tryLock(key, expire, timeUnit)) {
            if (autoLock != null) {
                return func.apply(key);
            } else {
                throw new SystemException(SystemErrorType.SYSTEM_ERROR, "获取加锁失败,稍后再试");
            }
        } catch (Exception e) {
            log.error("加锁异常", e);
            throw e;
        }
    }

    @Override
    public <R> R tryLock(String keyPrefix, List<String> keys ,long expire, TimeUnit timeUnit, Function<String, R> func) {
        log.info("使用redis开始加锁, keys={}", String.join(", ", keys));

        Cache<String, Long> cache = cacheManager.getCache(CacheNameConstant.COMMON_CACHE);
        List<AutoReleaseLock> locks = new ArrayList<>();

        try {
            for (String key : keys) {
                String keyNew = getKey(keyPrefix, key);
                AutoReleaseLock lock = cache.tryLock(keyNew, expire, timeUnit);
                if (lock != null) {
                    log.info("获取加锁成功, key={}", keyNew);
                    locks.add(lock);
                } else {
                    throw new SystemException(SystemErrorType.SYSTEM_ERROR, "获取不到锁, key=" + keyNew + ", 稍后再试");
                }
            }
            return func.apply("");
        } finally {
            locks.forEach(AutoReleaseLock::close);
        }
    }

    private String getKey(String keyPrefix, String lockKey) {
        if (StringUtils.isBlank(keyPrefix)) {
            return lockKey;
        }
        return keyPrefix + lockKey;
    }

    @Override
    public boolean getLock(LockType type) {
        return LockType.REDIS.equals(type);
    }
}
