package fiap.com.br.petguardian.veterinaria.dto;

import fiap.com.br.petguardian.veterinaria.Veterinaria;

public record VeterinariaResponse(
        Long id,
        String nome,
        String telefone,
        Long enderecoId
) {
    public static VeterinariaResponse fromEntity(Veterinaria veterinaria) {
        return new VeterinariaResponse(
                veterinaria.getId(),
                veterinaria.getNome(),
                veterinaria.getTelefone(),
                veterinaria.getEndereco().getId()
        );
    }
}
