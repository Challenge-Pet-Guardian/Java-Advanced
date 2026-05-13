package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.endereco.Endereco;
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
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Endereco endereco;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Familia familia;

    @OneToMany(mappedBy = "usuario")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tarefa> tarefas;
}
