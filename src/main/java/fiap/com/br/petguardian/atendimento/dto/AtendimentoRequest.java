package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.atendimento.AtendimentoStatus;
import fiap.com.br.petguardian.atendimento.TipoAtendimento;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.validation.EnumValidation;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AtendimentoRequest(
        @NotNull
        @EnumValidation(enumClass =  TipoAtendimento.class)
        String tipo,

        @NotNull
        LocalDateTime data,

        @Size(max = 300)
        String anotacoes,

        @NotNull
        @EnumValidation(enumClass =  AtendimentoStatus.class)
        String status,

        @NotNull
        @PositiveOrZero
        Double valor,

        @NotNull
        Long petId,

        @NotNull
        Long veterinariaId
) {
    public Atendimento toEntity(Pet pet, Veterinaria veterinaria) {
        return Atendimento.builder()
                .tipo(TipoAtendimento.valueOf(tipo.toUpperCase()))
                .data(data)
                .anotacoes(anotacoes)
                .status(AtendimentoStatus.valueOf(status.toUpperCase()))
                .valor(valor)
                .pet(pet)
                .veterinaria(veterinaria)
                .build();
    }
}
