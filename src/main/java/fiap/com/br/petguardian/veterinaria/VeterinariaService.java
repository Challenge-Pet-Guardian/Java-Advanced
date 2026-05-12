package fiap.com.br.petguardian.veterinaria;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoRepository;
import fiap.com.br.petguardian.veterinaria.dto.VeterinariaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinariaService {
    private final VeterinariaRepository veterinariaRepository;
    private final EnderecoRepository enderecoRepository;

    public List<Veterinaria> findAll() {
        return veterinariaRepository.findAll();
    }

    public Veterinaria findById(Long id) {
        return findVeterinariaById(id);
    }

    public Veterinaria create(VeterinariaRequest veterinariaRequest) {
        Endereco endereco = findEnderecoById(veterinariaRequest.enderecoId());
        Veterinaria veterinaria = veterinariaRequest.toEntity(endereco);

        return veterinariaRepository.save(veterinaria);
    }

    public Veterinaria update(Long id, VeterinariaRequest veterinariaRequest) {
        findVeterinariaById(id);
        Endereco endereco = findEnderecoById(veterinariaRequest.enderecoId());
        Veterinaria veterinaria = veterinariaRequest.toEntity(endereco);
        veterinaria.setId(id);
        return veterinariaRepository.save(veterinaria);
    }

    public void delete(Long id) {
        findVeterinariaById(id);
        veterinariaRepository.deleteById(id);
    }

    private Veterinaria findVeterinariaById(Long id) {
        return veterinariaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinária com id " + id + " não encontrada."));
    }

    private Endereco findEnderecoById(Long id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço com id " + id + " não encontrado."));
    }
}
