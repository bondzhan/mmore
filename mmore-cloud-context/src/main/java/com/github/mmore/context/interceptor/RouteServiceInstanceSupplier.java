package com.github.mmore.context.interceptor;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

public class RouteServiceInstanceSupplier implements ServiceInstanceListSupplier {

    private final String HEADER_KEY = "route";

    private final DiscoveryClient discoveryClient;

    private Request request;

    public RouteServiceInstanceSupplier(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.defer(() -> {
            String route = getRouteFromRequest();
            String authority = ((RequestDataContext) request.getContext()).getClientRequest().getUrl().getAuthority();
            List<ServiceInstance> instances = discoveryClient.getInstances(authority);
            List<ServiceInstance> filtered;
            if (route != null) {
                // 使用 route 过滤服务实例
                filtered = instances.stream()
                        .filter(instance -> route.equals(instance.getMetadata().get(HEADER_KEY)))
                        .collect(Collectors.toList());
                if (filtered.isEmpty()) {
                    // 如果没有匹配的实例，则使用默认负载均衡策略
                    return Flux.just(instances);
                }
            } else {
                // 使用默认负载均衡策略, 过滤掉带有 route 的服务实例
                filtered = instances.stream()
                        .filter(instance -> instance.getMetadata().get(HEADER_KEY) == null)
                        .toList();
            }
            return Flux.just(filtered);
        });
    }

    private String getRouteFromRequest() {
        if (request == null || request.getContext() == null) {
            return null;
        }
        RequestDataContext context = (RequestDataContext) request.getContext();
        return context.getClientRequest().getHeaders().getFirst(HEADER_KEY);
    }

    @Override
    public String getServiceId() {
        return "";
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        this.request = request;
        return get();
    }
}