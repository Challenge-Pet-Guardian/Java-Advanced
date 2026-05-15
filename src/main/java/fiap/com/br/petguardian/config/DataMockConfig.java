package fiap.com.br.petguardian.config;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoRepository;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
public class DataMockConfig {

    @Bean
    CommandLineRunner seedData(
            EnderecoRepository enderecoRepository,
            FamiliaRepository familiaRepository,
            UsuarioRepository usuarioRepository,
            PetRepository petRepository
    ) {
        return args -> {
            if (enderecoRepository.count() > 0 || familiaRepository.count() > 0 || usuarioRepository.count() > 0 || petRepository.count() > 0) {
                return;
            }

            Endereco endereco = enderecoRepository.save(
                    Endereco.builder()
                            .cep("07749180")
                            .rua("Avenida Otavio Spigarollo")
                            .bairro("Vila Rosina")
                            .cidade("Caieiras")
                            .estado("Sao Paulo")
                            .build()
            );

            Familia familia = familiaRepository.save(
                    Familia.builder()
                            .nome("Familia Mock")
                            .usuarios(new HashSet<>())
                            .pets(new HashSet<>())
                            .build()
            );

            usuarioRepository.save(
                    Usuario.builder()
                            .nome("Usuario Mock")
                            .email("usuario.mock@petguardian.com")
                            .senha("123456")
                            .telefone("11999999999")
                            .endereco(endereco)
                            .familia(familia)
                            .build()
            );

            petRepository.save(
                    Pet.builder()
                            .nome("Pet Mock")
                            .idade(3)
                            .raca("SRD")
                            .porte(PetPorte.MEDIO)
                            .sexo('M')
                            .castrado(Boolean.TRUE)
                            .familia(familia)
                            .build()
            );
        };
    }
}
