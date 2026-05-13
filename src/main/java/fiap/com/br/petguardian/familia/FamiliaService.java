package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.atendimento.AtendimentoRepository;
import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.familia.dto.FamiliaResponse;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.usuario.Usuario;
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
        Familia saved = familiaRepository.save(familiaRequest.toEntity(new HashSet<>(), new HashSet<>()));

        attachUsuariosToFamilia(familiaRequest.usuarioIds(), saved);
        attachPetsToFamilia(familiaRequest.petIds(), saved);

        return findFamiliaById(saved.getId());
    }

    public Familia update(Long id, FamiliaRequest familiaRequest) {
        findFamiliaById(id);
        clearUsuariosFromFamilia(id);
        clearPetsFromFamilia(id);

        Familia familia = familiaRequest.toEntity(new HashSet<>(), new HashSet<>());
        familia.setId(id);
        Familia saved = familiaRepository.save(familia);

        attachUsuariosToFamilia(familiaRequest.usuarioIds(), saved);
        attachPetsToFamilia(familiaRequest.petIds(), saved);

        return findFamiliaById(saved.getId());
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
                safeSet(atendimentoRepository.findIdsByFamiliaId(familiaId))
        );
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Familia com id " + id + " nao encontrada."));
    }

    private void attachUsuariosToFamilia(Set<Long> usuarioIds, Familia familia) {
        if (usuarioIds == null || usuarioIds.isEmpty()) return;

        for (Long usuarioId : usuarioIds) {
            if (usuarioId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario sem id informado.");

            Usuario usuario = findUsuarioById(usuarioId);
            usuario.setFamilia(familia);
            usuarioRepository.save(usuario);
        }
    }

    private void attachPetsToFamilia(Set<Long> petIds, Familia familia) {
        if (petIds == null || petIds.isEmpty()) return;

        for (Long petId : petIds) {
            if (petId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet sem id informado.");

            Pet pet = findPetById(petId);
            pet.setFamilia(familia);
            petRepository.save(pet);
        }
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario com id " + id + " nao encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " nao encontrado."));
    }

    private void clearUsuariosFromFamilia(Long familiaId) {
        List<Usuario> usuarios = usuarioRepository.findAllByFamiliaId(familiaId);
        if (usuarios == null || usuarios.isEmpty()) return;

        for (Usuario usuario : usuarios) {
            usuario.setFamilia(null);
        }
        usuarioRepository.saveAll(usuarios);
    }

    private void clearPetsFromFamilia(Long familiaId) {
        List<Pet> pets = petRepository.findAllByFamiliaId(familiaId);
        if (pets == null || pets.isEmpty()) return;

        for (Pet pet : pets) {
            pet.setFamilia(null);
        }
        petRepository.saveAll(pets);
    }

    private Set<Long> safeSet(Set<Long> values) {
        return values == null ? new HashSet<>() : values;
    }
}
