package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer idade;
    private String raca;

    @Enumerated(EnumType.STRING)
    private PetPorte porte;

    private Character sexo;
    private Boolean castrado;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Familia familia;

    @OneToMany(mappedBy = "pet")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tarefa> tarefas;
}
