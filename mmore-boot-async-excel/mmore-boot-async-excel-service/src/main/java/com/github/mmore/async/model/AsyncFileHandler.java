package com.github.mmore.async.model;


import lombok.Data;

@Data
public class AsyncFileHandler {

    /**
     * 任务类型 0：导入 1：导出
     */
    private Integer handlerType;

    /**
     * 业务名称
     */
    private String bizCode;

    /**
     * 文件地址, oss文件地址
     */
    private String fileUrl;

    /**
     * 扩展信息
     */
    private String feature;
}
