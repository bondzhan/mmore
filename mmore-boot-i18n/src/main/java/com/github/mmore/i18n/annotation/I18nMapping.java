package com.github.mmore.i18n.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(I18nMappings.class)
public @interface I18nMapping {
    /** 源字段名（存放code的字段） */
    String from();
    /** 目标字段名（存放本地化描述） */
    String to();
    /** key 前缀，例如：status → 拼接为 status.{code} */
    String prefix();
    /** 覆盖 basenames（可选），为空则使用全局 dictBasenames */
    String[] basenames() default {};
}

