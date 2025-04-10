package com.github.mmore.eda.model;

import com.github.mmore.common.constant.SystemConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author Bond
 * @Date 2025/2/22
 * @Description TODO
 */
@Data
@ConfigurationProperties(prefix = SystemConstant.RABBITMQ_PREFIX)
public class RabbitMqProperties {
    private List<QueueConfig> queues;
    private List<ExchangeConfig> exchanges;
    private List<BindingConfig> bindings;

    @Data
    public static class QueueConfig {
        private String name;
        private boolean durable = false;
    }

    @Data
    public static class ExchangeConfig {
        private String name;
        private String type = "topic"; // 默认Topic类型
    }

    @Data
    public static class BindingConfig {
        private String queue;
        private String exchange;
        private String routingKey;
    }
}
