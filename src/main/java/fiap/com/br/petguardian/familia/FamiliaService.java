package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamiliaService {
    private final FamiliaRepository familiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;

    public List<Familia> findAll() {
        return familiaRepository.findAll();
    }

    public Familia findById(Long id) {
        return findFamiliaById(id);
    }

    public Familia create(FamiliaRequest familiaRequest) {
        Familia saved = familiaRepository.save(familiaRequest.toEntity());
        return findFamiliaById(saved.getId());
    }

    public Familia update(Long id, FamiliaRequest familiaRequest) {
        Familia familia = findFamiliaById(id);
        familia.setNome(familiaRequest.nome());
        Familia saved = familiaRepository.save(familia);
        return findFamiliaById(saved.getId());
    }

    public void delete(Long id) {
        findFamiliaById(id);
        familiaRepository.deleteById(id);
    }

    public Familia addUsuario(Long familiaId, Long usuarioId) {
        Familia familia = findFamiliaById(familiaId);
        Usuario usuario = findUsuarioById(usuarioId);

        if (usuario.getFamilia() != null && !usuario.getFamilia().getId().equals(familiaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario ja pertence a outra familia.");
        }

        usuario.setFamilia(familia);
        usuarioRepository.save(usuario);

        return findFamiliaById(familiaId);
    }

    public Familia removeUsuario(Long familiaId, Long usuarioId) {
        findFamiliaById(familiaId);
        Usuario usuario = findUsuarioById(usuarioId);

        if (usuario.getFamilia() == null || !usuario.getFamilia().getId().equals(familiaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario nao pertence a esta familia.");
        }

        usuario.setFamilia(null);
        usuarioRepository.save(usuario);

        return findFamiliaById(familiaId);
    }

    public Familia addPet(Long familiaId, Long petId) {
        Familia familia = findFamiliaById(familiaId);
        Pet pet = findPetById(petId);

        if (pet.getFamilia() != null && !pet.getFamilia().getId().equals(familiaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet ja pertence a outra familia.");
        }

        pet.setFamilia(familia);
        petRepository.save(pet);

        return findFamiliaById(familiaId);
    }

    public Familia removePet(Long familiaId, Long petId) {
        findFamiliaById(familiaId);
        Pet pet = findPetById(petId);

        if (pet.getFamilia() == null || !pet.getFamilia().getId().equals(familiaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet nao pertence a esta familia.");
        }

        pet.setFamilia(null);
        petRepository.save(pet);

        return findFamiliaById(familiaId);
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Familia com id " + id + " nao encontrada."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario com id " + id + " nao encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " nao encontrado."));
    }
}
