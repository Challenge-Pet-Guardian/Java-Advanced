package fiap.com.br.petguardian.atendimento;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fiap.com.br.petguardian.status.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query("select distinct a from Atendimento a join a.pet p join p.usuarioPets up where up.usuario.familia.id = :familiaId")
    List<Atendimento> findAllByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select distinct a.id from Atendimento a join a.pet p join p.usuarioPets up where up.usuario.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);

    @Modifying
    @Transactional
    @Query("update Atendimento a set a.status = :expirada where a.status = :pendente and a.data < :agora")
    void expirarAtendimentosPendentesAtrasados(
            @Param("agora") LocalDateTime agora,
            @Param("pendente") Status pendente,
            @Param("expirada") Status expirada
    );
}
