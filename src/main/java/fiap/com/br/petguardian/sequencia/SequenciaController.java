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

    @GetMapping("/usuario/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public SequenciaResponse getSequenciaByUsuario(@PathVariable Long usuarioId) {
        return SequenciaResponse.fromEntity(sequenciaService.obterSequenciaDoUsuario(usuarioId));
    }

    @PutMapping("/usuario/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public SequenciaResponse updateSequenciaByUsuario(@PathVariable Long usuarioId, @Valid @RequestBody SequenciaRequest sequenciaRequest) {
        return SequenciaResponse.fromEntity(sequenciaService.updateSequencia(usuarioId, sequenciaRequest));
    }
}
