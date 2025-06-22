package com.nextsteprecommendengine.api.service.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Value("${rabbitmq.queue.process-cv}")
    private String processCvQueue;
    @Value("${rabbitmq.queue.complete-process-cv}")
    private String completeProcessCvQueue;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    @Bean
    public Queue ProcessEmbeddingQueue() {
        return new Queue(processCvQueue, true);
    }

    @Bean
    public Queue completeProcessEmbeddingQueue() {
        return new Queue(completeProcessCvQueue, true);
    }
}
