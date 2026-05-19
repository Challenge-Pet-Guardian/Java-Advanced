package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final FamiliaRepository familiaRepository;
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return findUsuarioById(id);
    }

    public Usuario create(UsuarioRequest usuarioRequest) {
        Familia familia = findFamiliaById(usuarioRequest.familiaId());
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(usuarioRequest.endereco());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(usuarioRequest.ddd()).numero(usuarioRequest.numeroTelefone()).build());
        return usuarioRepository.save(usuarioRequest.toEntity(telefone, endereco, familia));
    }

    public Usuario update(Long id, UsuarioRequest usuarioRequest) {
        findUsuarioById(id);
        Familia familia = findFamiliaById(usuarioRequest.familiaId());
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(usuarioRequest.endereco());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(usuarioRequest.ddd()).numero(usuarioRequest.numeroTelefone()).build());
        Usuario usuario = usuarioRequest.toEntity(telefone, endereco, familia);
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        findUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com id " + id + " não encontrado." ));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }
}
