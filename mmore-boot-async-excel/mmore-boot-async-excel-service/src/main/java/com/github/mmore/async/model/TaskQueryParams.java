package com.github.mmore.async.model;

import lombok.Data;

@Data
public class TaskQueryParams {

    /**
     * 任务参数
     */
    private String params;

    private TaskCodeEnum taskCode;

    private String fileUrl;
}
