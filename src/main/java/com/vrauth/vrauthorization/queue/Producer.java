package com.vrauth.vrauthorization.queue;

import com.vrauth.vrauthorization.common.config.RabbitmqConfig;
import com.vrauth.vrauthorization.common.logs.LogFilaRepository;
import com.vrauth.vrauthorization.domain.dto.CartaoNovoDto;
import com.vrauth.vrauthorization.domain.entities.LogFila;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private LogFilaRepository logFilaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(CartaoNovoDto mensagem) throws Exception {
        System.out.println("Enviando via fila <" + mensagem + ">");

        logFilaRepository.save(new LogFila("Enviando...", mensagem.toString()));

        rabbitTemplate.convertAndSend(RabbitmqConfig.QUEUE_NAME, mensagem);
    }

}