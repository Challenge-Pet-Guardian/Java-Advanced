package fiap.com.br.petguardian.endereco.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(
        @NotBlank
        String rua,

        @NotBlank
        String bairro,

        @NotBlank
        String cidade,

        @NotBlank
        String estado
) {
    public Endereco toEntity() {
        return Endereco.builder()
                .rua(rua)
                .bairro(bairro)
                .cidade(cidade)
                .estado(estado)
                .build();
    }
}
