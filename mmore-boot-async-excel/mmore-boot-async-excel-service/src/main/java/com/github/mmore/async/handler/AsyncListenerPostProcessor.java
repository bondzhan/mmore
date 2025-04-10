package com.github.mmore.async.handler;

import com.github.mmore.async.annotation.AsyncListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AsyncListenerPostProcessor implements BeanPostProcessor {

    private final Map<String, Method> listeners = new HashMap<>();

    private Map<String, AsyncListener> listenersMap = new HashMap<>();

    private final Map<String, Object> processBeans = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AsyncListener.class)) {
                AsyncListener listener = method.getAnnotation(AsyncListener.class);
                String bizCode = listener.taskCode().getCode();
                listeners.put(bizCode, method);
                listenersMap.put(bizCode, listener);
                processBeans.put(bizCode, bean);
            }
        }
        return bean;
    }

    public Method getMethod(String bizCode) {
        return listeners.get(bizCode);
    }

    public AsyncListener getListener(String bizCode) {
        return listenersMap.get(bizCode);
    }

    public Object getBean(String bizCode) {
        return processBeans.get(bizCode);
    }
}
