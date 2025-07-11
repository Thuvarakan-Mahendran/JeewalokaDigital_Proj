package com.jeewaloka.digital.jeewalokadigital.configaration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String OPTIONS = "OPTIONS";

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://frontend:80")
                        .allowedMethods(GET, POST, PUT, DELETE, OPTIONS)
                        .allowedHeaders("*")
                        .allowCredentials(true); // Enable cookies and Authorization headers
            }
        };
    }
}