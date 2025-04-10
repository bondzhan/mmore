package com.github.mmore.context.interceptor;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.web.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Bond
 * @Date 2025/1/23
 * @Description TODO
 */
@Slf4j
@RestControllerAdvice
@Order(100)
public class FeignExceptionHandlerAdvice {
    @ExceptionHandler(BlockException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiResult blockException(BlockException e) {
        log.error("block exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }

    @ExceptionHandler(FlowException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiResult flowException(FlowException e) {
        log.error("flow exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }

    @ExceptionHandler(DegradeException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiResult degradeException(DegradeException e) {
        log.error("degrade exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }

    @ExceptionHandler(ParamFlowException.class)
    public ApiResult paramFlowException(ParamFlowException e) {
        log.error("param flow exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }

    @ExceptionHandler(SystemBlockException.class)
    public ApiResult systemBlockException(SystemBlockException e) {
        log.error("system block exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }

    @ExceptionHandler(AuthorityException.class)
    public ApiResult authorityException(AuthorityException e) {
        log.error("authority exception:{}", e.getRule());
        return ApiResult.fail(SystemErrorType.SYSTEM_BUSY);
    }
}
