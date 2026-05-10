package fiap.com.br.petguardian.endereco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;

    @OneToOne(mappedBy = "endereco")
    @JsonIgnore
    private Usuario usuario;

    @OneToOne(mappedBy = "endereco")
    @JsonIgnore
    private Veterinaria veterinaria;
}
