package com.github.mmore.db.interceptor;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.web.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.*;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@Order(100)
@RestControllerAdvice
public class DbExceptionHandlerAdvice {
    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ApiResult duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DUPLICATE_PRIMARY_KEY);
    }

    /**
     * 处理数据完整性约束违反异常
     */
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ApiResult handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("data integrity violation:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DATA_INTEGRITY_VIOLATION);
    }

    /**
     * 处理数据库连接异常
     */
    @ExceptionHandler(value = {SQLException.class})
    public ApiResult handleSQLException(SQLException ex) {
        log.error("database connection error:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DATABASE_CONNECTION_ERROR);
    }

    /**
     * 处理MyBatis-Plus特定异常
     */
    @ExceptionHandler(value = {MybatisPlusException.class})
    public ApiResult handleMybatisPlusException(MybatisPlusException ex) {
        log.error("mybatis-plus operation failed:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DATABASE_OPERATION_FAILED);
    }

    /**
     * 处理数据找不到的异常
     */
    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ApiResult handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.warn("data not found:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DATA_NOT_FOUND);
    }

    /**
     * 处理SQL语法错误
     */
    @ExceptionHandler(value = {BadSqlGrammarException.class})
    public ApiResult handleBadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("sql syntax error:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.SQL_SYNTAX_ERROR);
    }

    /**
     * 处理超时异常
     */
    @ExceptionHandler(value = {QueryTimeoutException.class})
    public ApiResult handleQueryTimeoutException(QueryTimeoutException ex) {
        log.error("database query timeout:{}", ex.getMessage());
        return ApiResult.fail(SystemErrorType.DATABASE_TIMEOUT);
    }

}
