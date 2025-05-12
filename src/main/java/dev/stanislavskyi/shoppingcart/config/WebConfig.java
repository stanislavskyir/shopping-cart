package dev.stanislavskyi.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // разрешаем все пути
            .allowedOrigins("http://localhost:3000") // разрешаем React (frontend)
            .allowedMethods("*") // разрешаем все HTTP-методы (GET, POST и т.д.)
            .allowedHeaders("*"); // разрешаем любые заголовки
    }
}