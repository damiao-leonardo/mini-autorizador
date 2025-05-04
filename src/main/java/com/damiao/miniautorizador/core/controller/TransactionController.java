package com.damiao.miniautorizador.core.controller;

import com.damiao.miniautorizador.core.model.dto.TransactionDto;
import com.damiao.miniautorizador.core.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("transacoes")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Processar uma nova transação",
            description = "Processa uma transação nova com o numero e senha do cartão juntamente com o valor. Caso o cartão não exista, retorna erro."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transação realizada com sucesso",
                    content = @Content(schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Transação não ser processada.",
                    content = @Content(
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(
                                            name = "Saldo insuficiente",
                                            summary = "Exemplo de erro por saldo",
                                            value = "SALDO_INSUFICIENTE"
                                    ),
                                    @ExampleObject(
                                            name = "Senha inválida",
                                            summary = "Exemplo de erro por senha incorreta",
                                            value = "SENHA_INVALIDA"
                                    ),
                                    @ExampleObject(
                                            name = "Cartão inexistente",
                                            summary = "Exemplo de erro por cartão inexistente",
                                            value = "CARTAO_INEXISTENTE"
                                    )
                            }
                    )
            )
    })
    @PostMapping
    public ResponseEntity<String> processTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da transação a ser processada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))
            )
            @Valid @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(transactionService.processTransaction(transactionDto), HttpStatus.CREATED);
    }

}
