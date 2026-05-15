package fiap.com.br.petguardian.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PetGuardian API",
                description = "API da Solucao PetGuardian",
                version = "1.0"
        ),
        tags = {
                @Tag(name = "Usuarios"),
                @Tag(name = "Familias"),
                @Tag(name = "Pets"),
                @Tag(name = "Tarefas"),
                @Tag(name = "Atendimentos"),
                @Tag(name = "Veterinarias"),
                @Tag(name = "Enderecos"),
                @Tag(name = "Sequencias")
        }
)
public class SwaggerConfig {
}
