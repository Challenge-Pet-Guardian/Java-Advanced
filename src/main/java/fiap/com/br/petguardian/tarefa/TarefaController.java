package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {
    private final TarefaService tarefaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TarefaResponse> findAll() {
        return tarefaService.findAll()
                .stream()
                .map(TarefaResponse::fromEntity)
                .toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TarefaResponse findById(@PathVariable Long id) {
        return TarefaResponse.fromEntity(tarefaService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse create(@Valid @RequestBody TarefaRequest tarefaRequest) {
        return TarefaResponse.fromEntity(tarefaService.create(tarefaRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TarefaResponse update(@PathVariable Long id, @Valid @RequestBody TarefaRequest tarefaRequest) {
        return TarefaResponse.fromEntity(tarefaService.update(id, tarefaRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tarefaService.delete(id);
    }
}
