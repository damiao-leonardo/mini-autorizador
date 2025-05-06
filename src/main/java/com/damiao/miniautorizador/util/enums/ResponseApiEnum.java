package com.damiao.miniautorizador.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseApiEnum {

  CARTAO_NAO_ENCONTRADO("Cartão não encontrado"),
  SALDO_INSUFICIENTE("Saldo insuficiente"),
  SENHA_INVALIDA("Senha inválida"),
  CARTAO_INEXISTENTE("Cartão inexistente"),
  OK("OK");

  private final String message;

}