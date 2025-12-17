package com.github.mmore.common.model;

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
