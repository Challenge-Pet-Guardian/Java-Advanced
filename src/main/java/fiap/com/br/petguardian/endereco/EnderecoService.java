package fiap.com.br.petguardian.endereco;

import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Endereco findById(Long id) {
        return findEnderecoById(id);
    }

    public Endereco create(EnderecoRequest enderecoRequest) {
        return enderecoRepository.save(enderecoRequest.toEntity());
    }

    public Endereco update(Long id, EnderecoRequest enderecoRequest) {
        findEnderecoById(id);
        Endereco endereco = enderecoRequest.toEntity();
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }

    public void delete(Long id) {
        findEnderecoById(id);
        enderecoRepository.deleteById(id);
    }

    private Endereco findEnderecoById(Long id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço com id " + id + " não encontrado."));
    }
}
