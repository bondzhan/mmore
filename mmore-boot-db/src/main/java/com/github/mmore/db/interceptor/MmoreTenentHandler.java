package com.github.mmore.db.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import com.github.mmore.common.util.LoginContextHolder;
import com.github.mmore.db.properties.MmoreTenantProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

public class MmoreTenentHandler implements TenantLineHandler {
    private MmoreTenantProperties tenantProperties;

    public MmoreTenentHandler(MmoreTenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }
    @Override
    public Expression getTenantId() {
        if(LoginContextHolder.currentTenantId() == null){
            throw new SystemException(SystemErrorType.TENANT_ID_NOT_EXIT);
        }
        // 返回租户ID的表达式
        return new LongValue(LoginContextHolder.currentTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        // 返回租户ID列名
        return tenantProperties.getTenantIdColumnName();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return tenantProperties.getIgnoreTables().stream().anyMatch(
                ignoreTable -> ignoreTable.equalsIgnoreCase(tableName.replaceAll("`",""))
        );
    }
}
