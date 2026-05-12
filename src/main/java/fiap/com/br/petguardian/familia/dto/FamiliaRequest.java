package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public record FamiliaRequest(
        @NotBlank
        String nome,

        @NotEmpty
        Set<Usuario> usuarioIds,

        @NotEmpty
        Set<Pet> petIds
) {
    public Familia toEntity() {
        return Familia.builder()
                .nome(nome)
                .usuarios(isNull(usuarioIds) ? new HashSet<>() : usuarioIds)
                .pets(isNull(petIds) ? new HashSet<>() : petIds)
                .build();
    }
}
