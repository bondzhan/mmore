总体概览

  - 多模块 BOM + Starter 体系，统一依赖与版本，按职能拆分：Web 基础、通用工具、DB、缓存、分布式锁、云上下文、文件、
  Excel、分布式 ID、任务调度、EDA 事件等。
  - Spring Boot 3.2 / Spring Cloud 2023，模块通过 META-INF/spring.factories 自动装配，开箱即用。
  - 核心横切链路：登录上下文透传、租户隔离、链路追踪、统一异常、缓存与分布式锁、异步导入导出。

  依赖与版本管理（BOM）
  - mmore-base-dependencies
      - 统一管理版本与插件：Spring Boot/Cloud、MyBatis-Plus、JetCache、Knife4j、Micrometer、PowerJob、Sa-Token、CosId、
  fastexcel、Guava、Hutool 等。
      - 管理构建插件：spring-boot-maven-plugin、dockerfile-maven-plugin、git-commit-id-plugin、maven-surefire-plugin
  等。

  Web 基础与统一规范
  - mmore-boot-web
      - 自动装配：MmoreBootWebAutoConf 导入 CorsAutoConf、CustomWebMvcConf、SwaggerAutoConf，并注入 Tracer 到
  TraceIdUtil。
      - 全局异常：GlobalExceptionHandlerAdvice 统一处理参数、方法、媒体类型、资源、Feign、业务与系统异常，统一返
  回 ApiResult<T>。
      - 统一返回结构：ApiResult<T>（含 code/msg/time/traceId/data），工具方法 success/fail、数据校验方法
  checkNotNullAndGetData。
      - 登录上下文拦截：LoginContextHandler 解析请求头 mmore-login-context，放入 LoginContextHolder；默认放行部分无需登
  录的 URL。
      - CORS：CorsProperties（mmore.cors.*）可控来源/头/方法/暴露头/凭证等。
      - Swagger：SwaggerInfo（mmore.swagger.*）+ Knife4j。
      - 通用工具：TraceIdUtil、SpringContextHolder、PageUtil。

  通用模型与工具
  - mmore-boot-common
      - 异常体系：SystemErrorType、BizErrorType、SystemException、BizException，配合 Web 统一异常处理。
      - 登录上下文：LoginContext + LoginContextHolder（ThreadLocal 继承），提供 currentUserId/tenantId/... 辅助。
      - 分页模型：PageParam/PageResponse。
      - 能力模式：BaseAbility、AbilityFactory/Invoker 支持多 Provider 能力选择。

  数据库集成与多租户
  - mmore-boot-db
      - 自动装配：MmoreBootDBConf → CustomInterceptorDbAutoConf。
      - MyBatis-Plus 插件：
          - 租户：TenantLineInnerInterceptor + MmoreTenentHandler，从 LoginContextHolder.currentTenantId() 注入
  tenant_id（mmore.tenant.tenantIdColumnName 可改），支持表白名单（mmore.tenant.ignoreTables）。
          - 分页：PaginationInnerInterceptor（默认启用，可配 mybatis-plus.pagination.*）。
          - SQL 安全：BlockAttackInnerInterceptor（避免全表更新/删除，可控开关）。
          - 自动填充：PoMetaObjectHandler（创建/修改人/时间/租户等，字段名可通过 mybatis-plus.global-config.db-
  config.auto-fill-field.* 配置）。
      - 配置对象：MmoreTenantProperties（mmore.tenant.*）、MmoreMybatisPlusProperties（mybatis-plus.*）。

  缓存封装
  - mmore-boot-cache
      - 自动装配：MmoreBootCacheConf（启用 JetCache 方法缓存，依赖 JetCacheAutoConfiguration）+
  JetCacheManagerAutoConf。
      - 缓存模板：JetCacheTemplate 简化 get/put/remove/putAll（支持 area/name，默认 default 区域）。
      - 预置缓存名：CacheNameConstant（如 USER_CACHE/COMMON_CACHE/...），JetCacheManagerAutoConf 会在默认区域创建这
  些 Cache。

  分布式锁
  - mmore-boot-lock
      - 注解：@DistributedLock 支持 keyPrefix/key/keys[]（支持固定/SpEL/拼接），expire/timeUnit/lockType。
      - 切面：DistributedLockAspect 解析方法参数与 SpEL（ELUtil），支持对多个 key 同时加锁。
      - 实现：RedisLockHandler 基于 JetCache tryLock 实现分布式锁（通过 CacheNameConstant.COMMON_CACHE），自动释放，异
  常兜底。

  上下文与调用链透传
  - mmore-cloud-context
      - Feign 透传：RequestContextInterceptor 将 LoginContext 序列化为请求头 mmore-login-context，同时透传部分自定义头
  （如 route）。
      - 路由感知负载：RouteServiceInstanceSupplier 基于请求头 route 在服务实例元数据中做过滤路由；无 route 时默认过滤带
  路由元数据的实例。
      - 自动装配：MmoreCloudContextAutoConf 导入 OpenFeignAutoConf。
  - mmore-cloud-starter
      - 仅聚合引入 cloud-context，便于业务模块统一依赖。

  文件服务（OSS）
  - mmore-boot-file
      - 自动装配：MmoreFileAutoConf（oss.enabled=true 生效），提供 OSS 客户端与 OssFileClient。
      - 客户端：OssFileClient 支持字节/URL 上传（拼接对象路径）、生成下载签名 URL（downFile）。
      - 控制器：OssController 提供简单上传接口（条件装配 oss.enabled）。
      - 配置：OssProperties（oss.endpoint/accessKeyId/secretAccessKey/bucket/internetFileUrl/maxSize）。

  Excel 能力
  - mmore-boot-excel
      - 基于 fastexcel 的 BaseExcelListener<T>，按行收集数据至 dataList。
  - mmore-boot-async-excel
      - API（mmore-boot-async-excel-api）：定义持久化 SPI 接口 AsyncTaskPersistenceApi（save/update/selectPage）与
  AsyncTaskDto/QueryDto/状态枚举。
      - Service（mmore-boot-async-excel-service）：
          - 自动装配：MmoreAsyncTaskAutoConf。
          - 注解与注册：@AsyncListener(taskCode, dataType, sheetNum) 由 AsyncListenerPostProcessor 扫描注册方法。
          - 导入/导出处理器：ImportBaseHandler（下载文件→FastExcel 解析）、ExportBaseHandler（可扩展导出），包装
  于 AsyncBaseHandler。
          - 文件输出：根据 @AsyncTaskColumn(index,width) 设置列宽，写临时文件后用 OssFileClient 上传返回 URL。
          - 业务执行：AsyncTaskServiceImpl 接收 AsyncTaskVO，落库（调用 AsyncTaskPersistenceApi），提交
  TracingExecutors 异步执行处理，最终更新状态；提供 selectPage 查询。
          - 链路/上下文：TracingExecutors 在异步线程中继续父 Span，并透传 LoginContextHolder。
          - 控制器：AsyncTaskController 暴露导入/导出/分页查询接口（受 oss.enabled 控制）。
          - 任务与类型：TaskCodeEnum 将 bizCode 归一为导入/导出类型与描述；TaskQueryParams/AsyncTaskData 等模型承载解析
  与回传。

  分布式 ID
  - mmore-boot-id
      - 自动装配：MmoreBootIdConf 加载 MmoreIdProperties（mmore.id.workerId）。
      - 生成器：MmoreIdGenerator（类 Snowflake 简化版：时间戳 + workerId + 序列，容忍 5ms 时钟回拨）。

  任务与调度（AOP 支持）
  - mmore-boot-job
      - 依赖 PowerJob Worker（任务执行端）。
      - 注解：@JobLoginContext(paramName="loginContext") 从 PowerJob 任务参数 jobParams 中提取登录上下文并写入
  LoginContextHolder（JobLoginContextAspect），便于任务代码复用租户/用户上下文逻辑。

  事件驱动（EDA / RabbitMQ）
  - mmore-cloud-eda
      - 自动装配：EdaAutoConf 导入 RabbitMqAutoCnf。
      - 动态声明：读取 mmore.eda.rabbitmq.*（RabbitMqProperties），按配置创建队列/交换机（topic/direct/fanout）/绑定，
  并通过 RabbitAdmin 显式声明。
      - 消息转换：MessageConverter 使用 Jackson2JsonMessageConverter 处理 JSON。

  典型调用链路
  - HTTP 请求 → LoginContextHandler 解析 mmore-login-context → LoginContextHolder 生效
  - DB 操作 → MyBatis-Plus 插件从 LoginContextHolder 注入租户 ID，实现透明多租户隔离
  - Feign 调用 → RequestContextInterceptor 注入 mmore-login-context 与 route 头 → 下游继续解析上下文/自定义路由
  - 异步任务 → TracingExecutors 继承父 Span + 透传 LoginContext → 导入/导出处理器执行 → 结果上传 OSS → 任务状态回写
  - 分布式锁 → 方法加 @DistributedLock（支持 SpEL/多 key）→ RedisLockHandler 通过 JetCache tryLock 实现互斥

  关键可配项速览
  - Web：mmore.swagger.*、mmore.cors.*
  - DB：mmore.tenant.*、mybatis-plus.*
  - 缓存：JetCache 相关（在外部 yml）
  - 文件：oss.*
  - ID：mmore.id.workerId
  - EDA：mmore.eda.rabbitmq.{queues,exchanges,bindings}
  - 异步 Excel：依赖 oss.enabled=true；持久化 SPI 由业务实现 AsyncTaskPersistenceApi


