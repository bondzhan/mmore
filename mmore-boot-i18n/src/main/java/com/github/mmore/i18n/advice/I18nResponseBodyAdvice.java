package com.github.mmore.i18n.advice;

import com.github.mmore.i18n.service.I18nDictResolver;
import com.github.mmore.web.model.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class I18nResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final I18nDictResolver dictResolver;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResult<?> ar) {
            Object data = ar.getData();
            if (data != null) {
                dictResolver.translate(data);
            }
            return body;
        }
        dictResolver.translate(body);
        return body;
    }
}

