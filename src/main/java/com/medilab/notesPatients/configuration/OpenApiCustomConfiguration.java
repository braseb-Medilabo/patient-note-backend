package com.medilab.notesPatients.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiCustomConfiguration {
    
    @Value("${api.server.gateway.url}")
    private String urlServerGateway;
    
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server().url(urlServerGateway)
            ));
    }
}
