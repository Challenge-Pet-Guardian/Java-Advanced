package fiap.com.br.petguardian.sequencia;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.sequencia.dto.SequenciaRequest;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SequenciaService {
    private final SequenciaRepository sequenciaRepository;
    private final FamiliaRepository familiaRepository;
    private final TarefaRepository tarefaRepository;
    private final StatusService statusService;

    @Transactional
    public Sequencia obterSequenciaDaFamilia(Long familiaId) {
        Sequencia sequencia = buscarOuCriarSequencia(familiaId);
        verificarQuebraDeSequencia(sequencia);
        return sequencia;
    }

    @Transactional
    public void verificarERegistrarSequencia(Long familiaId) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime inicioProximoDia = hoje.plusDays(1).atStartOfDay();

        long totalTarefasHoje = tarefaRepository.countByFamiliaIdAndPrazoBetween(familiaId, inicioDia, inicioProximoDia);
        if (totalTarefasHoje == 0) {
            return;
        }

        Status concluida = statusService.findStatusByNome("CONCLUIDO");
        long tarefasConcluidasHoje = tarefaRepository.countByFamiliaIdAndPrazoBetweenAndStatus(familiaId, inicioDia, inicioProximoDia, concluida);

        if (totalTarefasHoje == tarefasConcluidasHoje) {
            registrarAtividade(familiaId, hoje);
        }
    }

    @Transactional
    public Sequencia updateSequencia(Long familiaId, SequenciaRequest sequenciaRequest) {
        if (!familiaId.equals(sequenciaRequest.familiaId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "familiaId do caminho difere do corpo da requisicao.");
        }

        Sequencia sequencia = findSequenciaByFamiliaId(familiaId);
        Familia familia = findFamiliaById(sequenciaRequest.familiaId());

        Sequencia sequenciaAtualizada = sequenciaRequest.toEntity(familia);
        sequenciaAtualizada.setId(sequencia.getId());

        return sequenciaRepository.save(sequenciaAtualizada);
    }

    private void registrarAtividade(Long familiaId, LocalDate hoje) {
        Sequencia sequencia = buscarOuCriarSequencia(familiaId);
        LocalDateTime hojeDateTime = hoje.atStartOfDay();

        if (sequencia.getDataUltimaAtividade() == null) {
            sequencia.setSequenciaAtual(1);
            sequencia.setDataUltimaAtividade(hojeDateTime);
            if (sequencia.getSequenciaMaxima() < 1) {
                sequencia.setSequenciaMaxima(1);
            }
            sequenciaRepository.save(sequencia);
            return;
        }

        LocalDate ultimaAtividade = sequencia.getDataUltimaAtividade().toLocalDate();

        if (ultimaAtividade.isEqual(hoje)) {
            return;
        }

        if (ultimaAtividade.isEqual(hoje.minusDays(1))) {
            sequencia.setSequenciaAtual(sequencia.getSequenciaAtual() + 1);
        } else {
            sequencia.setSequenciaAtual(1);
        }

        sequencia.setDataUltimaAtividade(hojeDateTime);
        if (sequencia.getSequenciaAtual() > sequencia.getSequenciaMaxima()) {
            sequencia.setSequenciaMaxima(sequencia.getSequenciaAtual());
        }

        sequenciaRepository.save(sequencia);
    }

    private Sequencia buscarOuCriarSequencia(Long familiaId) {
        Sequencia sequencia = sequenciaRepository.findByFamiliaId(familiaId);
        if (sequencia == null) {
            Familia familia = findFamiliaById(familiaId);
            SequenciaRequest sequenciaRequest = new SequenciaRequest(0, 0, LocalDateTime.now(), familiaId);
            sequencia = sequenciaRepository.save(sequenciaRequest.toEntity(familia));
        }
        return sequencia;
    }

    private void verificarQuebraDeSequencia(Sequencia sequencia) {
        if (sequencia.getDataUltimaAtividade() == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        LocalDate ultimaAtividade = sequencia.getDataUltimaAtividade().toLocalDate();
        long diasSemAtividade = ChronoUnit.DAYS.between(ultimaAtividade, hoje);

        if (diasSemAtividade > 1 && sequencia.getSequenciaAtual() > 0) {
            LocalDateTime inicioPeriodo = ultimaAtividade.plusDays(1).atStartOfDay();
            LocalDateTime fimPeriodo = hoje.atStartOfDay();

            long totalTarefasNoPeriodo = tarefaRepository.countByFamiliaIdAndPrazoBetween(
                    sequencia.getId(), inicioPeriodo, fimPeriodo);

            if (totalTarefasNoPeriodo > 0) {
                sequencia.setSequenciaAtual(0);
                sequenciaRepository.save(sequencia);
            }
        }
    }

    private Sequencia findSequenciaByFamiliaId(Long familiaId) {
        Sequencia sequencia = sequenciaRepository.findByFamiliaId(familiaId);
        if (sequencia == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sequencia nao encontrada para a familia.");
        }
        return sequencia;
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Familia com id " + id + " nao encontrada."));
    }
}
