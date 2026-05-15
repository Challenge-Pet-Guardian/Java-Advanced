package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;

import java.util.Set;

public record FamiliaResponse(
        Long id,
        String nome,
        Set<Long> usuarioIds,
        Set<Long> petIds,
        Set<Long> tarefaIds,
        Set<Long> atendimentoIds
) {
    public static FamiliaResponse fromEntity(Familia familia, Set<Long> usuarioIds, Set<Long> petIds, Set<Long> tarefaIds, Set<Long> atendimentoIds) {
        return new FamiliaResponse(
                familia.getId(),
                familia.getNome(),
                usuarioIds,
                petIds,
                tarefaIds,
                atendimentoIds
        );
    }
}
