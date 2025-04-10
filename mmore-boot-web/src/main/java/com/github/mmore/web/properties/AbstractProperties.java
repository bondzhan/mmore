package com.github.mmore.web.properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bond
 * @Date 2025/1/4
 */
public class AbstractProperties implements ApplicationContextAware {
    public static Map<String,AbstractProperties> configClassMap=new ConcurrentHashMap();
    private static ApplicationContext context;
    protected static  <T extends AbstractProperties> T getConfig(Class<T> clazz) {
        AbstractProperties config=configClassMap.get(clazz.getSimpleName());
            if (config == null) {
                synchronized (clazz) {
                    if (config == null) {
                        config = context.getBean(clazz);
                        configClassMap.put(clazz.getSimpleName(),config);
                    }
                }
            }
        return (T) config;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;

    }
}
