package fiap.com.br.petguardian.endereco.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EnderecoRequest(
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "CEP deve estar no formato 00000000.")
        String cep,

        @NotNull
        int numero
) {
    public Endereco toEntity(String rua, String bairro, String cidade, String estado) {
        return Endereco.builder()
                .cep(cep)
                .numero(numero)
                .rua(rua)
                .bairro(bairro)
                .cidade(cidade)
                .estado(estado)
                .build();
    }
}
