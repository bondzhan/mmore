package com.github.mmore.i18n.service;

import com.github.mmore.i18n.annotation.I18nMapping;
import com.github.mmore.i18n.annotation.I18nMappings;
import com.github.mmore.i18n.properties.I18nProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class I18nDictResolver {

    private final MessageSource errorMessageSource;
    private final I18nProperties props;

    public I18nDictResolver(@Qualifier("errorMessageSource") MessageSource errorMessageSource,
                            I18nProperties props) {
        this.errorMessageSource = errorMessageSource;
        this.props = props;
    }

    public void translate(Object body) {
        if (body == null) return;
        if (body instanceof Collection<?> col) {
            for (Object o : col) translate(o);
            return;
        }
        if (body.getClass().isArray()) {
            int len = java.lang.reflect.Array.getLength(body);
            for (int i = 0; i < len; i++) translate(java.lang.reflect.Array.get(body, i));
            return;
        }
        if (body instanceof Map<?,?> map) {
            for (Object val : map.values()) translate(val);
            return;
        }
        // Bean
        I18nMappings mappings = body.getClass().getAnnotation(I18nMappings.class);
        if (mappings == null) return;
        for (I18nMapping mapping : mappings.value()) {
            applyMapping(body, mapping);
        }
    }

    private void applyMapping(Object bean, I18nMapping mapping) {
        try {
            Field fromField = getField(bean.getClass(), mapping.from());
            Field toField = getField(bean.getClass(), mapping.to());
            if (fromField == null || toField == null) return;
            fromField.setAccessible(true);
            Object codeVal = fromField.get(bean);
            if (codeVal == null) return;
            String key = mapping.prefix() + "." + String.valueOf(codeVal);

            String label = resolveMessage(key, mapping.basenames());
            toField.setAccessible(true);
            toField.set(bean, label);
        } catch (IllegalAccessException ignored) {
        }
    }

    private Field getField(Class<?> clazz, String name) {
        for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
            try { return c.getDeclaredField(name); } catch (NoSuchFieldException ignored) {}
        }
        return null;
    }

    private String resolveMessage(String key, String[] overrideBasenames) {
        Locale locale = LocaleContextHolder.getLocale();
        if (overrideBasenames != null && overrideBasenames.length > 0) {
            // 临时 MessageSource with given basenames
            org.springframework.context.support.ReloadableResourceBundleMessageSource ms = new org.springframework.context.support.ReloadableResourceBundleMessageSource();
            ms.setBasenames(overrideBasenames);
            ms.setDefaultEncoding("UTF-8");
            ms.setUseCodeAsDefaultMessage(true);
            return ms.getMessage(key, null, key, locale);
        }
        // use global combined messageSource
        return errorMessageSource.getMessage(key, null, key, locale);
    }
}
