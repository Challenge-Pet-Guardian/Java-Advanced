package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;

import fiap.com.br.petguardian.tipoatendimento.EnumTipoAtendimento;

import java.time.LocalDateTime;

public record AtendimentoResponse(
        Long id,
        EnumTipoAtendimento tipoAtendimento,
        LocalDateTime data,
        String anotacoes,
        String status,
        Double valor,
        Long familiaId,
        Long petId,
        Long veterinariaId
) {
    public static AtendimentoResponse fromEntity(Atendimento atendimento) {
        return new AtendimentoResponse(
                atendimento.getId(),
                atendimento.getTipo().getTipoAtendimento(),
                atendimento.getData(),
                atendimento.getAnotacoes(),
                atendimento.getStatus().getNome_status().name(),
                atendimento.getValor(),
                atendimento.getPet().getUsuarioPets().stream()
                        .findFirst()
                        .map(up -> up.getUsuario().getFamilia().getId())
                        .orElse(null),
                atendimento.getPet().getId(),
                atendimento.getVeterinaria().getId()
        );
    }
}
