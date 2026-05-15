package fiap.com.br.petguardian.sequencia.dto;

import fiap.com.br.petguardian.sequencia.Sequencia;
import java.time.LocalDate;

public record SequenciaResponse(
        Long id,
        Long usuarioId,
        Integer sequenciaAtual,
        Integer sequenciaMaxima,
        LocalDate dataUltimaAtividade
) {
    public static SequenciaResponse fromEntity(Sequencia sequencia) {
        return new SequenciaResponse(
                sequencia.getId(),
                sequencia.getUsuario().getId(),
                sequencia.getSequenciaAtual(),
                sequencia.getSequenciaMaxima(),
                sequencia.getDataUltimaAtividade()
        );
    }
}
