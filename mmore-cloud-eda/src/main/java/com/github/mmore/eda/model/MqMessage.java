package com.github.mmore.eda.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class MqMessage implements Serializable {

    /**
     * 业务唯一性id 做幂等用
     */
    private String uniqueBizId;
    /**
     * key用于查询
     */
    private String key;
    /**
     * tag
     */
    private String tag;
    /**
     * 消息体（请转换为JSON格式）
     */
    private String body;

    /**
     * 业务扩展字段
     */
    private Map<String, String> features;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 身份id
     */
    private Long userIdentityId;
}
