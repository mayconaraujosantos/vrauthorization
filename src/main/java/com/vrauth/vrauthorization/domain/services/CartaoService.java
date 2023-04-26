package com.vrauth.vrauthorization.domain.services;

import com.vrauth.vrauthorization.common.logs.LogFilaRepository;
import com.vrauth.vrauthorization.domain.dto.CartaoNovoDto;
import com.vrauth.vrauthorization.domain.entities.Cartao;
import com.vrauth.vrauthorization.domain.entities.LogFila;
import com.vrauth.vrauthorization.domain.exception.CartaoInvalidoException;
import com.vrauth.vrauthorization.domain.exception.CartaoJaExisteException;
import com.vrauth.vrauthorization.domain.repositories.CartaoRepository;
import com.vrauth.vrauthorization.queue.Producer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CartaoService {

    @Autowired
    CartaoRepository cartaoRepository;

    @Autowired
    private LogFilaRepository logFilaRepository;

    @Autowired
    private Producer producer;

    public CartaoNovoDto criar(CartaoNovoDto cartaoNovo) {

        validarNumeroCartao(cartaoNovo.getNumeroCartao());
        validarCartaoJaExiste(cartaoNovo.getNumeroCartao());

        Cartao cartao = new Cartao(cartaoNovo);
        cartaoRepository.saveAndFlush(cartao);

        return cartaoNovo;
    }

    public BigDecimal saldoCartao(String numeroCartao) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartao == null) {
            throw new CartaoInvalidoException();
        }
        return cartao.getSaldo();
    }

    private void validarNumeroCartao(String numeroCartao) {
        if (!numeroCartao.matches("^\\d+$"))
            throw new CartaoInvalidoException();
    }

    private void validarCartaoJaExiste(String numeroCartao) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao);
        if (cartao != null) {
            throw new CartaoJaExisteException(cartao.getNumeroCartao(), cartao.getSenha());
        }
    }

    public List<CartaoNovoDto> criar(List<CartaoNovoDto> cartaoNovo) {
        cartaoNovo.stream()
                .forEach(cartao -> {
                    try {
                        producer.send(cartao);
                    } catch (Exception e) {
                        logFilaRepository.save(new LogFila("Erro ao enviar cartão <" + cartao + ">", e.getLocalizedMessage()));
                        e.printStackTrace();
                    }
                });
        return cartaoNovo;
    }

}