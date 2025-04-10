package com.github.mmore.lock.handler;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface LockHandler {

    <R> R tryLock(String keyPrefix, String lockKey, long expire, TimeUnit timeUnit, Function<String, R> func);

    <R> R tryLock(String keyPrefix, List<String> keys, long expire, TimeUnit timeUnit, Function<String, R> func);

    boolean getLock(LockType type);
}
