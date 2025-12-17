package com.github.mmore.web.model;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.mmore.common.model.ApiErrorType;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.web.common.TraceIdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@Accessors
@Schema(description = "统一API返回结果格式")
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 处理成功的常量代码
     */
    public static final String SUCCESSFUL_CODE = "000000";
    /**
     * 处理成功的默认提示信息
     */
    public static final String SUCCESSFUL_MESG = "处理成功";
    /**
     * 统一报文中code代码，标明异常类型
     */
    @Schema(title = "处理结果code", description = "处理结果，000000为成功", requiredMode = Schema.RequiredMode.REQUIRED, example="000000")
    private final String code;
    /**
     * 统一报文中提示信息
     */
    @Schema(title = "处理结果描述信息", description = "对code的详细说明和描述", example = "成功")
    private final String msg;

    /**
     * 统一报文中报文生成时间
     */
    @Schema(title = "请求结果生成时间戳", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-01-01T20:30:00.123Z")
    @JsonFormat(pattern = DatePattern.UTC_MS_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime time;
    /**
     * 统一报文中数据部分，支持泛型
     */
    @Schema(title = "处理结果数据信息", description = "可能为字符串、数组、对象等", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳市")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @Schema(title = "请求追踪ID", description = "一个串", example = "1234567890")
    private String traceId;

    /**
     * 默认构建方法，无参时默认为成功报文
     */
    public ApiResult() {
        this.code = SUCCESSFUL_CODE;
        this.msg = SUCCESSFUL_MESG;
        this.time = LocalDateTime.now();
        this.traceId = TraceIdUtil.getTraceId();
    }

    /**
     * 通过ErrorType构建Result
     *
     * @param errorType 错误类型
     */
    public ApiResult(ApiErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMesg();
        this.time = LocalDateTime.now();
        this.traceId = TraceIdUtil.getTraceId();
    }

    /**
     * 通过ErrorType和data数据构建Result
     *
     * @param errorType 错误类型
     * @param data      错误数据
     */
    public ApiResult(ApiErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code 错误码
     * @param msg 错误信息
     * @param data 错误数据对象
     */
    private ApiResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = LocalDateTime.now();
        this.traceId = TraceIdUtil.getTraceId();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 错误数据对象
     * @return Result
     */
    public static <T> ApiResult<T>  success(T data) {
        return new ApiResult<T>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static <T> ApiResult<T> success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static <T> ApiResult<T> fail() {
        return new ApiResult(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 异常返回msg提示
     *
     * @return Result
     */
    public static <T> ApiResult<T> failMsg(String msg) {
        return new ApiResult(SystemErrorType.SYSTEM_ERROR.getCode(), msg, null);
    }

    public static <T> ApiResult<T> failMsg(String code, String msg) {
        return new ApiResult(code, msg, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 错误数据对象
     * @return Result
     */
    public static  <T> ApiResult<T> fail(T data) {
        return new ApiResult<T>(SystemErrorType.SYSTEM_ERROR, data);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException 异常类参数
     * @return Result
     */
    public static  <T> ApiResult<T> fail(SystemException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data          错误数据对象
     * @param baseException 异常对象
     * @return Result
     */
    public static  <T> ApiResult<T> fail(SystemException baseException, T data) {
        return new ApiResult<T>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @param data      错误数据
     * @return Result
     */
    public static  <T> ApiResult<T> fail(ApiErrorType errorType, T data) {
        return new ApiResult<T>(errorType, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @return Result
     */
    public static  <T> ApiResult<T> fail(ApiErrorType errorType) {
        return ApiResult.fail(errorType, null);
    }

    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    /**
     * 判断请求成功且结果不为空
     *
     * @param apiErrorType
     * @return
     */
    public T checkNotNullAndGetData(ApiErrorType apiErrorType) {
        if (isFail()) {
            throw new SystemException(apiErrorType, this.msg);
        }

        if (this.data == null) {
            throw new SystemException(apiErrorType);
        }
        return this.data;
    }

    /**
     * 判断请求成功
     *
     * @param apiErrorType
     * @return
     */
    public T checkSuccessAndGetData(ApiErrorType apiErrorType) {
        if (isFail()) {
            throw new SystemException(SystemErrorType.SYSTEM_BUSY, this.msg);
        }

        return this.data;
    }
}
