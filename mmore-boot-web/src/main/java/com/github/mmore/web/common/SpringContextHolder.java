package com.github.mmore.web.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }
    /**
     * 获取 Spring 应用上下文
     * @return Spring 应用上下文
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 根据 Bean 名称获取 Bean 实例
     * @param name Bean 名称
     * @return Bean 实例
     */
    public static Object getBean(String name) {
        assertContextInjected();
        return applicationContext.getBean(name);
    }

    /**
     * 根据 Bean 类型获取 Bean 实例
     * @param requiredType Bean 类型
     * @param <T> Bean 类型的泛型
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 根据 Bean 名称和类型获取 Bean 实例
     * @param name Bean 名称
     * @param requiredType Bean 类型
     * @param <T> Bean 类型的泛型
     * @return Bean 实例
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 判断 Spring 应用上下文是否注入
     * @return 如果已注入返回 true，否则返回 false
     */
    public static boolean containsBean(String name) {
        return applicationContext != null && applicationContext.containsBean(name);
    }

    /**
     * 检查应用上下文是否注入，若未注入则抛出异常
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("Spring application context has not been injected yet.");
        }
    }


}
