package com.lzb.wishlist.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wishlist Service API")
                        .version("1.0.0")
                        .description("API for Wishlist management in e-commerce")
                        .contact(new Contact()
                                .name("Wagner Costa")
                                .email("dev.wagnercosta@icloud.com")
                                .url("https://www.fake.lzb.wishlist.com")));
    }
}
