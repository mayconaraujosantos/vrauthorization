package com.vrauth.vrauthorization.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title("Mini-Autorizador")
                .description("Autorizador de transações para cartões - VR")
                .version("1.0.0")
                .summary("summary")
                .termsOfService("termsOfService")
                .contact(getContact())
                .license(getLicense());
    }

    private Contact getContact() {
        return new Contact()
                .name("Maycon Santos")
                .email("maycon.araujo.santos@gmail.com")
                .url("http://localhost:8080/");
    }

    private License getLicense() {
        return new License()
                .identifier("license")
                .name("license")
                .url("license");
    }

}
