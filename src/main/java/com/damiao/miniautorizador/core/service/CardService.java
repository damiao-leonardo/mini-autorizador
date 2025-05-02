package com.damiao.miniautorizador.core.service;

import com.damiao.miniautorizador.core.model.dto.CardDto;
import com.damiao.miniautorizador.core.model.dto.CardResponseDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.core.model.mapper.CardMapper;
import com.damiao.miniautorizador.core.repository.CardRepository;
import com.damiao.miniautorizador.exceptions.CardNotFoundException;
import com.damiao.miniautorizador.exceptions.DuplicateCardException;
import com.damiao.miniautorizador.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Value("${CARD_AVAILABLE_BALANCE_INITIAL:500}")
    private BigDecimal availableBalanceInitial;

    @Transactional
    public CardResponseDto createCard(CardDto cardDto) {
        Card card = cardMapper.cardDtoToCard(cardDto);
        verifyCardDoesNotExist(cardDto);
        card.setAvailableBalance(availableBalanceInitial.setScale(2, RoundingMode.DOWN));
        cardRepository.save(card);
        return cardMapper.cardToCardResponseDto(cardDto);
    }

    public BigDecimal getAvailableBalance(String numeroCartao) {
        return cardRepository.findAvailableBalanceByNumber(numeroCartao)
           .orElseThrow(() -> new CardNotFoundException(Constant.CARD_NOT_FOUND));
    }

    private void verifyCardDoesNotExist(CardDto cardDto) {
        Optional.of(cardRepository.existsByCardNumber(cardDto.getNumeroCartao()))
                .filter(exists -> !exists)
                .orElseThrow(() -> new DuplicateCardException(cardDto));
    }
}
