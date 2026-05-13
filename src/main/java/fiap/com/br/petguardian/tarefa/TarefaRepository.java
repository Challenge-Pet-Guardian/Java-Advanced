package fiap.com.br.petguardian.tarefa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("select t.id from Tarefa t where t.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);
}
