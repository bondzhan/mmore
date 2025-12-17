package com.github.mmore.job.aop;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.util.JsonUtil;
import com.github.mmore.common.util.LoginContextHolder;
import com.github.mmore.job.annotation.JobLoginContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class JobLoginContextAspect {

    @Around("@annotation(com.github.mmore.job.annotation.JobLoginContext)")
    public Object setJobLoginContext(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取被拦截的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取注解信息
        JobLoginContext annotation = method.getAnnotation(JobLoginContext.class);
        // 获取注解中指定的参数名称
        String paramName = annotation.paramName();

        // 获取方法的参数名称和值
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            String jsonString = JsonUtil.toJSONString(args[0]);
            String jobParams = JsonUtil.parseJSONObject(jsonString).path("jobParams").asText();

            if (StrUtil.isNotBlank(jobParams)) {
                JsonNode jsonNode = JsonUtil.parseJSONObject(jobParams);
                JsonNode paramValueNode = jsonNode.get(paramName);
                if (Objects.nonNull(paramValueNode)) {
                    LoginContext loginContext = JsonUtil.parseObject(JsonUtil.toJSONString(paramValueNode), LoginContext.class);
                    LoginContextHolder.set(loginContext);
                }
            }
        }

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 返回方法执行结果
        return result;
    }
}
