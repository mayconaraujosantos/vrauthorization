package com.vrauth.vrauthorization.domain.repositories;

import com.vrauth.vrauthorization.domain.entities.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao,String>	{

    Cartao findByNumeroCartao(String numeroCartao);

}
