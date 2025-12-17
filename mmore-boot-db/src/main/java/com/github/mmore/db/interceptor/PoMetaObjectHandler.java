package com.github.mmore.db.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.mmore.common.util.LoginContextHolder;
import com.github.mmore.db.properties.MmoreMybatisPlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

@Slf4j
public class PoMetaObjectHandler implements MetaObjectHandler {

    private MmoreMybatisPlusProperties mybatisPlusProperties;

    public PoMetaObjectHandler(MmoreMybatisPlusProperties mybatisPlusProperties) {
        this.mybatisPlusProperties = mybatisPlusProperties;
    }
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("执行 insertFill 方法");

        Long createBy = ObjectUtil.defaultIfNull(LoginContextHolder.currentUserIdentityId(), 0L);
        String createName = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getCreateName();
        this.strictInsertFill(metaObject, createName, Long.class, createBy);

        String createTime = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getCreateTime();
        this.strictInsertFill(metaObject, createTime, LocalDateTime.class, LocalDateTime.now());

        Long tenantId = LoginContextHolder.currentTenantId();
        this.strictInsertFill(metaObject,"tenantId",Long.class,tenantId);

        String modifyName = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getModifyName();
        String modifyTime = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getModifyTime();
        this.strictInsertFill(metaObject, modifyName, Long.class, createBy);
        this.strictInsertFill(metaObject, modifyTime, LocalDateTime.class, LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String modifyName = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getModifyName();
        String modifyTime = mybatisPlusProperties.getGlobalConfig().getDbConfig().getAutoFillField().getModifyTime();
        Long modifyBy = ObjectUtil.defaultIfNull(LoginContextHolder.currentUserIdentityId(), 0L);
        this.strictUpdateFill(metaObject, modifyName, Long.class, modifyBy);
        this.strictUpdateFill(metaObject, modifyTime, LocalDateTime.class, LocalDateTime.now());
    }
}