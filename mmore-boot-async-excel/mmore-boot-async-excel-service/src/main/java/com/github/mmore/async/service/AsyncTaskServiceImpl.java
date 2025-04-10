package com.github.mmore.async.service;

import com.github.mmore.async.handler.AsyncBaseHandler;
import com.github.mmore.async.model.AsyncTaskVO;
import com.github.mmore.async.model.TaskCodeEnum;
import com.github.mmore.async.model.TaskQueryParams;
import com.github.mmore.async.util.TracingExecutors;
import com.github.mmore.async.sdk.api.AsyncTaskPersistenceApi;
import com.github.mmore.async.sdk.model.AsyncTaskDto;
import com.github.mmore.async.sdk.model.AsyncTaskQueryDto;
import com.github.mmore.async.sdk.model.AsyncTaskStatusEnum;
import com.github.mmore.common.model.BizException;
import com.github.mmore.common.model.PageResponse;
import com.github.mmore.web.model.ApiResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AsyncTaskServiceImpl implements AsyncTaskService {

    @Resource
    private AsyncTaskPersistenceApi asyncTaskApi;

    @Resource
    private AsyncBaseHandler asyncBaseHandler;

    @Resource
    private TracingExecutors tracingExecutor;

    @Override
    public void importTask(AsyncTaskVO asyncTaskVO) {
        TaskCodeEnum taskCodeEnum = getTaskCodeEnum(asyncTaskVO);

        AsyncTaskDto asyncTaskDto = builderDto(asyncTaskVO, taskCodeEnum);

        ApiResult<AsyncTaskDto> save = asyncTaskApi.save(asyncTaskDto);
        if (save.isSuccess()) {
            TaskQueryParams taskQueryParams = new TaskQueryParams();
            taskQueryParams.setTaskCode(taskCodeEnum);
            taskQueryParams.setFileUrl(asyncTaskVO.getFileUrl());
            taskQueryParams.setParams(asyncTaskVO.getParams());

            tracingExecutor.execute(() -> asyncBaseHandler.handler(save.getData(), taskQueryParams));
        } else {
            throw new BizException("保存任务失败");
        }
    }

    private AsyncTaskDto builderDto(AsyncTaskVO asyncTaskVO, TaskCodeEnum taskCodeEnum) {
        AsyncTaskDto asyncTaskDto = new AsyncTaskDto();
        asyncTaskDto.setBizCode(asyncTaskVO.getBizCode());
        asyncTaskDto.setTaskType(taskCodeEnum.getTaskType());
        asyncTaskDto.setTaskName(taskCodeEnum.getMessage());
        asyncTaskDto.setStatus(AsyncTaskStatusEnum.WAITING.getCode());
        asyncTaskDto.setParams(asyncTaskVO.getParams());
        asyncTaskDto.setFeature("");
        asyncTaskDto.setImportFileUrl(asyncTaskVO.getFileUrl());
        asyncTaskDto.setResultFileUrl("");
        asyncTaskDto.setSuccessCount(0);
        asyncTaskDto.setFailCount(0);
        asyncTaskDto.setTotalCount(0);
        return asyncTaskDto;
    }

    private TaskCodeEnum getTaskCodeEnum(AsyncTaskVO asyncTaskVO) {
        TaskCodeEnum taskCodeEnum = TaskCodeEnum.getByCode(asyncTaskVO.getBizCode());
        if (taskCodeEnum == null) {
            throw new BizException("任务类型不存在" + asyncTaskVO.getBizCode());
        }
        return taskCodeEnum;
    }

    @Override
    public void exportTask(AsyncTaskVO asyncTaskVO) {
        TaskCodeEnum taskCodeEnum = getTaskCodeEnum(asyncTaskVO);

        AsyncTaskDto asyncTaskDto = builderDto(asyncTaskVO, taskCodeEnum);

        ApiResult<AsyncTaskDto> save = asyncTaskApi.save(asyncTaskDto);

        if (save.isSuccess()) {
            TaskQueryParams taskQueryParams = new TaskQueryParams();
            taskQueryParams.setTaskCode(taskCodeEnum);
            taskQueryParams.setFileUrl(asyncTaskVO.getFileUrl());
            taskQueryParams.setParams(asyncTaskVO.getParams());

            tracingExecutor.execute(() -> asyncBaseHandler.handler(save.getData(), taskQueryParams));
        } else {
            throw new BizException("保存任务失败");
        }

    }

    @Override
    public PageResponse<AsyncTaskDto> selectPage(AsyncTaskQueryDto asyncTaskQueryDto) {
        ApiResult<PageResponse<AsyncTaskDto>> pageResponseApiResult = asyncTaskApi.selectPage(asyncTaskQueryDto);
        if (pageResponseApiResult.isSuccess()) {
            return pageResponseApiResult.getData();
        } else  {
            throw new BizException("查询失败");
        }
    }
}
