package com.github.mmore.i18n.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nMappings {
    I18nMapping[] value();
}

