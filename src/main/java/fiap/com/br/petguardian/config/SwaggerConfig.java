package fiap.com.br.petguardian.config;

import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PetGuardian API",
                description = "API da Solução PetGuardian",
                version = "1.0"
        )
)
public class SwaggerConfig {

}
