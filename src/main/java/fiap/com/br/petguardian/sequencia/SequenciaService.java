package fiap.com.br.petguardian.sequencia;

import fiap.com.br.petguardian.sequencia.dto.SequenciaRequest;
import fiap.com.br.petguardian.tarefa.StatusTarefa;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SequenciaService {
    private final SequenciaRepository sequenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TarefaRepository tarefaRepository;

    @Transactional
    public Sequencia obterSequenciaDoUsuario(Long usuarioId) {
        Sequencia sequencia = buscarOuCriarSequencia(usuarioId);
        verificarQuebraDeSequencia(sequencia);
        return sequencia;
    }

    @Transactional
    public void verificarERegistrarSequencia(Long usuarioId) {
        if (usuarioId == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime inicioProximoDia = hoje.plusDays(1).atStartOfDay();

        long totalTarefasHoje = tarefaRepository.countByUsuarioIdAndPrazoBetween(usuarioId, inicioDia, inicioProximoDia);
        if (totalTarefasHoje == 0) {
            return;
        }

        long tarefasConcluidasHoje = tarefaRepository.countByUsuarioIdAndPrazoBetweenAndStatus(usuarioId, inicioDia, inicioProximoDia, StatusTarefa.CONCLUIDA);

        if (totalTarefasHoje == tarefasConcluidasHoje) {
            registrarAtividade(usuarioId, hoje);
        }
    }

    @Scheduled(cron = "0 5 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void verificarSequenciasQuebradas() {
        LocalDate ontem = LocalDate.now().minusDays(1);
        LocalDateTime inicioOntem = ontem.atStartOfDay();
        LocalDateTime inicioHoje = ontem.plusDays(1).atStartOfDay();

        Set<Long> usuariosComTarefasOntem = tarefaRepository.findDistinctUsuarioIdsComTarefasNoPeriodo(inicioOntem, inicioHoje);

        for (Long usuarioId : usuariosComTarefasOntem) {
            long totalTarefasOntem = tarefaRepository.countByUsuarioIdAndPrazoBetween(usuarioId, inicioOntem, inicioHoje);
            long tarefasConcluidasOntem = tarefaRepository.countByUsuarioIdAndPrazoBetweenAndStatus(usuarioId, inicioOntem, inicioHoje, StatusTarefa.CONCLUIDA);

            if (totalTarefasOntem > tarefasConcluidasOntem) {
                Sequencia sequencia = sequenciaRepository.findByUsuarioId(usuarioId);
                if (sequencia != null && sequencia.getSequenciaAtual() > 0) {
                    sequencia.setSequenciaAtual(0);
                    sequenciaRepository.save(sequencia);
                }
            }
        }
    }

    @Transactional
    public Sequencia updateSequencia(Long usuarioId, SequenciaRequest sequenciaRequest) {
        Sequencia sequencia = findSequenciaByUsuarioId(usuarioId);
        Usuario usuario = findUsuarioById(sequenciaRequest.usuarioId());

        Sequencia sequenciaAtualizada = sequenciaRequest.toEntity(usuario);
        sequenciaAtualizada.setId(sequencia.getId());

        return sequenciaRepository.save(sequenciaAtualizada);
    }

    private void registrarAtividade(Long usuarioId, LocalDate hoje) {
        Sequencia sequencia = buscarOuCriarSequencia(usuarioId);

        if (sequencia.getDataUltimaAtividade() == null) {
            sequencia.setSequenciaAtual(1);
            sequencia.setDataUltimaAtividade(hoje);
            if (sequencia.getSequenciaMaxima() < 1) {
                sequencia.setSequenciaMaxima(1);
            }
            sequenciaRepository.save(sequencia);
            return;
        }

        if (sequencia.getDataUltimaAtividade().isEqual(hoje)) {
            return;
        }

        if (sequencia.getDataUltimaAtividade().isEqual(hoje.minusDays(1))) {
            sequencia.setSequenciaAtual(sequencia.getSequenciaAtual() + 1);
        } else {
            sequencia.setSequenciaAtual(1);
        }

        sequencia.setDataUltimaAtividade(hoje);
        if (sequencia.getSequenciaAtual() > sequencia.getSequenciaMaxima()) {
            sequencia.setSequenciaMaxima(sequencia.getSequenciaAtual());
        }

        sequenciaRepository.save(sequencia);
    }

    private Sequencia buscarOuCriarSequencia(Long usuarioId) {
        Sequencia sequencia = sequenciaRepository.findByUsuarioId(usuarioId);
        if (sequencia == null) {
            Usuario usuario = findUsuarioById(usuarioId);
            SequenciaRequest sequenciaRequest = new SequenciaRequest(0, 0, null, usuarioId);
            sequencia = sequenciaRepository.save(sequenciaRequest.toEntity(usuario));
        }
        return sequencia;
    }

    private void verificarQuebraDeSequencia(Sequencia sequencia) {
        if (sequencia.getDataUltimaAtividade() == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        long diasSemAtividade = ChronoUnit.DAYS.between(sequencia.getDataUltimaAtividade(), hoje);

        if (diasSemAtividade > 1 && sequencia.getSequenciaAtual() > 0) {
            sequencia.setSequenciaAtual(0);
            sequenciaRepository.save(sequencia);
        }
    }

    private Sequencia findSequenciaByUsuarioId(Long usuarioId) {
        Sequencia sequencia = sequenciaRepository.findByUsuarioId(usuarioId);
        if (sequencia == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sequencia não encontrada para o usuário.");
        }
        return sequencia;
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario com id " + id + " nao encontrado."));
    }
}
