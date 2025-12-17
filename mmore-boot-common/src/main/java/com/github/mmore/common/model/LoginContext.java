package com.github.mmore.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginContext {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 身份id
     */
    private Long userIdentityId;

    /**
     * 身份类型
     */
    private Integer type;

    /**
     * 租户ID
     */
    private Long tenantId;
}
