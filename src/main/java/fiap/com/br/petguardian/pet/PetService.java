package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.dto.PetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final FamiliaRepository familiaRepository;

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet findById(Long id) {
        return findPetById(id);
    }

    public Pet create(PetRequest petRequest) {
        Familia familia = findFamiliaById(petRequest.familiaId());
        return petRepository.save(petRequest.toEntity(familia));
    }

    public Pet update(Long id, PetRequest petRequest) {
        findPetById(id);
        Familia familia = findFamiliaById(petRequest.familiaId());
        Pet pet = petRequest.toEntity(familia);
        pet.setId(id);

        return petRepository.save(pet);
    }

    public void delete(Long id) {
        findPetById(id);
        petRepository.deleteById(id);
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }
}
