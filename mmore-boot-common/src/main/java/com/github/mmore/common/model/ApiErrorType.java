package com.github.mmore.common.model;

/**
 * @Author: bond
 * @Description 错误类型接口
 * @Date: 2025/1/6 14:20
 */
public interface ApiErrorType {
    /**
     * 返回code
     *
     * @return 错误code
     */
    String getCode();

    /**
     * 返回mesg
     *
     * @return 错误信息
     */
    String getMesg();
}
