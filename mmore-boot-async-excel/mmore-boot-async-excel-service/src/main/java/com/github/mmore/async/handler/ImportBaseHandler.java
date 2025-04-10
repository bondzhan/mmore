package com.github.mmore.async.handler;

import cn.idev.excel.FastExcel;
import com.github.mmore.async.model.AsyncTaskParseImportDto;
import com.github.mmore.async.model.TaskQueryParams;
import com.github.mmore.excel.listener.BaseExcelListener;
import com.github.mmore.async.annotation.AsyncListener;
import com.github.mmore.async.model.AsyncTaskData;
import com.github.mmore.common.model.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Slf4j
@Component
public class ImportBaseHandler implements IAsyncHandler<AsyncTaskParseImportDto> {

    /**
     * 下载文件
     *
     * @param fileUrl 文件地址
     * @return 文件
     * @throws Exception 异常
     */
    private static File downloadFile(String fileUrl) throws Exception {
        URL website = new URL(fileUrl);
        File tempFile = File.createTempFile("temp", ".xlsx"); // 创建唯一临时文件
        tempFile.deleteOnExit(); // 确保 JVM 退出时删除临时文件

        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
             FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileUrl, e);
            throw new BizException("文件下载失败，请检查网络连接或文件地址是否正确");
        }
        return tempFile;
    }

    @Override
    public AsyncTaskData<AsyncTaskParseImportDto> parserFile(TaskQueryParams importParams, AsyncListener annotationListener) {
        AsyncTaskData<AsyncTaskParseImportDto> asyncTaskData = new AsyncTaskData<>();
        BaseExcelListener<AsyncTaskParseImportDto> listener = new BaseExcelListener<>();
        try {
            File file = downloadFile(importParams.getFileUrl());
            FastExcel.read(new FileInputStream(file), annotationListener.dataType(), listener).sheet(annotationListener.sheetNum()).doRead();
        } catch (Exception e) {
            log.error("解析文件异常", e);
            throw new BizException(e.getMessage());
        }
        asyncTaskData.setAsyncTaskDtos(listener.getDataList());
        return asyncTaskData;
    }
}
