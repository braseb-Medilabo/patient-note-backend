package com.medilab.notesPatients.configuration;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Patient notes API",
version = "1.0"),
security = @SecurityRequirement(name = "bearerAuth"))


@Configuration
@SecurityScheme(name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT")   
public class SwaggerConfig {
    @Value("${api.server.gateway.url}")
    private String urlServerGateway;
    
    @Value("${api.gateway.prefix}") 
    private String prefix;
    
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server().url(urlServerGateway)
            ));
    }
    
    @Bean
    OpenApiCustomizer prefixPathsCustomizer() {
        return openApi -> {
            Paths oldPaths = openApi.getPaths();
            Paths newPaths = new Paths();
            oldPaths.forEach((path, item) -> newPaths.addPathItem(prefix + path, item));
            openApi.setPaths(newPaths);
        };
    }
}