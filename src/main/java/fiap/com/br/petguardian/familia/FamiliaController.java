package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.familia.dto.FamiliaRequest;
import fiap.com.br.petguardian.familia.dto.FamiliaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/familias")
@RequiredArgsConstructor
public class FamiliaController {
    private final FamiliaService familiaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FamiliaResponse> findAll() {
        return familiaService.findAll()
                .stream()
                .map(FamiliaResponse::fromEntity)
                .toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse findById(@PathVariable Long id) {
        return FamiliaResponse.fromEntity(familiaService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FamiliaResponse create(@Valid @RequestBody FamiliaRequest familiaRequest) {
        return FamiliaResponse.fromEntity(familiaService.create(familiaRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse update(@PathVariable Long id, @Valid @RequestBody FamiliaRequest familiaRequest) {
        return FamiliaResponse.fromEntity(familiaService.update(id, familiaRequest));
    }

    @PostMapping("{familiaId}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse addUsuario(@PathVariable Long familiaId, @PathVariable Long usuarioId) {
        return FamiliaResponse.fromEntity(familiaService.addUsuario(familiaId, usuarioId));
    }

    @DeleteMapping("{familiaId}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse removeUsuario(@PathVariable Long familiaId, @PathVariable Long usuarioId) {
        return FamiliaResponse.fromEntity(familiaService.removeUsuario(familiaId, usuarioId));
    }

    @PostMapping("{familiaId}/pets/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse addPet(@PathVariable Long familiaId, @PathVariable Long petId) {
        return FamiliaResponse.fromEntity(familiaService.addPet(familiaId, petId));
    }

    @DeleteMapping("{familiaId}/pets/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public FamiliaResponse removePet(@PathVariable Long familiaId, @PathVariable Long petId) {
        return FamiliaResponse.fromEntity(familiaService.removePet(familiaId, petId));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        familiaService.delete(id);
    }
}
