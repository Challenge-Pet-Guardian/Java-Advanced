package fiap.com.br.petguardian.usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findAllByFamiliaId(Long familiaId);

    @Query("select u.id from Usuario u where u.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);
}
