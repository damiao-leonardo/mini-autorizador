package com.damiao.miniautorizador.core.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CardResponseDto {
    @Schema(
            description = "Número do cartão criado",
            example = "6549873025634501"
    )
    private String senha;
    @Schema(
            description = "Saldo inicial do cartão",
            example = "500.00"
    )
    private String numeroCartao;
}
