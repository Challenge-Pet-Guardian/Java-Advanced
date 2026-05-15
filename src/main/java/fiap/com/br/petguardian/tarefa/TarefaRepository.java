package fiap.com.br.petguardian.tarefa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("select t.id from Tarefa t where t.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select count(t) from Tarefa t where t.usuario.id = :usuarioId and t.status not in ('CONCLUIDA', 'CANCELADA')")
    long countPendentesByUsuarioId(@Param("usuarioId") Long usuarioId);

    long countByUsuarioIdAndPrazoBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);

    long countByUsuarioIdAndPrazoBetweenAndStatus(Long usuarioId, LocalDateTime inicio, LocalDateTime fim, StatusTarefa status);

    @Query("select distinct t.usuario.id from Tarefa t where t.prazo >= :inicio and t.prazo < :fim")
    Set<Long> findDistinctUsuarioIdsComTarefasNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
