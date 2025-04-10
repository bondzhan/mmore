package com.github.mmore.job.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>@description
 * <p>@date 2025-02-21  11:13
 *
 * @author 蒋昊宇
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JobLoginContext {
    String paramName() default "loginContext";
}
