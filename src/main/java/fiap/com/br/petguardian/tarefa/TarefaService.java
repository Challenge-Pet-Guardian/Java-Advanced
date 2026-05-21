package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.sequencia.SequenciaService;
import fiap.com.br.petguardian.tarefa.dto.TarefaConclusaoRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final FamiliaRepository familiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final SequenciaService sequenciaService;
    private final StatusService statusService;

    public List<Tarefa> findAll(Long familiaId) {
        expirarTarefasPendentesAtrasadas();
        findFamiliaById(familiaId);
        return tarefaRepository.findAllByFamiliaId(familiaId);
    }

    public Tarefa findById(Long id) {
        expirarTarefasPendentesAtrasadas();
        return findTarefaById(id);
    }

    public Tarefa findByFamiliaIdAndTarefaId(Long familiaId, Long tarefaId) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findByIdAndFamiliaId(tarefaId, familiaId).orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + tarefaId + " não encontrada para a familia informada."));
    }

    public Tarefa create(TarefaRequest tarefaRequest) {
        Usuario criador = findUsuarioById(tarefaRequest.criadorId());
        Familia familia = criador.getFamilia();
        Pet pet = findPetById(tarefaRequest.petId());

        validarPetNaFamilia(familia, pet);

        Tarefa tarefa = tarefaRequest.toEntity(criador, pet);
        tarefa.setStatus(statusService.findStatusByNome("PENDENTE"));
        sequenciaService.verificarERegistrarSequencia(familia.getId());
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa update(Long id, TarefaRequest tarefaRequest) {
        Tarefa tarefaAtual = findTarefaById(id);
        Usuario criador = findUsuarioById(tarefaRequest.criadorId());
        Familia familia = criador.getFamilia();

        validarFamiliaImutavel(tarefaAtual, familia.getId());

        Pet pet = findPetById(tarefaRequest.petId());

        validarPetNaFamilia(familia, pet);

        Tarefa tarefa = tarefaRequest.toEntity(criador, pet);
        tarefa.setId(tarefaAtual.getId());
        tarefa.setCriacao(tarefaAtual.getCriacao());

        String statusStr = tarefaRequest.status();

        if ("EXPIRADO".equalsIgnoreCase(statusStr) && tarefaRequest.prazo().isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Não é permitido marcar como EXPIRADO antes do vencimento do prazo.");

        tarefa.setStatus(statusService.findStatusByNome(statusStr));

        if ("CONCLUIDO".equalsIgnoreCase(statusStr)) {
            if (tarefaRequest.concluinteId() == null) throw new IllegalArgumentException("Para marcar a tarefa como CONCLUIDO, é necessário informar o concluinteId.");
            Usuario concluinte = findUsuarioById(tarefaRequest.concluinteId());
            validarUsuarioNaFamilia(concluinte, familia);
            tarefa.setConcluinte(concluinte);
            tarefa.setConclusao(tarefaAtual.getConclusao() == null ? LocalDateTime.now() : tarefaAtual.getConclusao());
        } else {
            tarefa.setConcluinte(null);
            tarefa.setConclusao(null);
        }

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        sequenciaService.verificarERegistrarSequencia(familia.getId());
        return tarefaSalva;
    }

    @Transactional
    public Tarefa concluir(Long id, TarefaConclusaoRequest tarefaConclusaoRequest) {
        expirarTarefasPendentesAtrasadas();
        Tarefa tarefa = findTarefaById(id);
        if (!"PENDENTE".equals(tarefa.getStatus().getNome_status().name()))
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluidas.");

        Usuario concluinte = findUsuarioById(tarefaConclusaoRequest.concluinteId());
        validarUsuarioNaFamilia(concluinte, tarefa.getCriador().getFamilia());

        tarefa.setStatus(statusService.findStatusByNome("CONCLUIDO"));
        tarefa.setConcluinte(concluinte);
        tarefa.setConclusao(LocalDateTime.now());
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        sequenciaService.verificarERegistrarSequencia(tarefa.getCriador().getFamilia().getId());
        return tarefaSalva;
    }

    public void delete(Long id) {
        findTarefaById(id);
        tarefaRepository.deleteById(id);
    }

    private void validarUsuarioNaFamilia(Usuario usuario, Familia familia) {
        if (!familia.getId().equals(usuario.getFamilia().getId())) throw new IllegalArgumentException("Usuário concluinte com id " + usuario.getId() + " não pertence à família informada.");
    }

    private void validarPetNaFamilia(Familia familia, Pet pet) {
        if (!usuarioPetRepository.existsPetNaFamilia(pet.getId(), familia.getId())) throw new IllegalArgumentException("Pet com id " + pet.getId() + " não pertence a família informada.");
    }

    private void validarFamiliaImutavel(Tarefa tarefaAtual, Long familiaIdInformada) {
        if (!tarefaAtual.getCriador().getFamilia().getId().equals(familiaIdInformada)) throw new IllegalArgumentException("Não é permitido trocar a família de uma tarefa existente.");
    }

    private Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + id + " não encontrada."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " não encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário com id " + id + " não encontrado."));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Família com id " + id + " não encontrada."));
    }

    private void expirarTarefasPendentesAtrasadas() {
        Status pendente = statusService.findStatusByNome("PENDENTE");
        Status expirado = statusService.findStatusByNome("EXPIRADO");
        tarefaRepository.expirarTarefasPendentesAtrasadas(LocalDateTime.now(), pendente, expirado);
    }
}
