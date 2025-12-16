mmore-cloud-eda（事件驱动 / RabbitMQ 动态配置）

定位
- 通过配置文件动态定义队列、交换机（topic/direct/fanout）与绑定关系，并显式声明到 RabbitMQ。
- 提供 JSON 消息转换器（Jackson2）。

自动装配
- `EdaAutoConf` → `RabbitMqAutoCnf`
  - Bean：`RabbitAdmin`、动态 `Map<String, Queue/Exchange/Binding>`、`MessageConverter`。

配置示例（application.yml）
```yaml
mmore:
  eda:
    rabbitmq:
      queues:
        - name: order.create
          durable: true
      exchanges:
        - name: order.exchange
          type: topic
      bindings:
        - queue: order.create
          exchange: order.exchange
          routingKey: order.created
```

使用
- 引入本模块后，按上述配置定义并声明资源；业务中按常规 Spring AMQP 使用 `RabbitTemplate` 发送消息。

注意
- 若配置为空，提供默认队列 `mmore-default-queue` 作为兜底；
- 仅负责“声明”与“转换器”，具体消息生产/消费仍由业务实现。

