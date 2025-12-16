mmore-boot-job（任务执行上下文支持）

定位
- 为分布式任务（如 PowerJob Worker）提供“登录上下文注入”能力，便于任务代码复用 Web 场景的多租户/用户上下文逻辑。

自动装配
- `MmoreBootJobConf`：注册 `JobLoginContextAspect` 切面。

核心能力
- 注解 `@JobLoginContext(paramName="loginContext")`
  - 在被标注的方法执行前，从任务入参 JSON 中提取登录上下文写入 `LoginContextHolder`。
  - 方法执行完成后由 Web 层的拦截器/清理逻辑统一清理（或在任务结束时清理）。
- 切面 `JobLoginContextAspect`
  - 读取方法第一个参数（通常是任务上下文/参数对象），查找其中的 `jobParams` JSON 字段。
  - 在 `jobParams` 中按 `paramName`（默认 `loginContext`）提取并反序列化为 `LoginContext`，写入 `LoginContextHolder`。

任务参数约定
- 任务入参对象（或 Map / JSON 字符串）中包含字段 `jobParams`，其值为 JSON 串；例如：
```json
{
  "jobParams": "{\"loginContext\":{\"userId\":1,\"tenantId\":100,\"userIdentityId\":1,\"type\":1,\"userName\":\"admin\"}}"
}
```

使用示例
```java
@Component
public class DemoJobHandler {

  // 任务入参通常是平台回调约定的上下文/参数对象
  @JobLoginContext(paramName = "loginContext")
  public void run(SomeJobContext ctx) {
    // 已自动注入登录上下文，可直接读取
    Long tenantId = LoginContextHolder.currentTenantId();
    Long userId = LoginContextHolder.currentUserId();
    // ... 执行任务逻辑
  }
}
```

与 Web/DB 的配合
- Web：`LoginContextHolder` 的使用方式与 Web 请求一致，任务可共享同一套权限/租户逻辑。
- DB：`mmore-boot-db` 的多租户插件会从 `LoginContextHolder` 注入租户 ID，实现任务 SQL 的租户隔离。

注意
- 确保调用平台传入的任务参数中包含 `jobParams`，且内嵌 `loginContext` 字段（或按注解自定义的 `paramName`）。
- 任务方法内若异步执行子任务，需显式透传 `LoginContextHolder`（可参考 `mmore-boot-async-excel` 的 `TracingExecutors` 实现思路）。

