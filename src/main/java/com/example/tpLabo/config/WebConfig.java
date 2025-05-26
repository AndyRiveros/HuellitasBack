package com.example.tpLabo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get("src/main/resources/static/img").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/img/**")
                .addResourceLocations(uploadDir);
    }
}
