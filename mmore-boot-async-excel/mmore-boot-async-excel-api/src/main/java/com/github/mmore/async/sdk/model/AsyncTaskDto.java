package com.github.mmore.async.sdk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AsyncTaskDto {

    private Long asyncTaskId;

    /**
     * 任务类型 0：导入 1：导出
     */
    @Schema(description = "任务类型 0：导入 1：导出")
    private Byte taskType;

    /**
     * 任务名称
     */
    @Schema(description = "任务名称")
    private String taskName;

    /**
     * 业务编码, 用于区分业务
     */
    @Schema(description = "业务编码, 用于区分业务")
    private String bizCode;

    /**
     * 业务参数
     */
    @Schema(description = "业务参数")
    private String params;

    /**
     * 任务状态 0：待处理 1：处理中 2:已处理  3:处理异常
     * @see AsyncTaskStatusEnum
     */
    @Schema(description = "任务状态 0：待处理 1：处理中 2:已处理  3:处理异常 ")
    private Byte status;

    /**
     * 导入文件地址
     */
    @Schema(description = "导入文件地址")
    private String importFileUrl;

    /**
     * 结果文件地址
     */
    @Schema(description = "结果文件地址")
    private String resultFileUrl;

    /**
     * 成功数量
     */
    @Schema(description = "成功数量")
    private Integer successCount;

    /**
     * 失败数量
     */
    @Schema(description = "失败数量")
    private Integer failCount;

    /**
     * 总数量
     */
    @Schema(description = "总数量")
    private Integer totalCount;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String feature;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private Long createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
