package com.damiao.miniautorizador.core.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

    @Schema(
            description = "Número do cartão",
            example = "6549873025634501",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Digits(integer = 16, fraction = 0, message = "Número do cartão deve conter apenas números com até 16 dígitos.")
    @NotBlank(message = "Número do cartão é obrigatório.")
    private String numeroCartao;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(
            description = "Senha do cartão (mínimo 4 caracteres)",
            example = "1234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Size(min = 4, max = 6, message = "A senha deve ter entre 4 e 6 dígitos")
    private String senha;
}
