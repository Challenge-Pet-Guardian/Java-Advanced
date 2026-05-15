package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.sequencia.SequenciaService;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final FamiliaRepository familiaRepository;
    private final SequenciaService sequenciaService;

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Tarefa findById(Long id) {
        return findTarefaById(id);
    }

    public Tarefa create(TarefaRequest tarefaRequest) {
        Familia familia = findFamiliaById(tarefaRequest.familiaId());
        Usuario usuario = findUsuarioById(tarefaRequest.usuarioId());
        Pet pet = findPetById(tarefaRequest.petId());

        validarConsistencia(familia, usuario, pet);

        Tarefa tarefa = tarefaRequest.toEntity(familia, usuario, pet);
        tarefa.setStatus(StatusTarefa.PENDENTE);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        sequenciaService.verificarERegistrarSequencia(usuario.getId());
        return tarefaSalva;
    }

    public Tarefa update(Long id, TarefaRequest tarefaRequest) {
        Tarefa tarefaAtual = findTarefaById(id);

        Familia familia = findFamiliaById(tarefaRequest.familiaId());
        Usuario usuario = findUsuarioById(tarefaRequest.usuarioId());
        Pet pet = findPetById(tarefaRequest.petId());

        validarConsistencia(familia, usuario, pet);

        Tarefa tarefa = tarefaRequest.toEntity(familia, usuario, pet);
        tarefa.setId(tarefaAtual.getId());
        tarefa.setCriacao(tarefaAtual.getCriacao());

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        sequenciaService.verificarERegistrarSequencia(usuario.getId());
        return tarefaSalva;
    }

    public void delete(Long id) {
        findTarefaById(id);
        tarefaRepository.deleteById(id);
    }

    private void validarConsistencia(Familia familia, Usuario usuario, Pet pet) {
        if (usuario.getFamilia() == null || !usuario.getFamilia().getId().equals(familia.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário com id " + usuario.getId() + " não pertence à família informada.");
        }
        if (pet.getFamilia() == null || !pet.getFamilia().getId().equals(familia.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet com id " + pet.getId() + " não pertence à família informada.");
        }
    }

    private Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa com id " + id + " não encontrada."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com id " + id + " não encontrado."));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }
}
