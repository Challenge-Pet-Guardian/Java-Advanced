package fiap.com.br.petguardian.veterinaria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.telefone.Telefone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "veterinaria")
public class Veterinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinaria")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "telefone_id_telefone", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Telefone telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id_endereco", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Endereco endereco;
}
