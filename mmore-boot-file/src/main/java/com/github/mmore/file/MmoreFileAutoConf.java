package com.github.mmore.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.mmore.file.config.OssProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Bond
 * @Date 2025/2/22
 * @Description TODO
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MmoreFileAutoConf {


    @PostConstruct
    public void init() {
        log.info("Mmore-boot-file version " + MmoreFileAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }

    @Bean
    @ConditionalOnMissingBean
    public OssFileClient ossTemplate() {
        return new OssFileClient();
    }

    @Bean
    public OSS ossClient(OssProperties ossProperties) {
        return (new OSSClientBuilder()).build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getSecretAccessKey());
    }
}
