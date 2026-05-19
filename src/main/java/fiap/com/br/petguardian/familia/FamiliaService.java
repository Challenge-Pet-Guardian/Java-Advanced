package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.atendimento.AtendimentoRepository;
import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.familia.dto.FamiliaResponse;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FamiliaService {
    private final FamiliaRepository familiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final TarefaRepository tarefaRepository;
    private final AtendimentoRepository atendimentoRepository;

    public List<Familia> findAll() {
        return familiaRepository.findAll();
    }

    public Familia findById(Long id) {
        return findFamiliaById(id);
    }

    public Familia create(FamiliaRequest familiaRequest) {
        return familiaRepository.save(familiaRequest.toEntity());
    }

    public Familia update(Long id, FamiliaRequest familiaRequest) {
        findFamiliaById(id);

        Familia familia = familiaRequest.toEntity();
        familia.setId(id);
        return familiaRepository.save(familia);
    }

    public void delete(Long id) {
        findFamiliaById(id);
        familiaRepository.deleteById(id);
    }

    public FamiliaResponse toResponse(Familia familia) {
        Long familiaId = familia.getId();

        return FamiliaResponse.fromEntity(
                familia,
                safeSet(usuarioRepository.findIdsByFamiliaId(familiaId)),
                safeSet(petRepository.findIdsByFamiliaId(familiaId)),
                safeSet(tarefaRepository.findIdsByFamiliaId(familiaId)),
                safeSet(atendimentoRepository.findIdsByFamiliaId(familiaId)));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Familia com id " + id + " nao encontrada."));
    }
    
    private Set<Long> safeSet(Set<Long> values) {
        return values == null ? new HashSet<>() : values;
    }
}
