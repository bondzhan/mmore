package com.github.mmore.db.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.github.mmore.db.properties.MmoreMybatisPlusProperties;
import com.github.mmore.db.properties.MmoreTenantProperties;
import com.github.mmore.db.interceptor.PoMetaObjectHandler;
import com.github.mmore.db.interceptor.MmoreTenentHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @Author Bond
 * @Date 2025/1/8
 * @Description TODO
 */
@Slf4j
@AutoConfiguration
@Import({
        MmoreTenantProperties.class,
        MmoreMybatisPlusProperties.class
})
public class CustomInterceptorDbAutoConf {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MmoreMybatisPlusProperties mybatisPlusProperties, MmoreTenantProperties tenantProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if(tenantProperties.isEnabled()) {
            log.info("开启多租户插件");
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new MmoreTenentHandler(tenantProperties)));
        }
        if (mybatisPlusProperties.getPagination().isEnabled()) {
            log.info("开启分页插件");
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.getDbType("mysql")));
        }
        if(mybatisPlusProperties.getGlobalConfig().getDbConfig().getSqlSafe().isEnabled()){
            log.info("开启SQL安全插件 全表删除，全表更新");
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }
        return interceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plus.global-config.db-config.auto-fill-field", name = "enabled", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler(MmoreMybatisPlusProperties mybatisPlusProperties) {
        log.info("开启自动填充字段插件");
        return new PoMetaObjectHandler(mybatisPlusProperties);
    }
}
