package com.damiao.miniautorizador.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseApiEnum {

  CARD_NOT_FOUND("Cartão não encontrado"),
  INSUFFICIENT_BALANCE("Saldo insuficiente"),
  INVALID_PASSWORD("Senha inválida"),
  CARTAO_INEXISTENTE("Cartão inexistente"),
  OK("OK");

  private final String message;

}