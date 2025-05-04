package com.damiao.miniautorizador.core.service.validator;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.exceptions.TransactionException;

public interface TransactionValidator {
    void execute(TransactionDto transactionDto, Card card) throws TransactionException;
}
