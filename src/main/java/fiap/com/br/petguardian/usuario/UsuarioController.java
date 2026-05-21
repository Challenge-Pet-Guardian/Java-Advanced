package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import fiap.com.br.petguardian.usuario.dto.UsuarioResponse;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gerenciamento de usuários")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os usuários com paginação, ordenação e filtro por nome")
    public Page<UsuarioResponse> findAll(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        
        if (nome != null && !nome.isBlank()) {
            return usuarioService.findByNome(nome, pageable)
                    .map(UsuarioResponse::fromEntity);
        }
        
        return usuarioService.findAll(pageable)
                .map(UsuarioResponse::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar usuário por ID")
    public UsuarioResponse findById(@PathVariable Long id) {
        return UsuarioResponse.fromEntity(usuarioService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar usuário")
    public UsuarioResponse create(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        return UsuarioResponse.fromEntity(usuarioService.create(usuarioRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar usuário")
    public UsuarioResponse update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest usuarioRequest) {
        return UsuarioResponse.fromEntity(usuarioService.update(id, usuarioRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar usuário")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}
