package com.github.mmore.async.controller;

import com.github.mmore.async.model.AsyncTaskVO;
import com.github.mmore.async.sdk.model.AsyncTaskDto;
import com.github.mmore.async.sdk.model.AsyncTaskQueryDto;
import com.github.mmore.async.service.AsyncTaskService;
import com.github.mmore.common.model.PageResponse;
import com.github.mmore.web.model.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "异步导入导出", description = "异步导入导出")
@RequestMapping("/asyncTask")
@RestController
@ConditionalOnProperty(prefix = "oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncTaskController {

    @Resource
    private AsyncTaskService asyncTaskService;

    @Operation(summary = "导入")
    @PostMapping("/asyncImport")
    public ApiResult<Void> asyncImport(@RequestBody @Validated AsyncTaskVO asyncTaskVO) {
        asyncTaskService.importTask(asyncTaskVO);
        return ApiResult.success();
    }

    @Operation(summary = "导出")
    @PostMapping("/exportTask")
    public ApiResult<Void> exportTask(@RequestBody @Validated AsyncTaskVO asyncTaskVO) {
        asyncTaskService.exportTask(asyncTaskVO);
        return ApiResult.success();
    }

    @Operation(summary = "查询列表")
    @PostMapping("/selectPage")
    public ApiResult<PageResponse<AsyncTaskDto>> selectPage(@RequestBody @Validated AsyncTaskQueryDto asyncTaskQueryDto) {
        return ApiResult.success(asyncTaskService.selectPage(asyncTaskQueryDto));
    }
}
