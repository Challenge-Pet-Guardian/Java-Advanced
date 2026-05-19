package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;

import jakarta.validation.constraints.NotBlank;

public record FamiliaRequest(
        @NotBlank
        String nome
) {
    public Familia toEntity() {
        return Familia.builder()
                .nome(nome)
                .build();
    }
}
