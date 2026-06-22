package com.ms.pedidos.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
// 1. Cambia el import a la nueva clase
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";

    public static final String PEDIDO_CREADO_ROUTING_KEY = "pedido.creado";
    public static final String PEDIDO_CANCELADO_ROUTING_KEY = "pedido.cancelado";

    @Bean
    public DirectExchange logiflowExchange() {
        return new DirectExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // 2. Instancia la nueva clase aquí
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}