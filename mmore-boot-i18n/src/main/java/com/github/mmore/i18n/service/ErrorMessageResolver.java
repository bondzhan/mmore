package com.github.mmore.i18n.service;

import com.github.mmore.common.model.ApiErrorType;
import com.github.mmore.i18n.properties.I18nProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Author Bond
 */
public class ErrorMessageResolver {

    private final MessageSource messageSource;
    private final I18nProperties props;

    public ErrorMessageResolver(MessageSource messageSource, I18nProperties props) {
        this.messageSource = messageSource;
        this.props = props;
    }

    public String resolve(ApiErrorType errorType) {
        return resolve(errorType, LocaleContextHolder.getLocale());
    }

    public String resolve(ApiErrorType errorType, Locale locale) {
        if (errorType == null) {
            return null;
        }
        String code = errorType.getCode();
        String msg = messageSource.getMessage(code, null, null, locale);
        if (msg == null && props.isFallbackToErrorMesg()) {
            msg = errorType.getMesg();
        }
        return msg != null ? msg : code;
    }
}

