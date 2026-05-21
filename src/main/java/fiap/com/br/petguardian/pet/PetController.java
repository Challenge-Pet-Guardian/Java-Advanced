package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.dto.PetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Gerenciamento de pets da família")
public class PetController {
    private final PetService petService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os pets com paginação, ordenação e filtro por nome")
    public Page<PetResponse> findAll(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        
        if (nome != null && !nome.isBlank()) {
            return petService.findByNome(nome, pageable)
                .map(PetResponse::fromEntity);
        }
        
        return petService.findAll(pageable)
            .map(PetResponse::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar pet por ID")
    public PetResponse findById(@PathVariable Long id) {
        return PetResponse.fromEntity(petService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pet")
    public PetResponse create(@Valid @RequestBody PetRequest petRequest) {
        return PetResponse.fromEntity(petService.create(petRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar pet")
    public PetResponse update(@PathVariable Long id, @Valid @RequestBody PetRequest petRequest) {
        return PetResponse.fromEntity(petService.update(id, petRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar pet")
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }

    @PostMapping("{id}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Vincular um usuário a um pet")
    public void vincularUsuario(@PathVariable Long id, @PathVariable Long usuarioId, @RequestParam(defaultValue = "false") boolean principal) {
        petService.vincularUsuario(id, usuarioId, principal);
    }

    @DeleteMapping("{id}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvincular um usuário de um pet")
    public void desvincularUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        petService.desvincularUsuario(id, usuarioId);
    }
}
