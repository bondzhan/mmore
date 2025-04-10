package com.github.mmore.common.util;

import com.github.mmore.common.model.ApiErrorType;
import com.github.mmore.common.model.SubSystemException;
import com.github.mmore.common.model.SystemException;

/**
 * @author zhj
 * @date 2025/2/8
 */
public class AssertUtil {

    /**
     * 断言 assertTrue
     *
     * @param condition 必须为true，否则抛出异常
     */
    public static void that(boolean condition, String code, String mesg) {
        if (!condition) {
            throw new SubSystemException(code, mesg);
        }
    }

    /**
     * 断言 assertTrue
     *
     * @param condition 必须为true，否则抛出异常
     */
    public static void that(boolean condition, ApiErrorType apiErrorType) {
        if (!condition) {
            throw new SystemException(apiErrorType);
        }
    }

    /**
     * 断言 assertTrue
     *
     * @param condition 必须为true，否则抛出异常
     */
    public static void that(boolean condition, ApiErrorType errorType, String message) {
        if (!condition) {
            throw new SystemException(errorType, message);
        }
    }

    /**
     * 断言 assertTrue
     *
     * @param condition 必须为true，否则抛出异常
     */
    public static void that(boolean condition, ApiErrorType errorType, String message, Throwable cause) {
        if (!condition) {
            throw new SystemException(errorType, message, cause);
        }
    }

}
