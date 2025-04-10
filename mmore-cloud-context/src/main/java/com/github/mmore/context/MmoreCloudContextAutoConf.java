package com.github.mmore.context;

import com.github.mmore.context.autoconfigure.OpenFeignAutoConf;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Author Bond
 * @Date 2025/1/23
 * @Description TODO
 */
@Slf4j
@Import({
        OpenFeignAutoConf.class
})
@AutoConfiguration
public class MmoreCloudContextAutoConf {

    @PostConstruct
    public void init() {
        log.info("Mmore-cloud-context version " + MmoreCloudContextAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }

}
