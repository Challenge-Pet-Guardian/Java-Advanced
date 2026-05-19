package fiap.com.br.petguardian.tarefa.dto;

import fiap.com.br.petguardian.tarefa.Tarefa;

import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        String titulo,
        Integer pontosTarefa,
        String descricao,
        LocalDateTime criacao,
        LocalDateTime prazo,
        String status,
        Long familiaId,
        Long criadorId,
        Long concluinteId,
        Long petId
) {
    public static TarefaResponse fromEntity(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getPontosTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriacao(),
                tarefa.getPrazo(),
                tarefa.getStatus().getNome_status().name(),
                tarefa.getCriador().getFamilia().getId(),
                tarefa.getCriador().getId(),
                tarefa.getConcluinte() == null ? null : tarefa.getConcluinte().getId(),
                tarefa.getPet().getId()
        );
    }
}
