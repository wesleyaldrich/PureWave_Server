package com.purewave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL paths to the directory where your images are saved
        registry.addResourceHandler("/user-profile-images/**")
                .addResourceLocations("file:./user-profile-images/");

        // Map the 'uploads-audio/dry/' folder to be served as static content
        registry.addResourceHandler("/audio/files/dry/**")
                .addResourceLocations("file:./uploads-audio/dry/");

        // Map the 'uploads-audio/wet/' folder to be served as static content
        registry.addResourceHandler("/audio/files/wet/**")
                .addResourceLocations("file:./uploads-audio/wet/");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow CORS on all endpoints
                        .allowedOrigins("http://localhost:5173") // Specify your frontend address
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed HTTP methods
                        .allowCredentials(true);
            }
        };
    }
}
