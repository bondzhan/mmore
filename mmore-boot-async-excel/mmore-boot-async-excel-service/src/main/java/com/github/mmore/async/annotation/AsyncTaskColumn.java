package com.github.mmore.async.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列宽设置
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncTaskColumn {

    /**
     * 列下标 从0开始
     */
    int index();

    /**
     * 列宽 不能大于255
     */
    int width() default 20;
}
