package com.damiao.miniautorizador.core.service.validator;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.exceptions.TransactionException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CompositeTransactionValidator implements TransactionValidator {

    private final List<TransactionValidator> validators;

    @Override
    public void execute(TransactionDto transactionDto, Card card) throws TransactionException {
        for (TransactionValidator validator : validators) {
            validator.execute(transactionDto, card);
        }
    }
}
