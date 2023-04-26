package com.vrauth.vrauthorization.domain.repositories;

import com.vrauth.vrauthorization.domain.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransacaoRepository  extends JpaRepository<Transacao, UUID> {

}
