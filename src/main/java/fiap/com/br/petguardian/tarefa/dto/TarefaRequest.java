package fiap.com.br.petguardian.tarefa.dto;

import fiap.com.br.petguardian.atendimento.AtendimentoStatus;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.tarefa.StatusTarefa;
import fiap.com.br.petguardian.tarefa.Tarefa;

import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TarefaRequest(
        @NotBlank
        String titulo,

        @NotBlank
        String descricao,

        @NotNull
        LocalDateTime prazo,

        @NotNull
        @EnumValidation(enumClass =  StatusTarefa.class)
        String status,

        @NotNull
        Long usuarioId,

        @NotNull
        Long petId
) {
    public Tarefa toEntity(Usuario usuario, Pet pet) {
        return Tarefa.builder()
                .titulo(titulo)
                .descricao(descricao)
                .criacao(LocalDateTime.now())
                .prazo(prazo)
                .status(StatusTarefa.valueOf(status.toUpperCase()))
                .usuario(usuario)
                .pet(pet)
                .build();
    }
}
