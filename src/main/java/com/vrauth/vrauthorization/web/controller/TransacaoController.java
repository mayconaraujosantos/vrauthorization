package com.vrauth.vrauthorization.web.controller;

import com.vrauth.vrauthorization.domain.dto.TransacaoNovaDto;
import com.vrauth.vrauthorization.domain.enums.TransacaoEnum;
import com.vrauth.vrauthorization.domain.exception.CartaoInvalidoException;
import com.vrauth.vrauthorization.domain.exception.SaldoInsuficienteException;
import com.vrauth.vrauthorization.domain.exception.SenhaInvalidaException;
import com.vrauth.vrauthorization.domain.services.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@Tag(name = "Transação", description = "Transação de cartões")
@RequestMapping(value = "/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Operation(summary = "Cria uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoEnum.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoEnum.class))), })
    @PostMapping
    public ResponseEntity<TransacaoEnum> transacao(@RequestBody @Valid TransacaoNovaDto transacaoNova) {
        try {
            transacaoService.transacao(transacaoNova);
            return new ResponseEntity<>(TransacaoEnum.OK, CREATED);
        } catch (CartaoInvalidoException e) {
            return new ResponseEntity<>(TransacaoEnum.CARTAO_INEXISTENTE, UNPROCESSABLE_ENTITY);
        } catch (SenhaInvalidaException e) {
            return new ResponseEntity<>(TransacaoEnum.SENHA_INVALIDA, UNPROCESSABLE_ENTITY);
        } catch (SaldoInsuficienteException e) {
            return new ResponseEntity<>(TransacaoEnum.SALDO_INSUFICIENTE, UNPROCESSABLE_ENTITY);
        }
    }

}
