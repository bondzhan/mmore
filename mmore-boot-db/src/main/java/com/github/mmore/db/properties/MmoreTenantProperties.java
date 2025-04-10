package com.github.mmore.db.properties;

import com.github.mmore.web.properties.AbstractProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Bond
 * @Date 2025/1/8
 * @Description TODO
 */
@ConfigurationProperties(prefix = "mmore.tenant")
@Getter
@Setter
public class MmoreTenantProperties extends AbstractProperties {
    public static MmoreTenantProperties getConfig(){
        return getConfig(MmoreTenantProperties.class);
    }

    /**
     * 是否开启多租户,默认关闭
     */
    private boolean enabled=false;
    /**
     * 忽略多租户,表名
     */
    private List<String> ignoreTables=new ArrayList<>();
    /**
     * 数据库中租户id的列名
     */
    private String tenantIdColumnName="tenant_id";
}
