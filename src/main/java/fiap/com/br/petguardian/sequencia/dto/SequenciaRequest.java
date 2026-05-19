package fiap.com.br.petguardian.sequencia.dto;

import fiap.com.br.petguardian.sequencia.Sequencia;
import fiap.com.br.petguardian.familia.Familia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record SequenciaRequest(
        @NotNull
        @PositiveOrZero
        Integer sequenciaAtual,

        @NotNull
        @PositiveOrZero
        Integer sequenciaMaxima,

        LocalDateTime dataUltimaAtividade,

        @NotNull
        Long familiaId
) {
    public Sequencia toEntity(Familia familia) {
        return Sequencia.builder()
                .sequenciaAtual(sequenciaAtual)
                .sequenciaMaxima(sequenciaMaxima)
                .dataUltimaAtividade(dataUltimaAtividade)
                .familia(familia)
                .build();
    }
}
