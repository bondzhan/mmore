mmore-cloud-context（调用上下文与路由）

定位
- 为 Spring Cloud 场景提供：
  - Feign 请求透传登录上下文与自定义头（如 route）。
  - 基于请求头的服务实例过滤（按 metadata.route 实现路由分组）。

自动装配
- `MmoreCloudContextAutoConf` → `OpenFeignAutoConf`
  - `RequestContextInterceptor`：将 `LoginContext` 序列化到请求头 `mmore-login-context`，并透传 `route` 等头。
  - `RouteServiceInstanceSupplier`：按请求头 `route` 过滤服务实例；无 `route` 时过滤带 route 元数据的实例。

使用说明
- 在被调用方继续使用 `mmore-boot-web` 的 `LoginContextHandler` 解析上下文。
- 给实例添加 metadata：`route=xxx`（注册中心，例如 Nacos 的实例元数据）。
- 调用时设置请求头 `route` 选择对应实例分组；不设置则走“无 route 元数据”的默认实例。

