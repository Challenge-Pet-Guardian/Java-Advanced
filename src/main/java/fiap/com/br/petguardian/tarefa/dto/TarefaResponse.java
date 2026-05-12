package fiap.com.br.petguardian.tarefa.dto;

import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.tarefa.StatusTarefa;

import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime criacao,
        LocalDateTime prazo,
        StatusTarefa status,
        Long usuarioId,
        Long petId
) {
    public static TarefaResponse fromEntity(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getCriacao(),
                tarefa.getPrazo(),
                tarefa.getStatus(),
                tarefa.getUsuario().getId(),
                tarefa.getPet().getId()
        );
    }
}
