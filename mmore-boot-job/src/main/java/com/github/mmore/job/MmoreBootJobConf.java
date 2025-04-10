package com.github.mmore.job;

import com.github.mmore.job.aop.JobLoginContextAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>@description
 * <p>@date 2025-02-21  11:43
 *
 * @author 蒋昊宇
 */
@Slf4j
@AutoConfiguration
@Import({
        JobLoginContextAspect.class
})
public class MmoreBootJobConf {
    @PostConstruct
    public void init(){
        log.info("Mmore-boot-job version " + MmoreBootJobConf.class.getPackage().getImplementationVersion() + " init success!");

    }
}
