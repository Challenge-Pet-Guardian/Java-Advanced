package fiap.com.br.petguardian.endereco;

import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.endereco.dto.EnderecoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {
    private final EnderecoService enderecoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Endereco> findAll() {
        return enderecoService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EnderecoResponse findById(@PathVariable Long id) {
        return EnderecoResponse.fromEntity(enderecoService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoResponse create(@Valid @RequestBody EnderecoRequest enderecoRequest) {
        return EnderecoResponse.fromEntity(enderecoService.create(enderecoRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EnderecoResponse update(@PathVariable Long id, @Valid @RequestBody EnderecoRequest enderecoRequest) {
        return EnderecoResponse.fromEntity(enderecoService.update(id, enderecoRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        enderecoService.delete(id);
    }
}
