package com.github.mmore.async.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.common.util.LoginContextHolder;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
@Component
public class TracingExecutors {

    @Resource
    private ThreadPoolTaskExecutor delegate;

    @Resource
    private Tracer tracer;

    private Runnable wrapRunnable(Runnable task) {
        Span parentSpan = tracer.currentSpan();
        LoginContext loginContext = LoginContextHolder.getLoginContext();
        return () -> {
            try (Tracer.SpanInScope ignored = tracer.withSpan(parentSpan)) {
                try {
                    LoginContextHolder.set(loginContext);
                } catch (JsonProcessingException e) {
                    log.error("导入导出上下文设置异常", e);
                    throw new SystemException(SystemErrorType.SYSTEM_ERROR, "导入导出上下文设置异常");
                }
                task.run();
            } finally {
                LoginContextHolder.clear();
            }
        };
    }

    private <T> Callable<T> wrapCallable(Callable<T> task) {
        Span parentSpan = tracer.currentSpan();
        LoginContext loginContext = LoginContextHolder.getLoginContext();
        return () -> {
            try (Tracer.SpanInScope ignored = tracer.withSpan(parentSpan)) {
                try {
                    LoginContextHolder.set(loginContext);
                } catch (JsonProcessingException e) {
                    log.error("导入导出上下文设置异常", e);
                    throw new SystemException(SystemErrorType.SYSTEM_ERROR, "导入导出上下文设置异常");
                }
                return task.call();

            } finally {
                LoginContextHolder.clear();
            }
        };
    }

    public void execute(Runnable command) {
        delegate.execute(wrapRunnable(command));
    }
    public Future<?> submit(Runnable task) {
        return delegate.submit(wrapRunnable(task));
    }

    @PreDestroy
    public void shutdownExecutor() {
        delegate.shutdown();
    }

}
