package com.damiao.miniautorizador.core.service;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.CardMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {


    public String processTransaction(TransactionDto transactionDto) {
        try {
            return CardMessages.OK;
        } catch (TransactionException e) {
            log.warn("Transaction warn: Não foi possível realizar a transação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Transaction error: Erro ao realizar a transação: {}", e.getMessage(), e);
            throw new TransactionException("Erro inesperado ao realizar a transação!" + e.getMessage());
        }
    }


}
