package fiap.com.br.petguardian.atendimento;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query("select a.id from Atendimento a where a.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select count(a) from Atendimento a where a.familia.id = :familiaId and a.status not in ('CONCLUIDA', 'CANCELADA')")
    long countPendentesByFamiliaId(@Param("familiaId") Long familiaId);
}
