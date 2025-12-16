package com.github.mmore.i18n.properties;

import com.github.mmore.web.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * * @Author Bond
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mmore.i18n")
public class I18nProperties extends AbstractProperties {

    public static I18nProperties getConfig() {
        return getConfig(I18nProperties.class);
    }

    /**
     * 消息源基名，支持多个。默认：classpath:error/messages
     * 可配置为 error/en 以适配 error/en_us.properties 的命名风格
     */
    private List<String> basenames = new ArrayList<>();

    /**
     * 从请求头读取的语言标识，默认 Accept-Language
     */
    private String languageHeader = "Accept-Language";

    /**
     * 当未找到对应文案时，是否回退到 ApiErrorType.getMesg()
     */
    private boolean fallbackToErrorMesg = true;

    /**
     * 当请求头未携带语言信息时的默认语言（可选，RFC 5646）。
     * 例如：en、en-US、zh-CN、zh-TW
     * 留空则不修改当前线程 Locale。
     */
    private String defaultLocale;

    /**
     * 业务字典的 basenames（用于枚举/状态码等显示文案）。
     * 默认：classpath:dict/messages
     */
    private List<String> dictBasenames = new ArrayList<>();

    public I18nProperties() {
        if (basenames.isEmpty()) {
            basenames.add("classpath:error/messages");
        }
        if (dictBasenames.isEmpty()) {
            dictBasenames.add("classpath:dict/messages");
        }
    }
}
