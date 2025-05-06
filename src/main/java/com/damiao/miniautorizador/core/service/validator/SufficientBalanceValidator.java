package com.damiao.miniautorizador.core.service.validator;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.enums.ResponseApiEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SufficientBalanceValidator implements TransactionValidator {

    @Override
    public void execute(TransactionDto transactionDto, Card card) throws TransactionException {
        BigDecimal availableBalanceCurrent = Optional.ofNullable(card.getAvailableBalance())
                .filter(availableBalance -> availableBalance.compareTo(transactionDto.getValor()) >= 0)
                .orElseThrow(() -> new TransactionException(ResponseApiEnum.INSUFFICIENT_BALANCE.name()));

        log.info("Transaction validated successfully: sufficient balance = {}", availableBalanceCurrent);
    }
}
