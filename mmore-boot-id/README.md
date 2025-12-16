mmore-boot-id（分布式 ID 生成）

定位
- 生成 14 位十六进制的分布式 ID（时间戳 + workerId + 序列），容忍小幅时钟回拨。

核心组件
- `MmoreIdGenerator`：`nextId()` / `nextId(prefix)`
- 配置 `mmore.id.workerId`（集群唯一）

配置示例
```yaml
mmore:
  id:
    workerId: 1
```

使用示例
```java
@Resource MmoreIdGenerator idGen;
String id = idGen.nextId();
String id2 = idGen.nextId("ORDER"); // ORDER-xxxx
```

注意
- 若出现时钟回拨，内置等待与阈值控制；超过阈值将抛出异常以避免重复。

