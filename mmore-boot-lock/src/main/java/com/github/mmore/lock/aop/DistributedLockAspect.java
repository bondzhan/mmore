package com.github.mmore.lock.aop;

import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.lock.handler.DistributedLock;
import com.github.mmore.lock.handler.LockHandler;
import com.github.mmore.lock.util.ELUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Aspect
public class DistributedLockAspect {

    @Resource
    private List<LockHandler> lockHandlers;

    private final String SPLIT = "\\,";

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        LockHandler lockHandler = lockHandlers.stream().filter(h -> h.getLock(distributedLock.lockType())).findFirst().orElse(null);
        if (lockHandler == null) {
            throw new SystemException(SystemErrorType.SYSTEM_ERROR, "获取不到对应的锁处理:" + distributedLock.lockType());
        }

        List<String> keyList = new ArrayList<>();

        String[] keys = distributedLock.keys();
        if (keys.length > 0) {
            // 解析key
            for (int i = 0; i < keys.length; i++) {
                keys[i] = ELUtil.parseExpression(keys[i], joinPoint);
                parseKey(keyList, keys[i]);
            }

        } else {
            String key;
            if ( StringUtils.isNotBlank(distributedLock.key())) {
                key = ELUtil.parseExpression(distributedLock.key(), joinPoint);
                parseKey(keyList, key);
            } else {
                key = method.getDeclaringClass().getName() + "#" + method.getName();
                keyList.add(key);
            }
//            return lockHandler.tryLock(distributedLock.keyPrefix(), key, distributedLock.expire(), distributedLock.timeUnit(), msg -> {
//                try {
//                    return joinPoint.proceed();
//                } catch (Throwable e) {
//                    log.error("分布式锁业务处理异常", e);
//                    throw new RuntimeException(e);
//                }
//            });
        }
        return lockHandler.tryLock(distributedLock.keyPrefix(), keyList, distributedLock.expire(), distributedLock.timeUnit(), msg -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                log.error("分布式锁业务处理异常", e);
                if (e instanceof SystemException) {
                    throw (SystemException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }

    private void parseKey(List<String> keyList, String key) {
        if (StringUtils.isNotBlank(key)) {
            String[] split = key.split(SPLIT);
            for (String s : split) {
                keyList.add(s.trim());
            }
        }
    }
}