package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
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
@Table(name = "familias")
public class Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "familia")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "familia")
    private List<Pet> pets;

    /* ??
    private Usuario usuario_criador;

    private Usuario usuario_membros;
    */
}
