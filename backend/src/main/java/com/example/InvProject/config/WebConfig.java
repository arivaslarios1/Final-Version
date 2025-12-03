package com.example.InvProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files from /backend/uploads/
        String path = System.getProperty("user.dir") + "/backend/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}