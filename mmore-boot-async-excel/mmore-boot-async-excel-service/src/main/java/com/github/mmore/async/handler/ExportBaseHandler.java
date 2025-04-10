package com.github.mmore.async.handler;

import com.github.mmore.async.model.AsyncTaskParseExportDto;
import com.github.mmore.async.model.TaskQueryParams;
import com.github.mmore.async.annotation.AsyncListener;
import com.github.mmore.async.model.AsyncTaskData;
import org.springframework.stereotype.Component;

@Component
public class ExportBaseHandler  implements IAsyncHandler<AsyncTaskParseExportDto> {

    @Override
    public AsyncTaskData<AsyncTaskParseExportDto> parserFile(TaskQueryParams importParams, AsyncListener importListener) {
        return new AsyncTaskData<>();
    }
}
