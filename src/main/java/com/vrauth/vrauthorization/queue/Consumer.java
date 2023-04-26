package com.vrauth.vrauthorization.queue;

import com.vrauth.vrauthorization.common.logs.LogFilaRepository;
import com.vrauth.vrauthorization.domain.dto.CartaoNovoDto;
import com.vrauth.vrauthorization.domain.entities.LogFila;
import com.vrauth.vrauthorization.domain.exception.CartaoInvalidoException;
import com.vrauth.vrauthorization.domain.exception.CartaoJaExisteException;
import com.vrauth.vrauthorization.domain.services.CartaoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @Autowired
    private LogFilaRepository logFilaRepository;

    @Autowired
    private CartaoService cartaoService;

    @RabbitListener(queues = { "springboot.miniautorizador.queue" })
    public void receiveMessage(@Payload CartaoNovoDto mensagem) {

        System.out.println("Recebido via fila <" + mensagem + ">");

        try {
            cartaoService.criar(mensagem);
            logFilaRepository.save(new LogFila("Cartão criado.", mensagem.toString()));
            System.out.println("Cartão criado <" + mensagem + ">");
        } catch (CartaoJaExisteException e) {
            logFilaRepository.save(new LogFila("Cartão já existe.", mensagem.toString()));
            System.out.println("Cartão já existe <" + mensagem + ">");
        } catch (CartaoInvalidoException e) {
            logFilaRepository.save(new LogFila("Cartão cartão inválido.", mensagem.toString()));
            System.out.println("Cartão inválido <" + mensagem + ">");
        }


    }

}
