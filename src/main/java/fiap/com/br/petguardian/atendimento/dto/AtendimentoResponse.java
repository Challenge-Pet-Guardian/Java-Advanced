package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.atendimento.AtendimentoStatus;
import fiap.com.br.petguardian.atendimento.TipoAtendimento;

import java.time.LocalDateTime;

public record AtendimentoResponse(
        Long id,
        TipoAtendimento tipoAtendimento,
        LocalDateTime data,
        String anotacoes,
        AtendimentoStatus status,
        Double valor,
        Long petId,
        Long veterinariaId
) {
    public static AtendimentoResponse fromEntity(Atendimento atendimento) {
        return new AtendimentoResponse(
                atendimento.getId(),
                atendimento.getTipo(),
                atendimento.getData(),
                atendimento.getAnotacoes(),
                atendimento.getStatus(),
                atendimento.getValor(),
                atendimento.getPet().getId(),
                atendimento.getVeterinaria().getId()
        );
    }
}
