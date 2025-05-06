package com.damiao.miniautorizador.core.service;

import com.damiao.miniautorizador.core.model.dto.CardDto;
import com.damiao.miniautorizador.core.model.dto.CardResponseDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.core.model.mapper.CardMapper;
import com.damiao.miniautorizador.core.repository.CardRepository;
import com.damiao.miniautorizador.exceptions.CardNotFoundException;
import com.damiao.miniautorizador.exceptions.DuplicateCardException;
import com.damiao.miniautorizador.util.enums.ResponseApiEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    private static final String CARD_NUMBER = "6549873025634501";
    private static final String CARD_PASSWORD = "1234";

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Spy
    private CardMapper cardMapper;

    private Card card;

    private CardDto cardDto;

    private CardResponseDto cardResponseDto;

    /**
     * Inicializa os objetos de teste antes de cada metodo.
     *
     * OBSERVACAO:
     * Caso haja muitos testes utilizando construcoes similares de objetos
     * (ex: CardDto, Card, CardResponseDto), e recomendado extrair essas
     * construcoes para uma classe utilitaria (como ModelUtil ou TestDataBuilder)
     * para promover reuso, clareza e manutencao facilitada.
     */
    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(cardService, "availableBalanceInitial", new BigDecimal("500.00"));

        cardDto = new CardDto();
        cardDto.setNumeroCartao(CARD_NUMBER);
        cardDto.setSenha(CARD_PASSWORD);

        card = new Card();
        card.setCardNumber(CARD_NUMBER);
        card.setCardPassword(CARD_PASSWORD);

        cardResponseDto = new CardResponseDto(CARD_PASSWORD,CARD_NUMBER);

    }

    @Test
    @DisplayName("Deve criar o cartao com sucesso")
    void shouldCreateCardSuccessfully() {
        // Arrange
        when(cardMapper.cardDtoToCard(cardDto)).thenReturn(card);
        when(cardRepository.existsByCardNumber(cardDto.getNumeroCartao())).thenReturn(false);
        when(cardMapper.cardToCardResponseDto(cardDto)).thenReturn(cardResponseDto);

        // Act
        CardResponseDto resultCard = cardService.createCard(cardDto);

        // Assert
        assertEquals(cardDto.getNumeroCartao(), resultCard.getNumeroCartao());
        assertEquals(new BigDecimal("500.00"), card.getAvailableBalance());
        verify(cardRepository, times(1)).existsByCardNumber(CARD_NUMBER);
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("Deve lancar excecao quando o cartao ja existir")
    void shouldThrowExceptionWhenCardAlreadyExists() {
        // Arrange
        when(cardMapper.cardDtoToCard(cardDto)).thenReturn(card);
        when(cardRepository.existsByCardNumber(cardDto.getNumeroCartao())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateCardException.class, () -> cardService.createCard(cardDto));
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar o saldo disponivel quando o cartao existir")
    void shouldReturnAvailableBalanceWhenCardExists() {
        // Arrange
        BigDecimal expecteAvailabledBalance = new BigDecimal("100.00");
        when(cardRepository.findAvailableBalanceByNumber(CARD_NUMBER)).thenReturn(Optional.of(expecteAvailabledBalance));

        // Act
        BigDecimal availableBalance = cardService.getAvailableBalance(CARD_NUMBER);

        // Assert
        assertEquals(expecteAvailabledBalance, availableBalance);
    }

    @Test
    @DisplayName("Deve lancar excecao quando o cartao nao for encontrado")
    void shouldThrowExceptionWhenCardNotFound() {
        // Act & Assert
        CardNotFoundException ex = assertThrows(CardNotFoundException.class, () -> cardService.getAvailableBalance(CARD_NUMBER));
        assertEquals(ResponseApiEnum.CARTAO_NAO_ENCONTRADO.getMessage(), ex.getMessage());
    }
}