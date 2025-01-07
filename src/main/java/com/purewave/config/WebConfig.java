package com.purewave.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL paths to the directory where your images are saved
        registry.addResourceHandler("/user-profile-images/**")
                .addResourceLocations("file:./user-profile-images/");
    }
}
