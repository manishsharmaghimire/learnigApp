package com.elearn.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

/**
 * Configures OpenAPI (Swagger) documentation for the application.
 */
@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(Collections.singletonList(new Server().url("http://localhost:" + serverPort)))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .security(Collections.singletonList(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)))
                .info(new Info()
                        .title("E-Learning Platform API")
                        .version("1.0")
                        .description("API documentation for E-Learning Platform")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@elearn.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/api/v1/auth/**")
                .packagesToScan("com.elearn.controller")
                .build();
    }
    
    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .packagesToScan("com.elearn.controller")
                .build();
    }
    
    @Bean
    public GroupedOpenApi userApis() {
        return GroupedOpenApi.builder()
                .group("user-apis")
                .pathsToMatch("/api/v1/user/**")
                .packagesToScan("com.elearn.controller")
                .build();
    }
}
