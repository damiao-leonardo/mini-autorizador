package com.damiao.miniautorizador.util.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
  ADMINISTRATOR(1L,"Administrador","ROLE_ADMINISTRATOR"),

  SUPPLIER(2L, "Fornecedor","ROLE_SUPPLIER"),

  BUYER(3L, "Comprador","ROLE_BUYER"),

  SUPERVISOR(4L, "Fiscal","ROLE_SUPERVISOR"),

  SECRETARY(5L, "Secretário","ROLE_SECRETARY"),

  CONSULTANT(6L, "Consultor","ROLE_CONSULTANT");

  private final Long id;

  private final String name;

  private final String description;

  RoleEnum(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
