package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

public record FamiliaResponse(
        Long id,
        String nome,
        Set<Long> usuarioIds,
        Set<Long> petIds
) {
    public static FamiliaResponse fromEntity(Familia familia) {
        return new FamiliaResponse(
                familia.getId(),
                familia.getNome(),
                familia.getUsuarios().stream().map(Usuario::getId).collect(Collectors.toSet()),
                familia.getPets().stream().map(Pet::getId).collect(Collectors.toSet())
        );
    }
}
