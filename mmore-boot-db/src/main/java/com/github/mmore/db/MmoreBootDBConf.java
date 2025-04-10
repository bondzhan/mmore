package com.github.mmore.db;

import com.github.mmore.db.autoconfigure.CustomInterceptorDbAutoConf;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Author Bond
 * @Date 2025/1/8
 * @Description TODO
 */
@Slf4j
@AutoConfiguration
@Import({
        CustomInterceptorDbAutoConf.class
})
public class MmoreBootDBConf {

    @PostConstruct
    public void init(){
        log.info("Mmore-boot-db version " + MmoreBootDBConf.class.getPackage().getImplementationVersion() + " init success!");

    }
}
