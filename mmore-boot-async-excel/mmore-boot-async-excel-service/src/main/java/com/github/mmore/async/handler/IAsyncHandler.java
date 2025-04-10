package com.github.mmore.async.handler;


import com.github.mmore.async.model.TaskQueryParams;
import com.github.mmore.async.annotation.AsyncListener;
import com.github.mmore.async.model.AsyncTaskData;

public interface IAsyncHandler<T> {

    AsyncTaskData<T> parserFile(TaskQueryParams importParams, AsyncListener importListener);
}
