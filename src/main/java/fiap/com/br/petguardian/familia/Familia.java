package fiap.com.br.petguardian.familia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
@Table(name = "familias")
public class Familia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Default
    @OneToMany(mappedBy = "familia")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Usuario> usuarios = new HashSet<>();

    @Default
    @OneToMany(mappedBy = "familia")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Pet> pets = new HashSet<>();
}
