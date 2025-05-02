package com.damiao.miniautorizador.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CardResponseDto {
    private String senha;
    private String numeroCartao;
}
