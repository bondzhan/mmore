package com.github.mmore.eda.autoconfigure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mmore.common.constant.SystemConstant;
import com.github.mmore.eda.model.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过配置文件动态定义队列、交换机和绑定，无需修改代码即可扩展。
 * @Author Bond
 * @Date 2025/2/22
 * @Description TODO
 */
@Slf4j
@AutoConfiguration
public class RabbitMqAutoCnf {

    @Bean(name = "rabbitAdmin") // 显式指定 Bean 名称
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        log.info("RabbitAdmin bean created");
        return admin;
    }

    /**
     * 从配置文件中加载以SystemConstant.RABBITMQ_PREFIX开头的属性，映射到RabbitMqProperties对象。
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = SystemConstant.RABBITMQ_PREFIX)
    public RabbitMqProperties rabbitMqProperties() {
        return new RabbitMqProperties();
    }

    /**
     * 根据RabbitMqProperties中的队列配置，动态创建多个Queue实例，并存储在Map中。
     * 支持配置队列名称和持久性（durable）
     * @param properties
     * @return
     */
    @Bean
    @DependsOn({"rabbitAdmin"})
    public Map<String, Queue> queues(RabbitMqProperties properties, RabbitAdmin rabbitAdmin) {
        Map<String, Queue> queues = new HashMap<>();
        if (properties.getQueues() == null || properties.getQueues().isEmpty()) {
            log.info("No queues configured, using default queue: mmore-default-queue");
            //提供默认的队列、交换机和绑定，作为兜底方案
            Queue defaultQueue = new Queue("mmore-default-queue", true);
            queues.put("mmore-default-queue", defaultQueue);
            return queues;
        }
        for (RabbitMqProperties.QueueConfig queueConfig : properties.getQueues()) {
            Queue queue = new Queue(queueConfig.getName(), queueConfig.isDurable());
            queues.put(queueConfig.getName(), queue);
            rabbitAdmin.declareQueue(queue); // 显式声明队列
            log.info("Created queue: {}", queueConfig.getName());
        }
        return queues;
    }

    /**
     * 根据配置动态创建多个TopicExchange实例,支持多种交换机类型
     * direct: 通过routingKey完全匹配
     * topic: 通过routingKey模糊匹配
     * fanout: 广播到所有绑定的队列
     * @param properties
     * @return
     */
    @Bean
    @DependsOn({"rabbitAdmin"})
    public Map<String, Exchange> exchanges(RabbitMqProperties properties, RabbitAdmin rabbitAdmin) {
        Map<String, Exchange> exchanges = new HashMap<>();
        if (properties.getExchanges() == null || properties.getExchanges().isEmpty()) {
            log.info("No exchanges configured in RabbitMQ properties, returning empty exchange map.");
            return exchanges; // 返回空 Map
        }
        for (RabbitMqProperties.ExchangeConfig exchangeConfig : properties.getExchanges()) {
            Exchange exchange;
            switch (exchangeConfig.getType().toLowerCase()) {
                case "direct":
                    exchange = new DirectExchange(exchangeConfig.getName());
                    break;
                case "fanout":
                    exchange = new FanoutExchange(exchangeConfig.getName());
                    break;
                case "topic":
                default:
                    exchange = new TopicExchange(exchangeConfig.getName());
                    break;
            }
            exchanges.put(exchangeConfig.getName(), exchange);
            rabbitAdmin.declareExchange(exchange); // 显式声明交换机
            log.info("Created exchange: {} with type: {}", exchangeConfig.getName(), exchangeConfig.getType());
        }
        return exchanges;
    }

    /**
     * 根据配置动态创建队列和交换机之间的绑定关系
     * @param properties
     * @param queues
     * @param exchanges
     * @return
     */
    @Bean
    @DependsOn({"queues", "exchanges","rabbitAdmin"})
    public Map<String, Binding> bindings(RabbitMqProperties properties, Map<String, Queue> queues, Map<String, Exchange> exchanges,RabbitAdmin rabbitAdmin) {
        Map<String, Binding> bindings = new HashMap<>();
        if (properties.getBindings() == null || properties.getBindings().isEmpty()) {
            log.info("No bindings configured in RabbitMQ properties, returning empty binding map.");
            return bindings; // 返回空 Map
        }
        for (RabbitMqProperties.BindingConfig bindingConfig : properties.getBindings()) {
            Queue queue = queues.get(bindingConfig.getQueue());
            Exchange exchange = exchanges.get(bindingConfig.getExchange());
            if (queue == null || exchange == null) {
                log.error("Invalid binding config: queue {} or exchange {} not found", bindingConfig.getQueue(), bindingConfig.getExchange());
                continue;
            }
            Binding binding;
            if (exchange instanceof FanoutExchange) {
                // Fanout 不需要 routingKey
                binding = BindingBuilder.bind(queue).to((FanoutExchange) exchange);
            } else if (exchange instanceof DirectExchange) {
                binding = BindingBuilder.bind(queue).to((DirectExchange) exchange).with(bindingConfig.getRoutingKey());
            } else { // 默认 TopicExchange
                binding = BindingBuilder.bind(queue).to((TopicExchange) exchange).with(bindingConfig.getRoutingKey());
            }
            bindings.put(bindingConfig.getQueue() + "-" + bindingConfig.getExchange(), binding);
            rabbitAdmin.declareBinding(binding); // 显式声明绑定
            log.info("Created binding: {} to {} with routingKey: {}", queue.getName(), exchange.getName(), bindingConfig.getRoutingKey());
        }
        return bindings;
    }

    /**
     * 配置JSON消息转换器，支持将对象序列化为JSON并反序列化
     * 使用Jackson2JsonMessageConverter处理JSON格式。
     * ContentTypeDelegatingMessageConverter支持多种内容类型。
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
    }

}
