package com.github.mmore.common.model;

import lombok.Getter;

/**
 * 业务异常
 *
 */
@Getter
public class BizException extends RuntimeException {

    private final ApiErrorType errorType;

    public BizException() {
        this.errorType = BizErrorType.COMMON;
    }

    public BizException(ApiErrorType errorType) {
        super(errorType.getMesg());
        this.errorType = errorType;
    }

    public BizException(String message) {
        super(message);
        this.errorType = BizErrorType.COMMON;
    }

    public BizException(ApiErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BizException(ApiErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

}