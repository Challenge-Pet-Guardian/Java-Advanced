package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public record FamiliaRequest(
        @NotBlank
        String nome,

        Set<Long> usuarioIds,

        Set<Long> petIds
) {
    public Familia toEntity(Set<Usuario> usuarios, Set<Pet> pets) {
        return Familia.builder()
                .nome(nome)
                .usuarios(isNull(usuarios) ? new HashSet<>() : usuarios)
                .pets(isNull(pets) ? new HashSet<>() : pets)
                .build();
    }
}
