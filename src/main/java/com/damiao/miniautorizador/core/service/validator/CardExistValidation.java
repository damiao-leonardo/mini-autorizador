package com.damiao.miniautorizador.core.service.validator;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.CardMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CardExistValidation implements TransactionValidator {

    @Override
    public void execute(TransactionDto transactionDto, Card card) throws TransactionException {
        Card validatedCard = Optional.ofNullable(card)
                .orElseThrow(() -> new TransactionException(CardMessages.CARTAO_INEXISTENTE));

        log.info("Card validated successfully: {}", validatedCard);
    }
}
