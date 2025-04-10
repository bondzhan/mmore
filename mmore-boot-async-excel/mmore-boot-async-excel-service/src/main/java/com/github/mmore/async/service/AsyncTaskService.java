package com.github.mmore.async.service;

import com.github.mmore.async.model.AsyncTaskVO;
import com.github.mmore.async.sdk.model.AsyncTaskDto;
import com.github.mmore.async.sdk.model.AsyncTaskQueryDto;
import com.github.mmore.common.model.PageResponse;

public interface AsyncTaskService {

    void importTask(AsyncTaskVO asyncTaskVO);

    void exportTask(AsyncTaskVO asyncTaskVO);

    PageResponse<AsyncTaskDto> selectPage(AsyncTaskQueryDto asyncTaskQueryDto);
}
