package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.familia.dto.FamiliaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/familias")
@RequiredArgsConstructor
@Tag(name = "Familias", description = "Gerenciamento de famílias")
public class FamiliaController {
    private final FamiliaService familiaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "familias")
    @Operation(summary = "Listar todas as famílias")
    public List<FamiliaResponse> findAll() {
        return familiaService.findAll()
                .stream()
                .map(familiaService::toResponse)
                .toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(value = "familias", key = "#id")
    @Operation(summary = "Buscar família por ID")
    public FamiliaResponse findById(@PathVariable Long id) {
        return familiaService.toResponse(familiaService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "familias", allEntries = true)
    @Operation(summary = "Criar família")
    public FamiliaResponse create(@Valid @RequestBody FamiliaRequest familiaRequest) {
        return familiaService.toResponse(familiaService.create(familiaRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = "familias", allEntries = true)
    @Operation(summary = "Atualizar família")
    public FamiliaResponse update(@PathVariable Long id, @Valid @RequestBody FamiliaRequest familiaRequest) {
        return familiaService.toResponse(familiaService.update(id, familiaRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "familias", allEntries = true)
    @Operation(summary = "Deletar família")
    public void delete(@PathVariable Long id) {
        familiaService.delete(id);
    }
}
