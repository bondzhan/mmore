package com.github.mmore.async.sdk.api;

import com.github.mmore.async.sdk.model.AsyncTaskDto;
import com.github.mmore.async.sdk.model.AsyncTaskQueryDto;
import com.github.mmore.common.model.PageResponse;
import com.github.mmore.web.model.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异步持久化实现接口
 */
public interface AsyncTaskPersistenceApi {

    @RequestMapping("/save")
    ApiResult<AsyncTaskDto> save(AsyncTaskDto asyncTaskDto);

    @RequestMapping("/update")
    ApiResult<AsyncTaskDto> update(AsyncTaskDto asyncTaskDto);

    @RequestMapping("/selectPage")
    ApiResult<PageResponse<AsyncTaskDto>> selectPage(AsyncTaskQueryDto queryDto);
}
