package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
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
    private Pet pet;

    @ManyToOne
    private Veterinaria veterinaria;

}
