package org.afdb.aikp.shared.web;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("AIKP Platform API")
                        .version("1.0.0")
                        .description("African Infrastructure Knowledge Platform REST API")
                        .contact(new Contact()
                                .name("African Development Bank")))

                .externalDocs(new ExternalDocumentation()
                        .description("AIKP Documentation"));

    }

}