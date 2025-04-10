package com.github.mmore.async.sdk.model;

import com.github.mmore.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AsyncTaskQueryDto {

    @Schema(description = "分页参数")
    private PageParam pageParam;

    @Schema(description = "业务编码")
    @NotEmpty(message = "业务编码不能为空")
    private String bizCode;
}
