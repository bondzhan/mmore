package com.github.mmore.id.properties;

import com.github.mmore.web.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mmore.id")
@Getter
@Setter
public class MmoreIdProperties extends AbstractProperties {

    public static MmoreIdProperties getConfig(){
        return getConfig(MmoreIdProperties.class);
    }


    /**
     * 工作ID
     */
    private long workerId = 1;
}
