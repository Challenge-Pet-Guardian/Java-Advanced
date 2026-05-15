package fiap.com.br.petguardian.tarefa.dto;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.tarefa.StatusTarefa;
import fiap.com.br.petguardian.tarefa.Tarefa;

import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static java.util.Objects.isNull;

public record TarefaRequest(
        @NotBlank
        String titulo,

        @NotBlank
        String descricao,

        @NotNull
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime prazo,

        @EnumValidation(enumClass =  StatusTarefa.class)
        String status,

        @NotNull
        Long usuarioId,

        @NotNull
        Long petId,

        @NotNull
        Long familiaId
) {
    public Tarefa toEntity(Familia familia, Usuario usuario, Pet pet) {
        return Tarefa.builder()
                .titulo(titulo)
                .descricao(descricao)
                .criacao(LocalDateTime.now())
                .prazo(prazo)
                .status(isNull(status) || status.isBlank() ? StatusTarefa.PENDENTE : StatusTarefa.valueOf(status.toUpperCase()))
                .familia(familia)
                .usuario(usuario)
                .pet(pet)
                .build();
    }
}
