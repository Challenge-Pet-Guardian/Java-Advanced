package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.usuario.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String telefone
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }
}
