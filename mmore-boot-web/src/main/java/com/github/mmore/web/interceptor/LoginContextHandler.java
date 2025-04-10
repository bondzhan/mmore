package com.github.mmore.web.interceptor;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.github.mmore.common.constant.LoginContextConstant;
import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.common.util.JsonUtil;
import com.github.mmore.common.util.LoginContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Optional;

/**
 * @Author Bond
 * @Date 2025/1/23
 * @Description 在DispatcherServlet 接收到请求和 Controller 处理请求之间, 验证及提取登录信息
 */
@Slf4j
public class LoginContextHandler implements HandlerInterceptor {

    /**
     * 特殊处理，上下文只需要租户id即可
     */
    public static final List<String> NOT_CHECK_LOGIN_URL_LIST = Lists.newArrayList(
            "/user/register",
            "/user/login",
            "/userIdentity/queryByUserId");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("LoginContextHandler preHandle 被调用");
        if (!ObjUtil.isEmpty(request)) {
            // 进行一些预处理操作，例如检查用户是否登录
            String requestUri = request.getRequestURI();
            String loginContextJson = request.getHeader(LoginContextConstant.LOGIN_CONTEXT);
            if (isNotCheckUrl(requestUri)) {
                //不需要校验token的地址处理
                LoginContext loginContext;
                if (StringUtils.isNotBlank(loginContextJson)) {
                    try {
                        loginContext = new ObjectMapper().readValue(loginContextJson, LoginContext.class);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to parse LoginContext", e);
                        throw new SystemException(SystemErrorType.SYSTEM_ERROR, "Failed to parse LoginContext");
                    }
                } else {
                    loginContext = new LoginContext();
                    loginContext.setTenantId(1L);
                }
                //上下文只要解析出租户id就放行
                log.info("不需要解析token的接口上下文解析通过  requestUri {} loginContext {}", requestUri, JsonUtil.toJSONString(loginContext));
                LoginContextHolder.set(loginContext);
                return true;
            }
            if (StrUtil.isBlankIfStr(loginContextJson) && ObjectUtil.isNull(LoginContextHolder.getLoginContext())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                log.info("上下文解析不通过  requestUri {} loginContextJson {}", requestUri, loginContextJson);
                // 阻止请求继续执行
                return false;
            }
            LoginContext loginContext;
            try {
                loginContext = new ObjectMapper().readValue(loginContextJson, LoginContext.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to parse LoginContext", e);
                throw new SystemException(SystemErrorType.SYSTEM_ERROR, "Failed to parse LoginContext");
            }
            LoginContextHolder.set(loginContext);

            if (Optional.ofNullable(loginContext).map(LoginContext::getUserId).isEmpty()
                    || Optional.of(loginContext).map(LoginContext::getTenantId).isEmpty()) {
                //其他拦截接口上下文必须包含登录id和租户
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                log.info("上下文解析不通过  requestUri {} loginContext {}", requestUri, JsonUtil.toJSONString(loginContext));
                // 阻止请求继续执行
                return false;
            }
        }
        // 允许请求继续执行
        return true;
    }


    private boolean isNotCheckUrl(String url) {
        for (String notCheckUrl : NOT_CHECK_LOGIN_URL_LIST) {
            if (StringUtils.contains(url, notCheckUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("LoginContextHandler afterCompletion 被调用");
        // 在请求完成后清理
        LoginContextHolder.clear();
    }
}
