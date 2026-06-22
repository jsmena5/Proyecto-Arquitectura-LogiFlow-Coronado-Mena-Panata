package com.ms.ruteo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";

    public static final String PEDIDO_CREADO_QUEUE = "pedido.creado.queue";
    public static final String VEHICULO_DISPONIBLE_QUEUE = "vehiculo.disponible.queue";

    public static final String PEDIDO_CREADO_ROUTING_KEY = "pedido.creado";
    public static final String VEHICULO_DISPONIBLE_ROUTING_KEY = "vehiculo.disponible";
    public static final String ENVIO_ASIGNADO_ROUTING_KEY = "envio.asignado";

    @Bean
    public DirectExchange logiflowExchange() {
        return new DirectExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Queue pedidoCreadoQueue() {
        return QueueBuilder.durable(PEDIDO_CREADO_QUEUE).build();
    }

    @Bean
    public Queue vehiculoDisponibleQueue() {
        return QueueBuilder.durable(VEHICULO_DISPONIBLE_QUEUE).build();
    }

    @Bean
    public Binding pedidoCreadoBinding() {
        return BindingBuilder
                .bind(pedidoCreadoQueue())
                .to(logiflowExchange())
                .with(PEDIDO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding vehiculoDisponibleBinding() {
        return BindingBuilder
                .bind(vehiculoDisponibleQueue())
                .to(logiflowExchange())
                .with(VEHICULO_DISPONIBLE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}