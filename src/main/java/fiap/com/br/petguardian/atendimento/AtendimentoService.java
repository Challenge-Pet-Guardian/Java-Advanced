package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.atendimento.dto.AtendimentoRequest;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import fiap.com.br.petguardian.veterinaria.VeterinariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtendimentoService {
    private final AtendimentoRepository atendimentoRepository;
    private final PetRepository petRepository;
    private final VeterinariaRepository veterinariaRepository;
    private final FamiliaRepository familiaRepository;

    public List<Atendimento> findAll() {
        return atendimentoRepository.findAll();
    }

    public Atendimento findById(Long id) {
        return findAtendimentoById(id);
    }

    public Atendimento create(AtendimentoRequest atendimentoRequest) {
        Familia familia = findFamiliaById(atendimentoRequest.familiaId());
        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinaria veterinaria = findVeterinariaById(atendimentoRequest.veterinariaId());

        validarPetNaFamilia(familia, pet);

        Atendimento atendimento = atendimentoRequest.toEntity(familia, pet, veterinaria);
        return atendimentoRepository.save(atendimento);
    }

    public Atendimento update(Long id, AtendimentoRequest atendimentoRequest) {
        findAtendimentoById(id);

        Familia familia = findFamiliaById(atendimentoRequest.familiaId());
        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinaria veterinaria = findVeterinariaById(atendimentoRequest.veterinariaId());

        validarPetNaFamilia(familia, pet);

        Atendimento atendimento = atendimentoRequest.toEntity(familia, pet, veterinaria);
        atendimento.setId(id);
        return atendimentoRepository.save(atendimento);
    }

    public void delete(Long id) {
        findAtendimentoById(id);
        atendimentoRepository.deleteById(id);
    }

    private void validarPetNaFamilia(Familia familia, Pet pet) {
        if (pet.getFamilia() == null || !pet.getFamilia().getId().equals(familia.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pet com id " + pet.getId() + " não pertence à família informada.");
        }
    }

    private Atendimento findAtendimentoById(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento com id " + id + " não encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private Veterinaria findVeterinariaById(Long id) {
        return veterinariaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinária com id " + id + " não encontrada."));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }
}
