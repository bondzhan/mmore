package com.github.mmore.web.autoconfigure;

import com.github.mmore.web.interceptor.LoginContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description: web配置
 *
 * @Author lucoo
 * @Date 2021/6/23 18:16
 */
@Slf4j
@Configuration
public class CustomWebMvcConf implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册
        registry.addInterceptor(new LoginContextHandler())
                //匹配所有路径
                .addPathPatterns("/**")
                //不需要匹配的路径单独加
                .excludePathPatterns(
                        "/**/doc.html",
                        "/**/actuator/**",
                        "/**/swagger-ui.html",
                        "/**/favicon.ico",
                        "/**/webjars/**",
                        "/**/api-docs/**"
//                        "/**/user/login",
//                        "/**/userIdentity/queryByUserId"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html", "doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}