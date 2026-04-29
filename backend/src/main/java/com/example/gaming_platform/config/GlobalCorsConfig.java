package com.example.gaming_platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Allows this server to communicate with the front end which uses a different port
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")  // ✅ FIXED LINE
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS", "PUT", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}