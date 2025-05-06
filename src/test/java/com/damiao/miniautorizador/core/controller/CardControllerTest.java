package com.damiao.miniautorizador.core.controller;

import com.damiao.miniautorizador.core.MiniAutorizadorTestConfig;
import com.damiao.miniautorizador.core.model.dto.CardDto;
import com.damiao.miniautorizador.core.model.entity.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MiniAutorizadorTestConfig
@DisplayName("Testes de Integracao - CardController")
class CardControllerTest {

    private static final String CARD_NUMBER = "6549873025634501";
    private static final String CARD_PASSWORD = "1234";
    private static final String URL = "/cartoes";
    private static final String BASIC_AUTH = "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private CardDto cardDto;
    private Card card;

    @BeforeEach
    void setUp() {
        cardDto = new CardDto();
        cardDto.setNumeroCartao(CARD_NUMBER);
        cardDto.setSenha(CARD_PASSWORD);

        card = new Card();
        card.setCardNumber(CARD_NUMBER);
        card.setCardPassword(CARD_PASSWORD);
        card.setAvailableBalance(BigDecimal.valueOf(500).setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Order(0)
    @DisplayName("Deve criar um cartao com sucesso")
    void shouldCreateCardSuccessfully() throws Exception {
        // Arrange
        String payload = mapper.writeValueAsString(cardDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value(cardDto.getNumeroCartao()))
                .andExpect(jsonPath("$.senha").value(cardDto.getSenha()))
                .andExpect(jsonPath("$.numeroCartao").isNotEmpty())
                .andExpect(jsonPath("$.senha").isNotEmpty());
    }

    @Test
    @Order(1)
    @DisplayName("Deve retornar 422 ao tentar criar cartao duplicado")
    void shouldReturnUnprocessableEntityWhenDuplicateCard() throws Exception {
        // Arrange
        String payload = mapper.writeValueAsString(cardDto);

        // Assert
        mockMvc.perform(createPostRequest(payload)).andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Deve retornar 400 quando senha for nula")
    void shouldReturnBadRequestWhenPasswordIsNull() throws Exception {
        // Arrange
        cardDto.setSenha(null);
        String json = mapper.writeValueAsString(cardDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando numero do cartao for nulo")
    void shouldReturnBadRequestWhenCardNumberIsNull() throws Exception {
        // Arrange
        cardDto.setNumeroCartao(null);
        String json = mapper.writeValueAsString(cardDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(json)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 4xx para numero do cartao com tamanho invalido")
    void shouldReturnClientErrorForInvalidCardNumberSize() throws Exception {
        // Arrange
        cardDto.setNumeroCartao("123456789012345678901234567890");
        String json = mapper.writeValueAsString(cardDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(json)).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Deve retornar erro 4xx para numero do cartao com letras")
    void shouldReturnClientErrorForCardNumberWithLetters() throws Exception {
        // Arrange
        cardDto.setNumeroCartao("ABCDEFGHI");
        String json = mapper.writeValueAsString(cardDto);

        // Act & Assert
        mockMvc.perform(createPostRequest(json)).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Deve retornar saldo do cartao com sucesso")
    void shouldReturnCardBalance() throws Exception {
        // Arrange
        BigDecimal expectedBalance = BigDecimal.valueOf(500).setScale(2, RoundingMode.DOWN);

        // Act
        MvcResult result = mockMvc.perform(createGetRequest(cardDto.getNumeroCartao()))
                .andExpect(status().isOk())
                .andReturn();

        BigDecimal actualBalance = new BigDecimal(result.getResponse().getContentAsString()).setScale(2, RoundingMode.DOWN);

        // Assert
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar saldo de cartao inexistente")
    void shouldReturnNotFoundWhenCardDoesNotExist() throws Exception {
        // Arrange
        String nonexistentCardNumber = "111111111111111";

        // Act & Assert
        mockMvc.perform(createGetRequest(nonexistentCardNumber))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    private MockHttpServletRequestBuilder createPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(URL)
                .header("Authorization", BASIC_AUTH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createGetRequest(String path) {
        return MockMvcRequestBuilders
                .get(URL + "/" + path)
                .header("Authorization", BASIC_AUTH)
                .contentType(MediaType.APPLICATION_JSON);
    }
}
