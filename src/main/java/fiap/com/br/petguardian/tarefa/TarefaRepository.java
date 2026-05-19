package fiap.com.br.petguardian.tarefa;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fiap.com.br.petguardian.status.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("select t.id from Tarefa t where t.criador.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select count(t) from Tarefa t where t.criador.familia.id = :familiaId and t.prazo >= :inicio and t.prazo < :fim")
    long countByFamiliaIdAndPrazoBetween(
            @Param("familiaId") Long familiaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("select count(t) from Tarefa t where t.criador.familia.id = :familiaId and t.prazo >= :inicio and t.prazo < :fim and t.status = :status")
    long countByFamiliaIdAndPrazoBetweenAndStatus(
            @Param("familiaId") Long familiaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") Status status
    );

    @Query("select distinct t.criador.familia.id from Tarefa t where t.criador.familia.id is not null and t.prazo >= :inicio and t.prazo < :fim")
    Set<Long> findDistinctFamiliaIdsComTarefasNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("select t from Tarefa t where t.criador.familia.id = :familiaId")
    List<Tarefa> findAllByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select t from Tarefa t where t.id = :id and t.criador.familia.id = :familiaId")
    Optional<Tarefa> findByIdAndFamiliaId(@Param("id") Long id, @Param("familiaId") Long familiaId);

    @Modifying
    @Transactional
    @Query("update Tarefa t set t.status = :expirada where t.status = :pendente and t.prazo < :agora")
    void expirarTarefasPendentesAtrasadas(
            @Param("agora") LocalDateTime agora,
            @Param("pendente") Status pendente,
            @Param("expirada") Status expirada
    );
}
