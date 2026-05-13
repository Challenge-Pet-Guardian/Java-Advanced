package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;

    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Tarefa findById(Long id) {
        return findTarefaById(id);
    }

    public Tarefa create(TarefaRequest tarefaRequest) {
        Usuario usuario = findUsuarioById(tarefaRequest.usuarioId());
        Pet pet = findPetById(tarefaRequest.petId());
        Tarefa tarefa = tarefaRequest.toEntity(usuario, pet);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa update(Long id, TarefaRequest tarefaRequest) {
        findTarefaById(id);

        Usuario usuario = findUsuarioById(tarefaRequest.usuarioId());
        Pet pet = findPetById(tarefaRequest.petId());

        Tarefa tarefa = tarefaRequest.toEntity(usuario, pet);
        tarefa.setId(id);

        return tarefaRepository.save(tarefa);
    }

    public void delete(Long id) {
        findTarefaById(id);
        tarefaRepository.deleteById(id);
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
}
