package com.damiao.miniautorizador.core.controller;

import com.damiao.miniautorizador.core.MiniAutorizadorTestConfig;
import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.damiao.miniautorizador.core.repository.CardRepository;
import com.damiao.miniautorizador.util.enums.ResponseApiEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MiniAutorizadorTestConfig
@DisplayName("Testes de Integracao - TransactionControlller")
public class TransactionControlllerTest {

    private static final String URL = "/transacoes";
    private static final String CARD_NUMBER = "6549873025634501";
    private static final String CARD_PASSWORD = "1234";
    public static final String BASIC_AUTH = "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CardRepository cardRepository;


    private TransactionDto transactionDto;

    private Card card;

    @BeforeEach
    public void setUp() {
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
    @Order(0)
    @DisplayName("Deve criar uma transacao com sucesso")
    void shouldTransactionSuccessfully() throws Exception {
        // Arrange
        cardRepository.save(card);
        String payload = mapper.writeValueAsString(transactionDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(payload))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("Deve retornar 422 quando o cartao nao existir")
    void shouldReturnUnprocessableEntityWhenCardNotExist() throws Exception {
        // Arrange
        transactionDto.setNumeroCartao("2222222222222222");
        String payload = mapper.writeValueAsString(transactionDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(payload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(ResponseApiEnum.CARTAO_INEXISTENTE.name()));
    }

    @Test
    @DisplayName("Deve retornar 422 quando a senha for invalida")
    void shouldReturnUnprocessableEntityWhenPasswordIsInvalid() throws Exception {
        // Arrange
        transactionDto.setSenhaCartao("senha_invalida");
        String payload = mapper.writeValueAsString(transactionDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(payload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(ResponseApiEnum.SENHA_INVALIDA.name()));
    }

    @Test
    @DisplayName("Deve retornar 422 quando o saldo for insuficiente")
    void shouldReturnUnprocessableEntityWhenNotEnoughBalance() throws Exception {
        // Arrange
        transactionDto.setValor(new BigDecimal("550.00"));
        transactionDto.setNumeroCartao(CARD_NUMBER);
        String payload = mapper.writeValueAsString(transactionDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(payload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(ResponseApiEnum.SALDO_INSUFICIENTE.name()));
    }


    private MockHttpServletRequestBuilder createPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(URL)
                .header("Authorization", BASIC_AUTH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);
    }

}
