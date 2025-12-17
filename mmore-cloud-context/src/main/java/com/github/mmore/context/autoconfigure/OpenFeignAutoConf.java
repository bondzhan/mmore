package com.github.mmore.context.autoconfigure;

import com.github.mmore.context.interceptor.RouteServiceInstanceSupplier;
import com.github.mmore.context.interceptor.RequestContextInterceptor;
import feign.RequestInterceptor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignAutoConf {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return  new RequestContextInterceptor();
    }

    @Bean
    public RouteServiceInstanceSupplier routeServiceInstanceSupplier(DiscoveryClient discoveryClient) {
        return new RouteServiceInstanceSupplier(discoveryClient);
    }
}
