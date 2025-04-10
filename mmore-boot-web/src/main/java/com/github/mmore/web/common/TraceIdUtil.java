package com.github.mmore.web.common;

import io.micrometer.tracing.Tracer;

/**
 * @Author Bond
 * @Date 2025/1/7
 * @Description TraceId 获取工具类
 */
public class TraceIdUtil {
    private static Tracer tracer;

    // 设置Tracer实例
    public static void setTracer(Tracer tracerInstance) {
        tracer = tracerInstance;
    }

    // 获取当前请求的traceId
    public static String getTraceId() {
        return tracer != null && tracer.currentSpan() != null
                ? tracer.currentSpan().context().traceId()
                : null;
    }

}
