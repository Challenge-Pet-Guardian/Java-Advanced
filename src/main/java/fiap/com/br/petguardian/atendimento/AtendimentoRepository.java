package fiap.com.br.petguardian.atendimento;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query("""
            select distinct a.id
            from Atendimento a
            join a.pet p
            where p.familia.id = :familiaId
            """)
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);
}
