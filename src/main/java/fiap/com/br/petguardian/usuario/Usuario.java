package fiap.com.br.petguardian.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "telefone_id_telefone", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Telefone telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id_endereco", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "familia_id_familia", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Familia familia;

    @OneToMany(mappedBy = "criador")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Tarefa> tarefasCriadas = new HashSet<>();

    @OneToMany(mappedBy = "concluinte")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Tarefa> tarefasExecutadas = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UsuarioPet> usuarioPets = new HashSet<>();
}
