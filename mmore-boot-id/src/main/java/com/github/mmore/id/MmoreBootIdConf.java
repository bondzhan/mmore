package com.github.mmore.id;

import com.github.mmore.id.properties.MmoreIdProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * <p>@description
 * <p>@date 2025-02-18  10:45
 *
 * @author 蒋昊宇
 */
@Slf4j
@AutoConfiguration
@Import({
        MmoreIdProperties.class,
})
public class MmoreBootIdConf {

    @PostConstruct
    public void init(){
        log.info("Mmore-boot-id version " + MmoreBootIdConf.class.getPackage().getImplementationVersion() + " init success!");

    }
}
