package fiap.com.br.petguardian.sequencia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sequencias")
public class Sequencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Usuario usuario;

    @Column(nullable = false)
    private Integer SequenciaAtual;

    @Column(nullable = false)
    private Integer SequenciaMaxima;

    private LocalDate DataUltimaAtividade;
}
