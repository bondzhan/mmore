package com.github.mmore.web.autoconfigure;

import com.github.mmore.web.model.SwaggerInfo;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@EnableConfigurationProperties(SwaggerInfo.class)
public class SwaggerAutoConf {

    @Resource
    private SwaggerInfo swaggerInfo;

    /**
     * Swagger配置对象初使化
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(swaggerInfo.getTitle())
                        .description(swaggerInfo.getDescription())
                        .version(swaggerInfo.getVersion())
                        .license(new License().name(swaggerInfo.getLicense())
                                .url(swaggerInfo.getUrl())))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerInfo.getWikiDocumentation())
                        .url(swaggerInfo.getWikiUrl()));
    }
}
