package com.github.mmore.async.model;

import java.util.Arrays;
import java.util.Objects;

public enum TaskCodeEnum implements TaskCode {

    QUOTE_IMPORT("quote_import", IMPORT, "导入报价单"),
    QUOTE_EXPORT("quote_export", EXPORT,"导出报价单"),

    SELLER_BATCH_EXPORT("seller_batch_export", EXPORT,"中央库存导出"),

    SELLER_BATCH_LOCK("seller_batch_lock", IMPORT,"批量锁库"),

    CHANNEL_ORDER_IMPORT("channel_order_import", IMPORT,"导入渠道订单"),
    ;

    private final String code;

    /**
     * 任务类型 0：导入 1：导出
     */
    private final Byte taskType;

    private final String desc;

    TaskCodeEnum(String code, Byte taskType, String desc) {
        this.code = code;
        this.taskType = taskType;
        this.desc = desc;
    }
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Byte getTaskType() {
        return taskType;
    }

    @Override
    public String getMessage() {
        return desc;
    }

    public static TaskCodeEnum getByCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        } else {
            TaskCodeEnum[] values = values();
            return Arrays.stream(values).filter(v->v.getCode().equals(code)).findFirst().orElse(null);
        }
    }
}
