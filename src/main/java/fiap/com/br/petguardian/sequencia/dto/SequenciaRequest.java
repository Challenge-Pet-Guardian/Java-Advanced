package fiap.com.br.petguardian.sequencia.dto;

import fiap.com.br.petguardian.sequencia.Sequencia;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record SequenciaRequest(
        @NotNull
        @PositiveOrZero
        Integer sequenciaAtual,

        @NotNull
        @PositiveOrZero
        Integer sequenciaMaxima,

        LocalDate dataUltimaAtividade,

        @NotNull
        Long usuarioId
) {
    public Sequencia toEntity(Usuario usuario) {
        return Sequencia.builder()
                .SequenciaAtual(sequenciaAtual)
                .SequenciaMaxima(sequenciaMaxima)
                .DataUltimaAtividade(dataUltimaAtividade)
                .usuario(usuario)
                .build();
    }
}
