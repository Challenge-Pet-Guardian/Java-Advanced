package fiap.com.br.petguardian.sequencia.dto;

import fiap.com.br.petguardian.sequencia.Sequencia;
import java.time.LocalDateTime;

public record SequenciaResponse(
        Long id,
        Long familiaId,
        Integer sequenciaAtual,
        Integer sequenciaMaxima,
        LocalDateTime dataUltimaAtividade
) {
    public static SequenciaResponse fromEntity(Sequencia sequencia) {
        return new SequenciaResponse(
                sequencia.getId(),
                sequencia.getFamilia().getId(),
                sequencia.getSequenciaAtual(),
                sequencia.getSequenciaMaxima(),
                sequencia.getDataUltimaAtividade()
        );
    }
}
