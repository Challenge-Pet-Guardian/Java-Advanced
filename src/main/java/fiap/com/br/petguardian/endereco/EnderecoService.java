package fiap.com.br.petguardian.endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private static final String VIACEP_ENDPOINT = "https://viacep.com.br/ws/{cep}/json/";

    private final EnderecoRepository enderecoRepository;
    private final RestClient restClient = RestClient.create();

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Endereco findById(Long id) {
        return findEnderecoById(id);
    }

    public Endereco create(EnderecoRequest enderecoRequest) {
        return enderecoRepository.save(buildEnderecoFromCep(enderecoRequest));
    }

    public Endereco update(Long id, EnderecoRequest enderecoRequest) {
        findEnderecoById(id);
        Endereco endereco = buildEnderecoFromCep(enderecoRequest);
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

    private Endereco buildEnderecoFromCep(EnderecoRequest enderecoRequest) {
        try {
            var response = restClient.get()
                    .uri(VIACEP_ENDPOINT, enderecoRequest.cep())
                    .retrieve()
                    .body(ViaCepResponse.class);

            if (response == null || Boolean.TRUE.equals(response.erro())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP " + enderecoRequest.cep() + " não encontrado.");

            return enderecoRequest.toEntity(response.logradouro(), response.bairro(), response.localidade(), response.estado());

        } catch (RestClientException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao consultar o serviço de CEP.", error);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ViaCepResponse(
            String logradouro,
            String bairro,
            String localidade,
            String estado,
            Boolean erro
    ) {}
}
