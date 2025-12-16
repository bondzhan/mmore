mmore-boot-db（MyBatis-Plus 集成）

定位
- 集成并装配 MyBatis-Plus 插件：多租户、分页、SQL 安全（防全表）、公共字段自动填充等。

自动装配
- `MmoreBootDBConf` → `CustomInterceptorDbAutoConf`
  - 多租户：`TenantLineInnerInterceptor` + `MmoreTenentHandler`（从 `LoginContextHolder` 取租户）。
  - 分页：`PaginationInnerInterceptor`（默认开启）。
  - SQL 安全：`BlockAttackInnerInterceptor`（可开关）。
  - 元对象处理：`PoMetaObjectHandler`（create/modify/tenant 字段自动填充）。

配置项
- 多租户（application.yml）
  ```yaml
  mmore:
    tenant:
      enabled: true
      tenantIdColumnName: tenant_id
      ignoreTables: [dict_city, dict_country]
  ```
- MyBatis-Plus（application.yml）
  ```yaml
  mybatis-plus:
    pagination:
      enabled: true
      dbType: mysql
    global-config:
      db-config:
        sqlSafe:
          enabled: true
        auto-fill-field:
          enabled: true
          createName: create_by
          createTime: create_time
          modifyName: modify_by
          modifyTime: modify_time
  ```

使用建议
- 与 `mmore-boot-web` 搭配：登录拦截设置上下文 → DB 插件读取租户、用户。
- 自动填充字段需与表字段一致；忽略表注意反引号与大小写处理（已兼容）。

