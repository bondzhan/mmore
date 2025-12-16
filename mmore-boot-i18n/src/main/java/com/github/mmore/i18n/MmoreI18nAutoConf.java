package com.github.mmore.i18n;

import com.github.mmore.i18n.advice.I18nExceptionHandlerAdvice;
import com.github.mmore.i18n.advice.I18nResponseBodyAdvice;
import com.github.mmore.i18n.locale.I18nLocaleInterceptor;
import com.github.mmore.i18n.properties.I18nProperties;
import com.github.mmore.i18n.service.ErrorMessageResolver;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author Bond
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(I18nProperties.class)
public class MmoreI18nAutoConf implements WebMvcConfigurer {
    private final I18nProperties props;

    public MmoreI18nAutoConf(I18nProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init(){
        log.info("Mmore-boot-i18n version " + MmoreI18nAutoConf.class.getPackage().getImplementationVersion() + " init success!");
    }

    @Bean
    public MessageSource errorMessageSource(I18nProperties props) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 合并错误与业务字典 basenames
        List<String> basenames = new java.util.ArrayList<>();
        if (props.getBasenames() != null) basenames.addAll(props.getBasenames());
        if (props.getDictBasenames() != null) basenames.addAll(props.getDictBasenames());
        if (basenames.isEmpty()) {
            basenames.add("classpath:error/messages");
            basenames.add("classpath:dict/messages");
        }
        messageSource.setBasenames(basenames.toArray(new String[0]));
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public ErrorMessageResolver errorMessageResolver(@Qualifier("errorMessageSource") MessageSource errorMessageSource, I18nProperties props) {
        return new ErrorMessageResolver(errorMessageSource, props);
    }

    @Bean
    public I18nLocaleInterceptor i18nLocaleInterceptor(I18nProperties props) {
        return new I18nLocaleInterceptor(props);
    }

    @Bean
    public I18nExceptionHandlerAdvice i18nExceptionHandlerAdvice(ErrorMessageResolver resolver) {
        return new I18nExceptionHandlerAdvice(resolver);
    }

    @Bean
    public I18nResponseBodyAdvice i18nResponseBodyAdvice(com.github.mmore.i18n.service.I18nDictResolver dictResolver) {
        return new I18nResponseBodyAdvice(dictResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(i18nLocaleInterceptor(props));
    }
}
