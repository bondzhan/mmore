package com.github.mmore.i18n.locale;

import com.github.mmore.i18n.properties.I18nProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;


/**
 * * @Author Bond
 */
@Slf4j
@RequiredArgsConstructor
public class I18nLocaleInterceptor implements HandlerInterceptor {

    private final I18nProperties props;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerName = props.getLanguageHeader();
        String lang = request.getHeader(headerName);

        // 同时兼容 Accept-Language（浏览器/客户端常用）
        if (!StringUtils.hasText(lang)) {
            lang = request.getHeader("Accept-Language");
        }

        // 尝试设置 Locale
        if (StringUtils.hasText(lang)) {
            Locale locale = parseToLocale(lang);
            if (locale != null) {
                LocaleContextHolder.setLocale(locale);
                return true;
            }
        }

        // 两个头都没有时，使用默认 Locale（如配置）
        if (StringUtils.hasText(props.getDefaultLocale())) {
            Locale def = parseToLocale(props.getDefaultLocale());
            if (def != null) {
                LocaleContextHolder.setLocale(def);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        LocaleContextHolder.resetLocaleContext();
    }

    private Locale parseToLocale(String value) {
        try {
            // 支持下划线/大小写形式，例如 zh_CN、EN_us
            String tag = value.replace('_', '-');
            return Locale.forLanguageTag(tag);
        } catch (Exception e) {
            log.debug("Invalid language header: {}", value);
            return null;
        }
    }
}
