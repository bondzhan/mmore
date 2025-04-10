package com.github.mmore.web.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmore.swagger")
public class SwaggerInfo {
    private String version;
    private String title;
    private String description;
    private String url;
    private String license;
    private String wikiUrl;
    private String wikiDocumentation;
}
