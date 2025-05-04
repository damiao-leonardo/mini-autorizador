package com.damiao.miniautorizador.core.service;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.core.repository.CardRepository;
import com.damiao.miniautorizador.core.service.validator.CompositeTransactionValidator;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.CardMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final CardRepository cardRepository;
    private final CompositeTransactionValidator compositeValidator;

    @Transactional
    @Retryable(
            retryFor = StaleObjectStateException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2)
    )
    public String processTransaction(TransactionDto transactionDto) {

        try {
            Card card = findCardByNumber(transactionDto.getNumeroCartao());
            validateTransaction(transactionDto, card);
            applyTransaction(transactionDto, card);
            log.info("Transação concluída com sucesso para o cartão {}", card.getCardNumber());
            return CardMessages.OK;
        } catch (TransactionException e) {
            log.warn("Falha na transação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao processar a transação: {}", e.getMessage(), e);
            throw new TransactionException("Erro inesperado ao realizar a transação: " + e.getMessage());
        }
    }

    private Card findCardByNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    private void validateTransaction(TransactionDto transactionDto, Card card) {
        compositeValidator.execute(transactionDto, card);
    }

    private void applyTransaction(TransactionDto transactionDto, Card card) {
        card.setAvailableBalance(card.getAvailableBalance().subtract(transactionDto.getValor()));
        cardRepository.save(card);
    }

}
