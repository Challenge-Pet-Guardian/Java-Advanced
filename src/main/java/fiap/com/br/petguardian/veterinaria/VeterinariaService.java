package fiap.com.br.petguardian.veterinaria;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
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
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;

    public List<Veterinaria> findAll() {
        return veterinariaRepository.findAll();
    }

    public Veterinaria findById(Long id) {
        return findVeterinariaById(id);
    }

    public Veterinaria create(VeterinariaRequest veterinariaRequest) {
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(veterinariaRequest.endereco());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(veterinariaRequest.ddd()).numero(veterinariaRequest.numeroTelefone()).build());
        Veterinaria veterinaria = veterinariaRequest.toEntity(telefone, endereco);

        return veterinariaRepository.save(veterinaria);
    }

    public Veterinaria update(Long id, VeterinariaRequest veterinariaRequest) {
        findVeterinariaById(id);
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(veterinariaRequest.endereco());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(veterinariaRequest.ddd()).numero(veterinariaRequest.numeroTelefone()).build());
        Veterinaria veterinaria = veterinariaRequest.toEntity(telefone, endereco);
        veterinaria.setId(id);
        return veterinariaRepository.save(veterinaria);
    }

    public void delete(Long id) {
        findVeterinariaById(id);
        veterinariaRepository.deleteById(id);
    }

    private Veterinaria findVeterinariaById(Long id) {
        return veterinariaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Veterinaria com id " + id + " nao encontrada."));
    }
}
