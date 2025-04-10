package com.github.mmore.eda;

import com.github.mmore.eda.autoconfigure.RabbitMqAutoCnf;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Author Bond
 * @Date 2025/2/22
 * @Description TODO
 */
@Slf4j
@Import({
        RabbitMqAutoCnf.class
})
@AutoConfiguration
public class EdaAutoConf {


    @PostConstruct
    public void init() {
        log.info("Youforest-cloud-eda version " + EdaAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }
}
