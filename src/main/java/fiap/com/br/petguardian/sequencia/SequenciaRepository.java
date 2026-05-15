package fiap.com.br.petguardian.sequencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SequenciaRepository extends JpaRepository<Sequencia, Long> {
    Sequencia findByUsuarioId(Long usuarioId);
}
