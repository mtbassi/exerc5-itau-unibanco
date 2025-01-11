package bassi.itau_unibanco.exerc1_itau_unibanco.infra.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Value("${swagger.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String applicationVersion;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(this.getInfo());
    }

    private Info getInfo() {
        return new Info().title(this.applicationName)
                .description("Este microserviço fornece uma API RESTful para gerenciar produtos. Ele permite realizar operações como criação, consulta, atualização e exclusão de produtos.")
                .version(this.applicationVersion)
                .license(new License().name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .contact(this.getContact());
    }

    private Contact getContact() {
        return new Contact().name("Matheus Bassi")
                .url("https://www.linkedin.com/in/matheusbassi/");
    }
}
