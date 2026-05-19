package fiap.com.br.petguardian.sequencia;

import fiap.com.br.petguardian.sequencia.dto.SequenciaRequest;
import fiap.com.br.petguardian.sequencia.dto.SequenciaResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sequencias")
@RequiredArgsConstructor
@Tag(name = "Sequencias")
public class SequenciaController {

    private final SequenciaService sequenciaService;

    @GetMapping("/familia/{familiaId}")
    @ResponseStatus(HttpStatus.OK)
    public SequenciaResponse getSequenciaByFamilia(@PathVariable Long familiaId) {
        return SequenciaResponse.fromEntity(sequenciaService.obterSequenciaDaFamilia(familiaId));
    }

    @PutMapping("/familia/{familiaId}")
    @ResponseStatus(HttpStatus.OK)
    public SequenciaResponse updateSequenciaByFamilia(@PathVariable Long familiaId, @Valid @RequestBody SequenciaRequest sequenciaRequest) {
        return SequenciaResponse.fromEntity(sequenciaService.updateSequencia(familiaId, sequenciaRequest));
    }
}
