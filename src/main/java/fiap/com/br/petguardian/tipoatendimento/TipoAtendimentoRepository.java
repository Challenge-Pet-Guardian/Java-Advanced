package fiap.com.br.petguardian.tipoatendimento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, Long> {
    Optional<TipoAtendimento> findByTipoAtendimento(EnumTipoAtendimento tipoAtendimento);
}
