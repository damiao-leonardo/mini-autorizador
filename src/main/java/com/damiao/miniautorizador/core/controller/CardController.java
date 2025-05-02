package com.damiao.miniautorizador.core.controller;

import com.damiao.miniautorizador.core.model.dto.CardDto;
import com.damiao.miniautorizador.core.model.dto.CardResponseDto;
import com.damiao.miniautorizador.core.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("cartoes")
@Tag(name = "Cartões", description = "Operações relacionadas aos cartões")
public class CardController {

    private final CardService cardService;

    @Operation(
            summary = "Cria um novo cartão",
            description = "Cria um novo cartão com número e senha. Caso o cartão já exista, retorna erro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cartão criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Requisição não autorizada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Cartão já existe na base de dados",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do cartão a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CardDto.class))
            )
            @Valid @RequestBody CardDto cardDto) {
        return new ResponseEntity<>(cardService.createCard(cardDto), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Consulta o saldo de um cartão",
            description = "Retorna o saldo disponível de um cartão a partir do número informado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Consulta realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = CardResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cartão não encontrado na base de dados",
                    content = @Content
            )
    })
    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> getAvailableBalance(
            @Parameter(description = "Número do cartão", required = true)
            @PathVariable("numeroCartao") String numeroCartao) {
        return new ResponseEntity<>(cardService.getAvailableBalance(numeroCartao), HttpStatus.OK);
    }
}
