package fiap.com.br.petguardian.atendimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "atendimentos")
public class Atendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoAtendimento tipo;

    private LocalDateTime data;
    private String anotacoes;

    @Enumerated(EnumType.STRING)
    private AtendimentoStatus status;

    private Double valor;

    @ManyToOne
    @JsonIgnore
    private Pet pet;

    @ManyToOne
    @JsonIgnore
    private Veterinaria veterinaria;

}
