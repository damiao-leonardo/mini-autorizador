package com.damiao.miniautorizador.core.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

    @Digits(integer = 16, fraction = 0, message = "Número do cartão deve conter apenas números com até 16 dígitos.")
    @NotBlank(message = "Número do cartão é obrigatório.")
    private String numeroCartao;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 4, max = 6, message = "A senha deve ter entre 4 e 6 dígitos")
    private String senha;
}
