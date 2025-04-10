package com.github.mmore.async.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AsyncTaskData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务参数
     */
    private String params;

    /**
     * 数据列表
     */
    private List<T> asyncTaskDtos;
}
