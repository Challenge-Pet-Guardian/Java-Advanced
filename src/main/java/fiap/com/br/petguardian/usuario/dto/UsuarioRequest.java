package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.usuario.Usuario;

import fiap.com.br.petguardian.validation.DddValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String senha,

        @NotBlank
        @DddValidation
        String ddd,

        @NotBlank
        @Pattern(regexp = "\\d{9}", message = "Número de telefone deve conter exatamente 9 dígitos numéricos.")
        String numeroTelefone,

        @NotNull
        @Valid
        EnderecoRequest endereco,

        @NotNull
        Long familiaId
) {
    public Usuario toEntity(Telefone telefone, Endereco endereco, Familia familia) {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .telefone(telefone)
                .endereco(endereco)
                .familia(familia)
                .build();
    }
}
