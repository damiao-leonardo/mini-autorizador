package com.damiao.miniautorizador.exceptions;


import com.damiao.miniautorizador.core.model.dto.CardDto;
import lombok.Getter;

@Getter
public class DuplicateCardException extends RuntimeException {

    private final String password;
    private final String cardNumber;

    public DuplicateCardException(CardDto cardDto) {
        super("Cartão já cadastrado !");
        this.password = cardDto.getSenha();
        this.cardNumber = cardDto.getNumeroCartao();
    }

}
