package com.github.mmore.cache;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author Bond
 * @Date 2025/1/18
 * @Description TODO
 */
@Configuration
@EnableMethodCache(basePackages = "com.github.mmore")
@AutoConfiguration
@AutoConfigureAfter(JetCacheAutoConfiguration.class)
@Import(JetCacheAutoConfiguration.class)
@Slf4j
public class MmoreBootCacheConf {

    @PostConstruct
    public void init() {
        log.info("Mmore-boot-cache version " + MmoreBootCacheConf.class.getPackage().getImplementationVersion() + " init success!");
    }
}
