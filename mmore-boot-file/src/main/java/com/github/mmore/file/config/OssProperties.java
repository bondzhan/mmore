package com.github.mmore.file.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private String internetFileUrl;

    /**
     * 文件最大上传大小
     */
    private Long maxSize = 10L;

    public Long getMaxSizeMb() {
        return maxSize * 1024 * 1024L;
    }
}
