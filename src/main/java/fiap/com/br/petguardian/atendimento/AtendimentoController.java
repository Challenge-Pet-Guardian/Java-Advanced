package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.atendimento.dto.AtendimentoRequest;
import fiap.com.br.petguardian.atendimento.dto.AtendimentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {
    private final AtendimentoService atendimentoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AtendimentoResponse> findAll() {
        return atendimentoService.findAll()
                .stream()
                .map(AtendimentoResponse::fromEntity)
                .toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AtendimentoResponse findById(@PathVariable Long id) {
        return AtendimentoResponse.fromEntity(atendimentoService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtendimentoResponse create(@Valid @RequestBody AtendimentoRequest atendimentoRequest) {
        return AtendimentoResponse.fromEntity(atendimentoService.create(atendimentoRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AtendimentoResponse update(@PathVariable Long id, @Valid @RequestBody AtendimentoRequest atendimentoRequest) {
        return AtendimentoResponse.fromEntity(atendimentoService.update(id, atendimentoRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        atendimentoService.delete(id);
    }
}
