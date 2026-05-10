package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String porte;
    private Character sexo;
    private Boolean castrado;

    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    @OneToMany(mappedBy = "pet")
    private List<Tarefa> tarefas;
}
