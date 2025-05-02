package com.damiao.miniautorizador.core.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    @Schema(
            description = "Número do cartão",
            example = "6549873025634501",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Número do cartão é obrigatório.")
    private String numeroCartao;
    @Schema(
            description = "Senha do cartão (mínimo 4 caracteres)",
            example = "1234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Senha do cartão é obrigatória.")
    private String senhaCartao;
    @Schema(
            description = "Valor da transação",
            example = "10.0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor mínimo da transação deve ser 0.01.")
    private BigDecimal valor;
}
