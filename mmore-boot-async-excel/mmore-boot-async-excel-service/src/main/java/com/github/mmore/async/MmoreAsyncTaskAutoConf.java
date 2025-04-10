package com.github.mmore.async;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Bond
 * @Date 2025/2/22
 * @Description TODO
 */
@Slf4j
@Configuration
public class MmoreAsyncTaskAutoConf {


    @PostConstruct
    public void init() {
        log.info("mmore-boot-async-excel-service version " + MmoreAsyncTaskAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }
}
