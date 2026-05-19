package fiap.com.br.petguardian.atendimento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import fiap.com.br.petguardian.tipoatendimento.TipoAtendimento;
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
@Table(name = "atendimento")
public class Atendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atendimento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_atend_id_tipo_atend", nullable = false)
    private TipoAtendimento tipo;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false, length = 300)
    private String anotacoes;

    @ManyToOne
    @JoinColumn(name = "status_id_status", nullable = false)
    private Status status;

    @Column(nullable = false)
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "pet_id_pet", nullable = false)
    @JsonIgnore
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "veterinaria_id_veterinaria", nullable = false)
    @JsonIgnore
    private Veterinaria veterinaria;

}
