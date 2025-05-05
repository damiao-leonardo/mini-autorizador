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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
            backoff = @Backoff(delay = 900)
    )
    public String processTransaction(TransactionDto transactionDto) {
        log.info("Iniciando processamento da transação para o cartão: {}", transactionDto.getNumeroCartao());

        try {
            Card card = cardRepository.findByCardNumber(transactionDto.getNumeroCartao());
            compositeValidator.execute(transactionDto, card);
            subtractAvailableBalance(transactionDto, card);
            cardRepository.saveAndFlush(card);
            log.info("Transação finalizada com sucesso para cartão: {}", transactionDto.getNumeroCartao());
            return CardMessages.OK;

        } catch (TransactionException e) {
            log.warn("Erro de validação de transação: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Erro inesperado durante o processamento da transação para cartao {}: {}",
                    transactionDto.getNumeroCartao(), e.getMessage(), e);
            throw new TransactionException("Erro inesperado ao realizar a transacao: " + e.getMessage());
        }
    }


    private void subtractAvailableBalance(TransactionDto transactionDto, Card card) {
        card.setAvailableBalance(card.getAvailableBalance().subtract(transactionDto.getValor()));
    }

}
