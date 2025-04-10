package com.github.mmore.eda.aop;

import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.util.JsonUtil;
import com.github.mmore.common.util.LoginContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * <p>@description
 * <p>@date 2025-02-25  17:51
 *
 * @author 蒋昊宇
 */
@Aspect
@Component
public class EdaLoginContextAspect {
    @Around("@annotation(com.github.mmore.eda.annotation.EdaLoginContext)")
    public Object setJobLoginContext(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取被拦截的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取方法的参数名称和值
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            String jsonString = JsonUtil.toJSONString(args[0]);
            LoginContext loginContext = JsonUtil.parseObject(jsonString, LoginContext.class);
            LoginContextHolder.set(loginContext);
        }

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 返回方法执行结果
        return result;
    }
}
