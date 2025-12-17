package com.github.mmore.context.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.github.mmore.common.model.LoginContext;
import com.github.mmore.common.util.LoginContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.List;


@Slf4j
public class RequestContextInterceptor implements RequestInterceptor {

    /**
     * 需要透传的请求头
     */
    private List<String> transmissionHeaders = Lists.newArrayList(
            "route"
    );

    @Override
    public void apply(RequestTemplate template) {
        // 优先尝试从 LoginContextHolder 获取上下文
        LoginContext loginContext = null;
        try {
            loginContext = LoginContextHolder.getLoginContext();
            if (loginContext != null && applyHeadersFromLoginContext(template, loginContext)) {
                log.debug("Headers successfully applied from LoginContextHolder");
                return;
            }
        } catch (Exception e) {
            log.error("Failed to get or apply headers from LoginContextHolder", e);
        }
    }

    /**
     * 从 LoginContext 中构造并应用 header
     *
     * @param template Feign 请求模板
     * @param loginContext 登录上下文
     * @return 是否成功应用 header
     */
    private boolean applyHeadersFromLoginContext(RequestTemplate template, LoginContext loginContext) {
        boolean hasHeaders = false;
        // 将整个 LoginContext 序列化为 JSON 并添加到 header
        try {
            String loginContextJson = new ObjectMapper().writeValueAsString(loginContext);
            template.header("mmore-login-context", loginContextJson);
            log.debug("Added mmore-login-context header: {}", loginContextJson);

            // 透传route
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        if (transmissionHeaders.contains(headerName)) {
                            String headerValue = request.getHeader(headerName);
                            template.header(headerName, headerValue);
                            log.debug("Added header: {} = {}", headerName, headerValue);
                        }
                    }
                }
            }
            hasHeaders = true;
        } catch (Exception e) {
            log.error("Failed to serialize LoginContext to JSON", e);
        }
        return hasHeaders;
    }
//    }
}
