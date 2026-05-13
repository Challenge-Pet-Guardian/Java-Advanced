package fiap.com.br.petguardian.veterinaria;

import fiap.com.br.petguardian.veterinaria.dto.VeterinariaRequest;
import fiap.com.br.petguardian.veterinaria.dto.VeterinariaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/veterinarias")
@RequiredArgsConstructor
public class VeterinariaController {
    private final VeterinariaService veterinariaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VeterinariaResponse> findAll() {
        return veterinariaService.findAll()
                .stream()
                .map(VeterinariaResponse::fromEntity)
                .toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VeterinariaResponse findById(@PathVariable Long id) {
        return VeterinariaResponse.fromEntity(veterinariaService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinariaResponse create(@Valid @RequestBody VeterinariaRequest veterinariaRequest) {
        return VeterinariaResponse.fromEntity(veterinariaService.create(veterinariaRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public VeterinariaResponse update(@PathVariable Long id, @Valid @RequestBody VeterinariaRequest veterinariaRequest) {
        return VeterinariaResponse.fromEntity(veterinariaService.update(id, veterinariaRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        veterinariaService.delete(id);
    }
}
