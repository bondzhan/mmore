package com.github.mmore.i18n.advice;

import com.github.mmore.common.model.BizException;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.i18n.service.ErrorMessageResolver;
import com.github.mmore.web.model.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Bond
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class I18nExceptionHandlerAdvice {

    private final ErrorMessageResolver resolver;

    @ExceptionHandler(SystemException.class)
    public ApiResult<Void> handleSystem(SystemException ex) {
        String localized = resolver.resolve(ex.getErrorType());
        return ApiResult.failMsg(ex.getErrorType().getCode(), localized);
    }

    @ExceptionHandler(BizException.class)
    public ApiResult<Void> handleBiz(BizException ex) {
        String localized = resolver.resolve(ex.getErrorType());
        return ApiResult.failMsg(ex.getErrorType().getCode(), localized);
    }
}

