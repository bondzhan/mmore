mmore-boot-web（Web 基础能力）

定位
- 提供统一返回、全局异常、Swagger/Knife4j、CORS、登录上下文拦截与链路追踪接入的开箱即用能力。

自动装配
- `MmoreBootWebAutoConf` 导入：
  - `CorsAutoConf`：基于 `mmore.cors.*` 的跨域配置。
  - `CustomWebMvcConf`：静态资源、拦截器（`LoginContextHandler`）。
  - `SwaggerAutoConf`：OpenAPI/Knife4j；属性 `mmore.swagger.*`。
  - 初始化 `TraceIdUtil`（Micrometer Tracer）。

核心组件
- 统一返回：`ApiResult<T>`（code/msg/time/traceId/data）。
- 全局异常：`GlobalExceptionHandlerAdvice`（参数/方法/媒体类型/资源/Feign/系统/业务异常）。
- 登录上下文：`LoginContextHandler` 解析请求头 `mmore-login-context`，保存到 `LoginContextHolder`。
- 工具：`TraceIdUtil`、`SpringContextHolder`、`PageUtil`。

主要配置
- Swagger（application.yml）
  - `mmore.swagger.{title,description,version,license,url,wikiUrl,wikiDocumentation}`
- CORS（application.yml）
  - `mmore.cors.{allowedOrigin,allowedHeader,allowedMethod,exposedHeaders,path,allowCredentials,maxAge}`

使用方式
- 直接在业务 POM 引入 `mmore-boot-web` 或经 `mmore-boot-starter` 聚合引入。
- 控制器中抛出 `SystemException/BizException` 将被统一异常处理成 `ApiResult`。

