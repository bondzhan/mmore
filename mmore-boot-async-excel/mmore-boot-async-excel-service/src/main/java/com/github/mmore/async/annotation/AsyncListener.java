package com.github.mmore.async.annotation;


import com.github.mmore.async.model.AsyncTaskParseDto;
import com.github.mmore.async.model.TaskCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncListener {

    /**
     * 业务类型
     */
    TaskCodeEnum taskCode();


    /**
     * 数据类型
     */
    Class <? extends AsyncTaskParseDto> dataType();

    /**
     * 解析sheet页码
     * @return
     */
    int sheetNum() default 0;

    /**
     * 自定义处理器
     */
//    Class<? extends IAsyncHandler<? extends AsyncTaskParseDto>> customHandler() default ImportBaseHandler.class;
}