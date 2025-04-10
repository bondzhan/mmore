package com.github.mmore.lock.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的key前缀
     *
     * @return
     */
    String keyPrefix() default "";

    /**
     * 锁的key
     *
     * @return
     */
    String key() default "";

    /**
     * 锁的key
     *       1、支持固定可以如：@DistributedLock(keys = {"brand:1“, "brand:2"})
     *       2、支持固定+EL表达式，如：@DistributedLock(keys = {"brand: + {#brandIds[0]}", "{#brandIds[1]}"})
     *       3、支持SpEL表达式，如：@DistributedLock(keys = {"{#brandIds[0]}", "{#brandIds[1]}"})
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 过期时间
     *
     * @return
     */
    long expire() default 60;


    /**
     * 续约，目前还不支持，请根据业务自动调整过期时间
     *
     * @return
     */
//    long leaseTime() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    LockType lockType() default LockType.REDIS;
}