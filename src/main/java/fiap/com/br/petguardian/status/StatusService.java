package fiap.com.br.petguardian.status;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    public Status findById(Long id) {
        return findStatusById(id);
    }

    @Cacheable(value = "status", key = "#nome.toUpperCase()")
    public Status findStatusByNome(String nome) {
        EnumStatus enumStatus = parseEnumStatus(nome);
        return statusRepository.findByNomeStatus(enumStatus).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status '" + nome + "' não encontrado."));
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status com id " + id + " não encontrado."));
    }

    private EnumStatus parseEnumStatus(String nome) {
        try {
            return EnumStatus.valueOf(nome.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + nome);
        }
    }
}
