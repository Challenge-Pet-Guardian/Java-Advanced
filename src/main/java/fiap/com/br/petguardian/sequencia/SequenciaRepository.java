package fiap.com.br.petguardian.sequencia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenciaRepository extends JpaRepository<Sequencia, Long> {
    Sequencia findByFamiliaId(Long familiaId);
}
