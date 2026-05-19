package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.dto.PetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Gerenciamento de pets da família")
public class PetController {
    private final PetService petService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PetResponse> findAll() {
        return petService.findAll()
                .stream()
                .map(PetResponse::fromEntity)
                .toList();
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
}
