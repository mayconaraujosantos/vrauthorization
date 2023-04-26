package com.vrauth.vrauthorization.web.controller;

import com.vrauth.vrauthorization.domain.dto.CartaoNovoDto;
import com.vrauth.vrauthorization.domain.exception.CartaoInvalidoException;
import com.vrauth.vrauthorization.domain.exception.CartaoJaExisteException;
import com.vrauth.vrauthorization.domain.services.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@Validated
@Tag(name = "Cartão", description = "Operações de cartões")
@RequestMapping(value = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @Operation(summary = "Cria um novo cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartaoNovoDto.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartaoNovoDto.class))), })
    @PostMapping
    public ResponseEntity<CartaoNovoDto> criar(@RequestBody @Valid CartaoNovoDto cartaoNovo) {
        try {
            return new ResponseEntity<>(cartaoService.criar(cartaoNovo), CREATED);
        } catch (CartaoJaExisteException e) {
            return new ResponseEntity<>(cartaoNovo, UNPROCESSABLE_ENTITY);
        } catch (CartaoInvalidoException e) {
            return new ResponseEntity<>(null, UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Consulta saldo do cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(hidden = true))) })
    @GetMapping(value = "/{numeroCartao}")
    public ResponseEntity<BigDecimal> saldoCartao(
            @Schema(description = "Número do cartão", example = "4000000000000001") @PathVariable String numeroCartao) {
        try {
            BigDecimal response = cartaoService.saldoCartao(numeroCartao);
            return new ResponseEntity<>(response, OK);
        } catch (CartaoInvalidoException e) {
            return new ResponseEntity<>(null, NOT_FOUND);
        }
    }

    @Operation(summary = "Cria novos cartões")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CartaoNovoDto.class)))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(schema = @Schema(hidden = true))), })
    @PostMapping(value = "/")
    public ResponseEntity<List<CartaoNovoDto>> criarLista(@RequestBody List<@Valid CartaoNovoDto> cartaoNovo) {
        try {
            return new ResponseEntity<>(cartaoService.criar(cartaoNovo), CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, UNPROCESSABLE_ENTITY);
        }
    }

}