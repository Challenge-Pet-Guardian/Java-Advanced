package fiap.com.br.petguardian.veterinaria.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeterinariaRequest(
        @NotBlank
        String nome,

        @NotBlank
        String telefone,

        @NotNull
        Long enderecoId
) {
    public Veterinaria toEntity(Endereco endereco) {
        return Veterinaria.builder()
                .nome(nome)
                .telefone(telefone)
                .endereco(endereco)
                .build();
    }
}
