package com.damiao.miniautorizador.exceptions;

import lombok.Getter;

@Getter
public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(String message) {
        super(message);
    }

}
