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

    private String tipo_atendimento;
    private LocalDateTime data;
    private String anotacoes;
    private String status;
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "veterinaria_id")
    private Veterinaria veterinaria;

}
