package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.atendimento.StatusAtendimento;
import fiap.com.br.petguardian.atendimento.TipoAtendimento;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.tarefa.StatusTarefa;
import fiap.com.br.petguardian.validation.EnumValidation;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

public record AtendimentoRequest(
        @NotNull
        @EnumValidation(enumClass =  TipoAtendimento.class)
        String tipo,

        @NotNull
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime data,

        @Size(max = 300)
        String anotacoes,

        @EnumValidation(enumClass =  StatusAtendimento.class)
        String status,

        @NotNull
        @PositiveOrZero
        Double valor,

        @NotNull
        Long petId,

        @NotNull
        Long veterinariaId,

        @NotNull
        Long familiaId
) {
    public Atendimento toEntity(Familia familia, Pet pet, Veterinaria veterinaria) {
        return Atendimento.builder()
                .tipo(TipoAtendimento.valueOf(tipo.toUpperCase()))
                .data(data)
                .anotacoes(anotacoes)
                .status(isNull(status) || status.isBlank() ? StatusAtendimento.PENDENTE : StatusAtendimento.valueOf(status.toUpperCase()))
                .valor(valor)
                .familia(familia)
                .pet(pet)
                .veterinaria(veterinaria)
                .build();
    }
}
