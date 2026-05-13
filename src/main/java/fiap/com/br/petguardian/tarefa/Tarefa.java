package fiap.com.br.petguardian.tarefa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tarefas")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private LocalDateTime criacao;
    private LocalDateTime prazo;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @ManyToOne
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JsonIgnore
    private Pet pet;
}
