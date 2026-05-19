package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.pet.raca.RacaRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final RacaRepository racaRepository;

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet findById(Long id) {
        return findPetById(id);
    }

    public Pet create(PetRequest petRequest) {
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());
        Pet petSalvo = petRepository.save(petRequest.toEntity(raca));

        usuarioPetRepository.save(
                UsuarioPet.principal(usuario, petSalvo));

        return petSalvo;
    }

    public Pet update(Long id, PetRequest petRequest) {
        Pet petAtual = findPetById(id);
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());

        Pet pet = petRequest.toEntity(raca);
        pet.setId(id);

        Pet petSalvo = petRepository.save(pet);

        usuarioPetRepository.limparResponsavelPrincipalPorPet(petAtual.getId());
        UsuarioPet usuarioPet = usuarioPetRepository.findByUsuarioIdAndPetId(usuario.getId(), petAtual.getId())
                .orElseGet(() -> UsuarioPet.of(usuario, petAtual, false));

        usuarioPet.setResponsavelPrincipal(Boolean.TRUE);
        usuarioPetRepository.save(usuarioPet);

        return petSalvo;
    }

    public void delete(Long id) {
        findPetById(id);
        usuarioPetRepository.deleteByPetId(id);
        petRepository.deleteById(id);
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario com id " + id + " nao encontrado."));
    }

    private Raca findOrCreateRaca(String nomeRaca) {
        return racaRepository.findByNome(nomeRaca).orElseGet(() -> racaRepository.save(Raca.builder().nome(nomeRaca).build()));
    }
}
