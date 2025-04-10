package com.github.mmore.async.sdk.model;

import lombok.Getter;

public enum AsyncTaskStatusEnum {

    WAITING((byte) 0, "待处理"),
    PROCESSING((byte) 1, "处理中"),
    SUCCESS((byte) 2, "处理成功"),
    FAIL((byte) 3, "处理失败");

    @Getter
    private final Byte code;

    private final String desc;

    AsyncTaskStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