错误码与字典国际化（mmore-boot-i18n）
- 功能
  - 错误码国际化：基于 ApiErrorType 的 code，按资源文件返回本地化 msg。
  - 业务枚举/状态码国际化：通过注解 @I18nMapping 在响应体返回前写入本地化描述。
  - 语言解析：优先读取配置的请求头（默认 Accept-Language），兼容 en、en-US、zh-CN、zh_TW 等；可配置默认语言。
- 关键配置（application.yml）
  - mmore.i18n.basenames: 错误码资源基名（默认 classpath:error/messages）
  - mmore.i18n.dictBasenames: 业务字典资源基名（默认 classpath:dict/messages）
  - mmore.i18n.languageHeader: 自定义语言头（默认 Accept-Language）
  - mmore.i18n.defaultLocale: 无头部时的默认语言（可选，如 en）
  - mmore.i18n.fallbackToErrorMesg: 未命中文案是否回退到枚举 mesg（默认 true）
- 错误码资源示例（src/main/resources/error/messages_en.properties）
  - 000000=Success
  - -1=System error
  - 020000=Invalid arguments
  - COMMON=Business error
- 业务字典资源示例（src/main/resources/dict/messages_zh_CN.properties）
  - status.1=进行中
  - status.2=已完成
  - status.3=已关闭
- 注解用法（在响应 DTO 上）
  - @I18nMappings({ @I18nMapping(from = "status", to = "statusDesc", prefix = "status") })
  - 将 status=1 自动映射为 statusDesc=对应语言文案（字典 key：status.1）。

示例应用（mmore-example-web）
- 依赖：mmore-boot-starter、mmore-boot-i18n。
- 配置：application.yml 中可设置 mmore.i18n.*，并提供 error/* 与 dict/* 资源文件。
- 接口：
  - GET /demo/arg-error → 抛出 SystemException(020000)，返回本地化 msg。
  - GET /demo/biz-error → 抛出 BizException(COMMON)，返回本地化 msg。
  - GET /demo/order → 返回包含 status 的对象，自动填充 statusDesc（多语言）。
- 本地验证：
  - 英文：curl -H "Accept-Language: en" http://localhost:8080/demo/order
  - 简体：curl -H "Accept-Language: zh-CN" http://localhost:8080/demo/order
