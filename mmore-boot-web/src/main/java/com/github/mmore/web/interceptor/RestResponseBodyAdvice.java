package com.github.mmore.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mmore.web.model.ApiResult;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@RestControllerAdvice
public class RestResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NotNull MethodParameter returnType,
                            @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> returnTypeContainingClass = returnType.getContainingClass();
        // 排除swagger的资源
        boolean hasSwaggerResource = !returnTypeContainingClass.getName().contains("org.springdoc");
        boolean hasResponseBody = AnnotatedElementUtils.hasAnnotation(returnTypeContainingClass, ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class);
        boolean hasRestController = AnnotatedElementUtils.hasAnnotation(returnTypeContainingClass, RestController.class) ||
                returnType.hasMethodAnnotation(RestController.class);
        return hasResponseBody && hasRestController && hasSwaggerResource;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  @NotNull ServerHttpResponse response) {
        // body为ApiResult类型，直接返回
        if (body instanceof ApiResult) {
            return body;
        }
        // 如果是String，将Result转为json string
        if (Objects.isNull(body) || body instanceof String) {
            return new ObjectMapper().writeValueAsString(ApiResult.success(body));
        }
        return ApiResult.success(body);
    }
}