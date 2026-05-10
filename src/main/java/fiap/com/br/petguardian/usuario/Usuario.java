package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.endereco.Endereco;
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
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private boolean cuidado_pet;

    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    @OneToMany(mappedBy = "usuario")
    private List<Tarefa> tarefas;
}
