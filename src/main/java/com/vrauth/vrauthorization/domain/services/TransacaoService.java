package com.vrauth.vrauthorization.domain.services;

import com.vrauth.vrauthorization.domain.dto.TransacaoNovaDto;
import com.vrauth.vrauthorization.domain.entities.Cartao;
import com.vrauth.vrauthorization.domain.entities.Transacao;
import com.vrauth.vrauthorization.domain.exception.CartaoInvalidoException;
import com.vrauth.vrauthorization.domain.exception.SaldoInsuficienteException;
import com.vrauth.vrauthorization.domain.exception.SenhaInvalidaException;
import com.vrauth.vrauthorization.domain.repositories.CartaoRepository;
import com.vrauth.vrauthorization.domain.repositories.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.vrauth.vrauthorization.domain.enums.TransacaoEnum.*;

@Service
@Transactional(Transactional.TxType.SUPPORTS)
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    public void transacao(TransacaoNovaDto transacaoNova) {
        Cartao cartao = cartaoRepository.findByNumeroCartao(transacaoNova.getNumeroCartao());

        validarCartao(cartao, transacaoNova);
        validarSenhaCartao(cartao.getSenha(), transacaoNova);
        validarSaldoCartao(cartao.getSaldo(), transacaoNova);
        debitarSaldoCartao(cartao, transacaoNova);
    }

    @Transactional(dontRollbackOn = CartaoInvalidoException.class)
    public void validarCartao(Cartao cartao, TransacaoNovaDto transacaoNova) {
        if (cartao == null) {
            transacaoRepository.save(new Transacao(transacaoNova, CARTAO_INEXISTENTE));
            throw new CartaoInvalidoException();
        }
    }

    @Transactional(dontRollbackOn = SenhaInvalidaException.class)
    public void validarSenhaCartao(String senhaCartao, TransacaoNovaDto transacaoNova) {
        if (!senhaCartao.equals(transacaoNova.getSenhaCartao())) {
            transacaoRepository.save(new Transacao(transacaoNova, SENHA_INVALIDA));
            throw new SenhaInvalidaException();
        }
    }

    @Transactional(dontRollbackOn = SaldoInsuficienteException.class)
    public void validarSaldoCartao(BigDecimal saldoCartao, TransacaoNovaDto transacaoNova) {
        if ((saldoCartao.subtract(transacaoNova.getValor())).signum() == -1) {
            transacaoRepository.save(new Transacao(transacaoNova, SALDO_INSUFICIENTE));
            throw new SaldoInsuficienteException();
        }
    }

    @Transactional
    public void debitarSaldoCartao(Cartao cartao, TransacaoNovaDto transacaoNova) {
        synchronized (this) {
            cartao.debitar(transacaoNova.getValor());
            cartaoRepository.save(cartao);
            transacaoRepository.save(new Transacao(transacaoNova, OK));
        }
    }

}