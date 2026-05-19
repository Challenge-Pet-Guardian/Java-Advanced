package fiap.com.br.petguardian.tarefa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TarefaRequest(
        @NotBlank
        String titulo,

        @NotBlank
        Integer pontosTarefa,

        @NotBlank
        String descricao,

        @NotNull
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime prazo,

        @NotNull
        Long criadorId,

        @NotNull
        Long petId,

        @NotBlank
        @EnumValidation(enumClass = EnumStatus.class)
        String status,

        Long concluinteId
) {
    public Tarefa toEntity(Usuario criador, Pet pet) {
        return Tarefa.builder()
                .titulo(titulo)
                .pontosTarefa(pontosTarefa)
                .descricao(descricao)
                .criacao(LocalDateTime.now())
                .prazo(prazo)
                .criador(criador)
                .pet(pet)
                .build();
    }
}
