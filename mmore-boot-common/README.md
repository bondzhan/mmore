mmore-boot-common（通用模型与工具）

定位
- 提供通用异常体系、分页模型、登录上下文、能力模式与常用工具，供各业务模块复用。

核心能力
- 异常与错误码
  - 接口 `ApiErrorType`：定义 `code/mesg`。
  - 系统错误 `SystemErrorType`、`SystemException`；业务错误 `BizErrorType`、`BizException`。
  - 与 Web 模块的全局异常处理配合，统一返回 `ApiResult`。
- 登录上下文
  - `LoginContext` + `LoginContextHolder`（InheritableThreadLocal）。
  - 便捷方法：`currentUserId/currentTenantId/...`。
- 分页
  - `PageParam/PageResponse`。
- 能力模式（策略集）
  - `BaseAbility/AbilityFactory/AbilityInvoker`：按 provider 选择实现，支持扩展。

示例
- 抛出系统异常：`throw new SystemException(SystemErrorType.ARGUMENT_NOT_VALID);`
- 抛出业务异常：`throw new BizException(BizErrorType.COMMON, "库存不足");`

注意
- 与 `mmore-boot-web` 搭配使用获得统一异常返回；
- 与 `mmore-boot-db` 搭配可让 DB 插件读取 `LoginContextHolder` 的租户与用户信息。

