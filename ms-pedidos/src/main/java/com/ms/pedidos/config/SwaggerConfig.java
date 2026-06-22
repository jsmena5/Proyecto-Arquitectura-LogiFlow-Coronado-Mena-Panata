package com.ms.pedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pedidosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API ms-pedidos")
                        .version("1.0.0")
                        .description("Microservicio REST para la gestión de pedidos y publicación de eventos RabbitMQ"));
    }
}