mmore-boot-i18n 模块

功能概览
- 错误码国际化：基于 `ApiErrorType` 的 `code`，按资源文件映射返回本地化 `msg`。
- 业务字典/枚举国际化：通过注解在响应体返回前，将 `code` 自动转换为本地化描述（支持集合、数组、Map、嵌套对象）。
- 语言解析：优先读取自定义请求头（默认 `Accept-Language`），兼容 `en`、`en-US`、`zh-CN`、`zh_TW` 等格式；可配置默认语言。

集成与装配
- 依赖引入：在业务服务 POM 添加 `mmore-boot-i18n`。
- 自动装配：已通过 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 注册。
- 组件清单：
  - `I18nLocaleInterceptor`：解析请求语言并设置 `LocaleContextHolder`。
  - `ErrorMessageResolver`：按错误码解析本地化文案。
  - `I18nExceptionHandlerAdvice`：优先拦截 `SystemException/BizException`，返回本地化的 `ApiResult`。
  - `I18nDictResolver`：按注解将业务 `code` 转换为本地化描述。
  - `I18nResponseBodyAdvice`：在响应返回前递归处理数据结构，应用字典翻译。

配置项（application.yml）
- `mmore.i18n.basenames`：错误码资源基名，默认 `classpath:error/messages`。
- `mmore.i18n.dictBasenames`：业务字典资源基名，默认 `classpath:dict/messages`。
- `mmore.i18n.languageHeader`：自定义语言头，默认 `Accept-Language`。
- `mmore.i18n.defaultLocale`：无头部时的默认语言（可选，如 `en`）。
- `mmore.i18n.fallbackToErrorMesg`：错误码未命中文案时是否回退到枚举 `mesg`（默认 `true`）。

错误码国际化使用
- 资源文件位置：`src/main/resources/error/`
- 基名示例：
  - `error/messages_en.properties`
  - `error/messages_zh_CN.properties`
- 内容示例（键为错误码）：
  - `000000=Success`
  - `-1=System error`
  - `020000=Invalid arguments`
  - `COMMON=Business error`
- 抛出异常（示例）：
  - `throw new SystemException(SystemErrorType.ARGUMENT_NOT_VALID);`
  - `throw new BizException(BizErrorType.COMMON);`
- 返回效果：由 `I18nExceptionHandlerAdvice` 生成 `ApiResult`，`msg` 为本地化文本，`code` 保持不变。

业务字典/枚举国际化使用（注解）
- 注解定义：
  - `@I18nMapping(from, to, prefix, basenames)`：`from` 为 code 字段，`to` 为描述字段，`prefix` 为字典前缀（拼出 key=prefix.code），`basenames` 可覆盖默认字典基名。
  - `@I18nMappings`：支持多个映射同贴到一个 DTO 上。
- DTO 示例：
  - `@I18nMappings({ @I18nMapping(from="status", to="statusDesc", prefix="status") })`
  - `private Integer status; private String statusDesc;`
- 字典资源位置：`src/main/resources/dict/`
- 内容示例：
  - `dict/messages_en.properties`：`status.1=In progress`、`status.2=Completed`、`status.3=Closed`
  - `dict/messages_zh_CN.properties`：`status.1=进行中`、`status.2=已完成`、`status.3=已关闭`
- 返回效果：`I18nResponseBodyAdvice` 会在响应返回前将 `status` → `statusDesc` 自动填充对应语言文案。

语言识别与回退
- 优先读取 `mmore.i18n.languageHeader` 指定的头；若缺失，回退读取 `Accept-Language`。
- 格式兼容：`en`、`en-US`、`en_US`、`zh-CN`、`zh_TW` 等（内部转换为语言标签）。
- 无头场景：如配置了 `mmore.i18n.defaultLocale`，将使用该语言；否则维持当前线程 Locale。
- 未命中键：使用键本身作为文案；若是错误码且 `fallbackToErrorMesg=true`，回退到枚举 `mesg`。

示例请求
- `curl -H "Accept-Language: en" http://localhost:8080/demo/arg-error` → 错误码英文文案
- `curl -H "Accept-Language: zh-CN" http://localhost:8080/demo/order` → `statusDesc=进行中`

常见问题
- 注入冲突：容器存在默认 `messageSource` 与自定义 `errorMessageSource`，本模块已通过 `@Qualifier("errorMessageSource")` 精确注入，避免冲突。
- 自动装配未生效：确认依赖已引入、日志级别为 info 以上，并检查是否加载 `AutoConfiguration.imports`。
- 资源未命中：确认 basenames 配置、文件路径、键名是否与错误码/字典 key 对应。

