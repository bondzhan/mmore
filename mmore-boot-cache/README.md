mmore-boot-cache（JetCache 封装）

定位
- 基于 JetCache 提供方法缓存启用与统一缓存模板，预置常用 Cache 名称与区域。

自动装配
- `MmoreBootCacheConf`：启用 `@EnableMethodCache(basePackages = "com.github.mmore")`。
- `JetCacheManagerAutoConf`：创建默认区域 `default` 下的预置缓存：
  - `userCache:`、`commonCache:`、`productCache:`、`orderCache:`、`settleCache:`、`massageCache:`

使用方式
- 依赖 JetCache 的 Redis 配置（外部 yml）。
- 注入并使用 `JetCacheTemplate`：
  - `get/put/remove/putAll`，支持带/不带 area 两种写法。
- 在业务方法使用 JetCache 注解或调用模板统一操作。

示例
```java
@Resource JetCacheTemplate cache;
cache.put(CacheNameConstant.COMMON_CACHE, "k1", "v1");
String v = cache.get(CacheNameConstant.COMMON_CACHE, "k1");
```

注意
- 预置缓存按名称硬编码创建，可按需扩展配置化创建策略。

