package com.ms.ruteo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ruteoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API ms-ruteo")
                        .version("1.0.0")
                        .description("Microservicio REST para asignación y optimización simple de rutas"));
    }
}