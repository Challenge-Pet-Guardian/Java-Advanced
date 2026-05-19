package fiap.com.br.petguardian.veterinaria.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VeterinariaRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Size(max = 2)
        String ddd,

        @NotBlank
        @Size(max = 9)
        String numeroTelefone,

        @NotNull
        @Valid
        EnderecoRequest endereco
) {
    public Veterinaria toEntity(Telefone telefone, Endereco endereco) {
        return Veterinaria.builder()
                .nome(nome)
                .telefone(telefone)
                .endereco(endereco)
                .build();
    }
}
