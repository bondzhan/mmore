package com.github.mmore.lock;

import com.github.mmore.lock.aop.DistributedLockAspect;
import com.github.mmore.lock.handler.redis.RedisLockHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MmoreLockAutoConf {


    @PostConstruct
    public void init() {
        log.info("Mmore-boot-lock version " + MmoreLockAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }

    @Bean
    public RedisLockHandler redisLockHandler(){
        return new RedisLockHandler();
    }

    @Bean
    public DistributedLockAspect distributedLockAspect(){
        return new DistributedLockAspect();
    }
}
