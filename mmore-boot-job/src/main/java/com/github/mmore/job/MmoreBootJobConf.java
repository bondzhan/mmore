package com.github.mmore.job;

import com.github.mmore.job.aop.JobLoginContextAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

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
