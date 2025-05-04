package com.damiao.miniautorizador.core.service.validator;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.CardMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PasswordValidation implements TransactionValidator {

    @Override
    public void execute(TransactionDto transactionDto, Card card) throws TransactionException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Card validatedCard = Optional.ofNullable(card)
                .filter(cardItem -> passwordEncoder.matches(transactionDto.getSenhaCartao(), cardItem.getCardPassword()))
                .orElseThrow(() -> new TransactionException(CardMessages.INVALID_PASSWORD));

        log.info("Transaction validated successfully: Card with number {} has correct password", validatedCard.getCardNumber());
    }
}
