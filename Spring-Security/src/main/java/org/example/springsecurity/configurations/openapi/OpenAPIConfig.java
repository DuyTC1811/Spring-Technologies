package org.example.springsecurity.configurations.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${springdoc.api-docs.url-product}")
    private String productUrl;

    @Value("${springdoc.api-docs.url-dev}")
    private String devUrl;

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(productUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("duytran.81811@gmail.com");
        contact.setName("DuyTC");
        contact.setUrl("https://github.com/DuyTC1811");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Security API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.")
                .termsOfService("https://github.com/DuyTC1811")
                .license(mitLicense);

        ExternalDocumentation externalDocumentation = new ExternalDocumentation();
        externalDocumentation.setDescription("DEV");
        externalDocumentation.setUrl("https://springshop.wiki.github.org/docs");

        // Tạo SecurityRequirement cho Bearer Authentication WEB
        SecurityRequirement securityRequirementWeb = new SecurityRequirement();
        securityRequirementWeb.addList("Bearer Authentication WEB");

        // Tạo SecurityRequirement cho Bearer Authentication APP
        SecurityRequirement securityRequirementApp = new SecurityRequirement();
        securityRequirementApp.addList("Bearer Authentication APP");

        // Tạo SecurityScheme cho Bearer Authentication WEB
        SecurityScheme schemeWeb = new SecurityScheme();
        schemeWeb.setBearerFormat("JWT");
        schemeWeb.setScheme("bearer");
        schemeWeb.setType(SecurityScheme.Type.HTTP);
        schemeWeb.setDescription("Authentication for WEB clients");

        // Tạo SecurityScheme cho Bearer Authentication APP
        SecurityScheme schemeApp = new SecurityScheme();
        schemeApp.setBearerFormat("JWT");
        schemeApp.setScheme("bearer");
        schemeApp.setType(SecurityScheme.Type.HTTP);
        schemeApp.setDescription("Authentication for APP clients");

        Components components = new Components();
        components.addSecuritySchemes("Bearer Authentication WEB", schemeWeb);
        components.addSecuritySchemes("Bearer Authentication APP", schemeApp);

        return new OpenAPI()
                .info(info)
                .externalDocs(externalDocumentation)
                .addSecurityItem(securityRequirementWeb)
                .addSecurityItem(securityRequirementApp)
                .components(components)
                .servers(List.of(devServer, prodServer));
    }
}
