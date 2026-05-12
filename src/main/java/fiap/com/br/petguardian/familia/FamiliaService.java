package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

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

        attachUsuariosToFamilia(familiaRequest.usuarioIds(), saved);
        attachPetsToFamilia(familiaRequest.petIds(), saved);

        return findFamiliaById(saved.getId());
    }

    public Familia update(Long id, FamiliaRequest familiaRequest) {
        findFamiliaById(id);

        Familia familia = familiaRequest.toEntity();
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

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }

    private void attachUsuariosToFamilia(Set<Usuario> usuarios, Familia familia) {
        for (Usuario usuarioRequest : usuarios) {
            Long usuarioId = usuarioRequest.getId();
            if (usuarioId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario sem id informado.");

            Usuario usuario = findUsuarioById(usuarioId);
            usuario.setFamilia(familia);
            usuarioRepository.save(usuario);
        }
    }

    private void attachPetsToFamilia(Set<Pet> pets, Familia familia) {
        for (Pet petRequest : pets) {
            Long petId = petRequest.getId();
            if (petId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet sem id informado.");

            Pet pet = findPetById(petId);
            pet.setFamilia(familia);
            petRepository.save(pet);
        }
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com id " + id + " não encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }
}
