package com.damiao.miniautorizador.core.service;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.core.repository.CardRepository;
import com.damiao.miniautorizador.core.service.validator.CardExistValidation;
import com.damiao.miniautorizador.core.service.validator.CompositeTransactionValidator;
import com.damiao.miniautorizador.core.service.validator.PasswordValidation;
import com.damiao.miniautorizador.core.service.validator.SufficientBalanceValidator;
import com.damiao.miniautorizador.core.service.validator.TransactionValidator;
import com.damiao.miniautorizador.exceptions.TransactionException;
import com.damiao.miniautorizador.util.CardMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final String CARD_NUMBER = "6549873025634501";
    private static final String CARD_PASSWORD = "1234";

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CompositeTransactionValidator compositeValidator;

    private TransactionDto transactionDto;
    private Card card;

    /**
     * Inicializa os objetos de teste antes de cada metodo.
     *
     * OBSERVACAO:
     * Caso haja muitos testes utilizando construcoes similares de objetos
     * (ex: TransactionDto, Card), e recomendado extrair essas
     * construcoes para uma classe utilitaria (como ModelUtil ou TestDataBuilder)
     * para promover reuso, clareza e manutencao facilitada.
     */
    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        transactionDto = new TransactionDto();
        transactionDto.setNumeroCartao(CARD_NUMBER);
        transactionDto.setSenhaCartao(CARD_PASSWORD);
        transactionDto.setValor(BigDecimal.valueOf(100.0));

        card = new Card();
        card.setCardNumber(CARD_NUMBER);
        card.setCardPassword(passwordEncoder.encode(CARD_PASSWORD));
        card.setAvailableBalance(BigDecimal.valueOf(500.00));
    }

    @Test
    @DisplayName("Deve processar transação com sucesso")
    void shouldProcessTransactionSuccessfully() {
        // Arrange
        when(cardRepository.findByCardNumber(anyString())).thenReturn(card);

        // Aqui usamos um validador real que lançará exceção
        List<TransactionValidator> validators = List.of(
                new CardExistValidation(),
                new PasswordValidation(),
                new SufficientBalanceValidator());
        CompositeTransactionValidator composite = new CompositeTransactionValidator(validators);
        transactionService = new TransactionService(cardRepository, composite);

        // Act
        String result = transactionService.processTransaction(transactionDto);

        // Assert
        assertEquals(CardMessages.OK, result);
        assertEquals(new BigDecimal("400.0"), card.getAvailableBalance());
        verify(cardRepository).saveAndFlush(card);
    }

    @Test
    @DisplayName("Deve debitar o valor correto do saldo")
    void shouldDebitCorrectAmountFromBalance() {
        // Arrange
        transactionDto.setValor(new BigDecimal("50.00"));
        when(cardRepository.findByCardNumber(anyString())).thenReturn(card);
        doNothing().when(compositeValidator).execute(any(), any());

        // Act
        String result = transactionService.processTransaction(transactionDto);

        // Assert
        assertEquals(CardMessages.OK, result);
        assertEquals(new BigDecimal("450.00"), card.getAvailableBalance());
        verify(cardRepository).saveAndFlush(any(Card.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cartão não existe")
    void shouldThrowExceptionWhenCardDoesNotExist() {
        // Arrange
        when(cardRepository.findByCardNumber(transactionDto.getNumeroCartao())).thenReturn(null);
        // Aqui usamos um validador real que lançará exceção
        List<TransactionValidator> validators = List.of(new CardExistValidation());
        CompositeTransactionValidator composite = new CompositeTransactionValidator(validators);
        transactionService = new TransactionService(cardRepository, composite);

        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.processTransaction(transactionDto);
        });

        assertEquals(CardMessages.CARTAO_INEXISTENTE, exception.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for insuficiente")
    void shouldThrowExceptionWhenInsufficientBalance() {
        // Arrange
        transactionDto.setValor(new BigDecimal("550.00"));
        when(cardRepository.findByCardNumber(transactionDto.getNumeroCartao())).thenReturn(card);

        // Aqui usamos um validador real que lançará exceção
        List<TransactionValidator> validators = List.of(new SufficientBalanceValidator());
        CompositeTransactionValidator composite = new CompositeTransactionValidator(validators);
        transactionService = new TransactionService(cardRepository, composite);

        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.processTransaction(transactionDto);
        });

        assertEquals(CardMessages.INSUFFICIENT_BALANCE, exception.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha for inválida")
    void shouldThrowExceptionWhenInvalidPassword() {
        // Arrange
        transactionDto.setSenhaCartao("senha_incorreta");
        when(cardRepository.findByCardNumber(transactionDto.getNumeroCartao())).thenReturn(card);

        // Aqui usamos um validador real que lançará exceção
        List<TransactionValidator> validators = List.of(new PasswordValidation());
        CompositeTransactionValidator composite = new CompositeTransactionValidator(validators);
        transactionService = new TransactionService(cardRepository, composite);

        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.processTransaction(transactionDto);
        });

        assertEquals(CardMessages.INVALID_PASSWORD, exception.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro inesperado ao processar a transação")
    void shouldThrowTransactionExceptionWhenUnexpectedErrorOccursDuringTransactionProcessing() {
        // Arrange
        when(cardRepository.findByCardNumber(transactionDto.getNumeroCartao())).thenThrow(RuntimeException.class);

        // Act & Assert
        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.processTransaction(transactionDto));

        // Assert
        assertNotNull(exception.getMessage(), "A mensagem de exceção não deve ser nula.");
        assertTrue(exception.getMessage().startsWith("Erro inesperado ao realizar a transacao"), "A mensagem de exceção deve começar com 'Erro inesperado ao realizar a transacao'");
        verify(cardRepository, never()).save(any(Card.class));
    }

}