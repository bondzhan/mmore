package com.github.mmore.common.util;

import cn.hutool.core.text.UnicodeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author Bond
 * @Date 2025/1/8
 * @Description 当前登录上下文
 */
@Slf4j
public class LoginContextHolder {

    private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    // 存储 LoginContext 对象
    public static void set(LoginContext loginContext) throws JsonProcessingException {
        String jsonString = new ObjectMapper().writeValueAsString(loginContext);
        THREAD_LOCAL.set(UnicodeUtil.toUnicode(jsonString));
    }

    // 获取 LoginContext 对象
    public static LoginContext getLoginContext() {
        String context = THREAD_LOCAL.get();
        if (context == null || context.isEmpty()) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(context, LoginContext.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse LoginContext", e);
            throw new SystemException(SystemErrorType.SYSTEM_ERROR,"Failed to parse LoginContext");
        }
    }

    // 获取用户 ID
    public static Long currentUserId() {
        LoginContext loginContext = getLoginContext();
        return loginContext != null ? loginContext.getUserId() : null;
    }

    // 获取用户 身份ID
    public static Long currentUserIdentityId() {
        LoginContext loginContext = getLoginContext();
        return loginContext != null ? loginContext.getUserIdentityId() : null;
    }

    // 获取租户 ID
    public static Long currentTenantId() {
        LoginContext loginContext = getLoginContext();
        return loginContext != null ? loginContext.getTenantId() : null;
    }

    // 获取用户名
    public static String currentUserName() {
        LoginContext loginContext = getLoginContext();
        return loginContext != null ? loginContext.getUserName() : null;
    }

    // 获取用户 身份类型
    public static Integer currentType() {
        LoginContext loginContext = getLoginContext();
        return loginContext != null ? loginContext.getType() : null;
    }

    // 清理上下文
    public static void clear() {
        THREAD_LOCAL.remove();
        if (log.isDebugEnabled()) {
            log.debug("LoginContext cleared for thread: {}", Thread.currentThread().getName());
        }
    }
}
