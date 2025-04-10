package com.github.mmore.async.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AsyncTaskVO {

    /**
     * 任务参数
     */
    @Schema(description = "导入查询参数")
    private String params;


    /**
     * @see TaskCodeEnum
     */
    @Schema(description = "业务编码")
    @NotEmpty(message = "业务编码不能为空")
    private String bizCode;

    /**
     * 任务类型 0：导入 1：导出
     */
//    @Schema(description = "任务类型 0：导入 1：导出")
//    @NotNull(message = "任务类型不能为空")
//    private Byte taskType;

    /**
     * 导入文件地址
     */
    @Schema(description = "导入地址")
    private String fileUrl;
}
