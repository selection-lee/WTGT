package com.ssafy.wattagatta.global.config;

import com.ssafy.wattagatta.domain.product.dto.request.ProductInfoMessage;
import com.ssafy.wattagatta.domain.product.listener.ProductInfoListener;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;


    /**
     * Exchange 설정
     */
    @Bean
    public DirectExchange taskExchange() {
        return new DirectExchange("task_exchange");
    }

    /**
     * Queue 설정
     */
    @Bean
    public Queue taskQueue() {
        return new Queue("task_queue", false);
    }

    /**
     * 큐, Exchange 바인딩
     */
    @Bean
    public Binding binding(Queue taskQueue, DirectExchange taskExchange) {
        return BindingBuilder.bind(taskQueue).to(taskExchange).with("task.routing.key");
    }

    /**
     * Json 메시지 변환기 설정
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(new DefaultClassMapper() {{
            setDefaultType(ProductInfoMessage.class);
        }});
        return converter;
    }

    /**
     * ConnectionFactory 설정
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * MessageListener 설정
     */
    @Bean
    public SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory,
                                                            ProductInfoListener productInfoListener,
                                                            MessageConverter messageConverter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("task_queue");

        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(productInfoListener, "receiveProductInfo");
        listenerAdapter.setMessageConverter(messageConverter);
        container.setMessageListener(listenerAdapter);

        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setAutoStartup(true);
        return container;
    }

}
