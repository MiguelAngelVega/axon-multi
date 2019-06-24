package com.github.axonmulti.common.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Scan the core artifacts: commands, events, queries from a base package
// in the core module
@Configuration
@ComponentScan(basePackages = {"com.github.axonmulti.core"})
@Slf4j
public class AxonConfig {


    // This is not needed in 4.1.1
    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }


    // Exchange

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange("axonExchange").build();
    }

    // Queue

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("axonQueue").build();
    }

    // Binding

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
    }

    // use Admin to configure at runtime

    @Autowired
    public void configure(AmqpAdmin amqpAdmin) {
        amqpAdmin.declareExchange(exchange());
        amqpAdmin.declareQueue(queue());
        amqpAdmin.declareBinding(binding());
    }

    // Listen to RabbitMQ messages

    @Bean
    public SpringAMQPMessageSource axonQueueMessageSource(AMQPMessageConverter messageConverter) {
        return new SpringAMQPMessageSource(messageConverter) {

            @RabbitListener(queues = "axonQueue")
            @Override
            public void onMessage(Message message, Channel channel) {
                log.debug("[AMQP] Processing message: {}, on channel: {}", message, channel);
                super.onMessage(message, channel);
            }
        };
    }


}