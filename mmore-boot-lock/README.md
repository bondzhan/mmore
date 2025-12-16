mmore-boot-lock（分布式锁）

定位
- 提供注解式分布式锁能力，默认基于 JetCache 的 Redis 锁实现，支持单/多 key、SpEL 表达式、过期时间等。

核心组件
- 注解 `@DistributedLock`
  - `keyPrefix/key/keys[]/expire/timeUnit/lockType`
  - `keys[]` 支持固定、EL、拼接（如：`"brand:" + {#id}`）。
- 切面 `DistributedLockAspect`：解析方法参数/SpEL，支持多 key 顺序加锁，自动释放。
- 处理器 `RedisLockHandler`：通过 JetCache `tryLock` 获取分布式锁。

使用示例
```java
@DistributedLock(keyPrefix = "order:", keys = {"{#orderId}"}, expire = 60)
public void submit(Long orderId) { ... }
```

注意
- 需配置好 JetCache 的 Redis；
- 多 key 加锁按顺序获取锁，若任一失败会抛出异常并释放已持有锁。

