package fiap.com.br.petguardian.sequencia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.familia.Familia;
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
@Table(name = "sequencia")
public class Sequencia {
    @Id
    @Column(name = "familia_id_familia")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "familia_id_familia")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Familia familia;

    @Column(name = "sequencia_atual", nullable = false)
    private Integer sequenciaAtual;

    @Column(name = "sequencia_maxima", nullable = false)
    private Integer sequenciaMaxima;

    @Column(name = "data_ultima_acao", nullable = false)
    private LocalDateTime dataUltimaAtividade;
}
