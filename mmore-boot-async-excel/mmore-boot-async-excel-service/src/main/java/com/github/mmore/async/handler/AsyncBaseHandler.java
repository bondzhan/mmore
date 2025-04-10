package com.github.mmore.async.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.handler.AbstractSheetWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.mmore.async.model.*;
import com.github.mmore.async.annotation.AsyncListener;
import com.github.mmore.async.annotation.AsyncTaskColumn;
import com.github.mmore.async.sdk.api.AsyncTaskPersistenceApi;
import com.github.mmore.async.sdk.model.AsyncTaskDto;
import com.github.mmore.async.sdk.model.AsyncTaskStatusEnum;
import com.github.mmore.common.model.BizException;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.file.OssFileClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AsyncBaseHandler {

    @Resource
    private AsyncListenerPostProcessor asyncListenerPostProcessor;
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private AsyncTaskPersistenceApi asyncTaskApi;

    @Resource
    private OssFileClient ossFileClient;

    public void handler(AsyncTaskDto asyncTaskDto, TaskQueryParams taskQueryParams) {
        AsyncListener listener = asyncListenerPostProcessor.getListener(taskQueryParams.getTaskCode().getCode());
        AsyncTaskData asyncTaskData;

        // 解析文件
        if (TaskCode.IMPORT.equals(taskQueryParams.getTaskCode().getTaskType())) {
            asyncTaskData = applicationContext.getBean(ImportBaseHandler.class).parserFile(taskQueryParams, listener);
        } else {
            asyncTaskData = applicationContext.getBean(ExportBaseHandler.class).parserFile(taskQueryParams, listener);
        }

        log.info("文件解析完成...");

        // 业务处理
        try {
            // 数据库持久化
            asyncTaskDto.setStatus(AsyncTaskStatusEnum.PROCESSING.getCode());
            updateStatus(asyncTaskDto);

            log.info("业务开始处理...");
            // 业务处理
            AsyncTaskParseDto asyncTask = processMessage(taskQueryParams.getTaskCode().getCode(), asyncTaskData);
            log.info("业务处理完成...");
            // 保存结果到oss
            String fileUrl = uploadResultOss(asyncTaskData.getAsyncTaskDtos(), listener);
            log.info("上传到oss完成...");

            // 更新状态处理完成
            asyncTaskDto.setStatus(AsyncTaskStatusEnum.SUCCESS.getCode());
            asyncTaskDto.setResultFileUrl(fileUrl);

            // 如果是导入计算错误
            if (TaskCode.IMPORT.equals(asyncTaskDto.getTaskType())) {
                if (CollectionUtil.isNotEmpty(asyncTaskData.getAsyncTaskDtos())) {
                    List<AsyncTaskParseImportDto> dtos = asyncTaskData.getAsyncTaskDtos();
                    int count = (int) dtos.stream().filter(item ->
                            StringUtils.isNotBlank(item.getErrorMsg())
                    ).count();

                    asyncTaskDto.setSuccessCount(dtos.size() - count);
                    asyncTaskDto.setFailCount(count);
                    asyncTaskDto.setTotalCount(dtos.size());
                }

            }
//            updateStatus(asyncTaskDto);
            log.info("任务处理完成...");
        } catch (InvocationTargetException e) {
            log.error("调用业务方法异常", e);
            throw new BizException(e.getTargetException().getMessage());
        } catch (IllegalAccessException e) {
            log.error("调用业务方法异常", e);
            throw new BizException(e.getMessage());
        } catch (SystemException e) {
            log.error("业务处理异常");
            throw e;
        } catch (Exception e) {
            log.error("其他异常", e);
            throw e;
        } finally {
            try {
                if (!Objects.equals(asyncTaskDto.getStatus(), AsyncTaskStatusEnum.SUCCESS.getCode())) {
                    asyncTaskDto.setStatus(AsyncTaskStatusEnum.FAIL.getCode());
                }
                updateStatus(asyncTaskDto);
            } catch (Exception e) {
                log.error("更新状态异常", e);
            }
        }
    }

    private AsyncTaskParseDto processMessage(String bizCode, Object message) throws InvocationTargetException, IllegalAccessException {
        Method method = asyncListenerPostProcessor.getMethod(bizCode);
        if (method != null) {
            return (AsyncTaskParseDto)method.invoke(asyncListenerPostProcessor.getBean(bizCode), message);
        }
        return null;
    }

    private String uploadResultOss(List<AsyncTaskParseDto> dataList, AsyncListener listener) {

        if (dataList == null) {
            return null;
        }

        // 创建唯一临时文件
        File tempFile = null;
        try {
            tempFile = File.createTempFile("result", ".xlsx");
            tempFile.deleteOnExit(); // 确保 JVM 退出时删除临时文件

            // 写入数据到临时文件
            FastExcel.write(tempFile, listener.dataType())
                    .registerWriteHandler(new CustomColumnWidthHandler(listener.dataType()))
                    .head(listener.dataType())
                    .sheet("数据" + System.currentTimeMillis())
                    .doWrite(dataList);

            // 上传文件到 OSS
            try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
                return ossFileClient.uploadFile(fileInputStream.readAllBytes(), tempFile.getName());
            } catch (IOException e) {
                log.error("上传文件到 OSS 失败: {}", tempFile.getName(), e);
                throw new BizException("上传文件到 OSS 失败，请检查网络连接或文件内容");
            }

        } catch (Exception e) {
            log.error("生成结果文件失败", e);
            throw new BizException("生成结果文件失败，请检查文件内容或配置");

        } finally {
            // 确保临时文件被删除
            if (tempFile != null && !tempFile.delete()) {
                log.warn("无法删除临时文件: {}", tempFile.getAbsolutePath());
            }
        }
    }

    /**
     *  更新数据库
     * @param asyncTaskDto
     */
    private void updateStatus(AsyncTaskDto asyncTaskDto) {
        asyncTaskApi.update(asyncTaskDto);
    }


    // 自定义列宽处理器
    static class CustomColumnWidthHandler extends AbstractSheetWriteHandler {

        private Class<?> clazz;

        public CustomColumnWidthHandler(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof AsyncTaskColumn asyncTaskColumn) {
                        writeSheetHolder.getSheet().setColumnWidth(asyncTaskColumn.index(), asyncTaskColumn.width() * 256);
                    }
                }
            }
        }
    }
}
