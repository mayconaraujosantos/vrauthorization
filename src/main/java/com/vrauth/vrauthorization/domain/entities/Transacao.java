package com.vrauth.vrauthorization.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import com.vrauth.vrauthorization.domain.dto.TransacaoNovaDto;
import com.vrauth.vrauthorization.domain.enums.TransacaoEnum;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transacao")
public class Transacao {

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

	@NotNull
	private String numeroCartao;

	@NotNull
	private String senhaCartao;

	@NotNull
	private BigDecimal valor;

	@Enumerated(EnumType.STRING)
	private TransacaoEnum status;

	public Transacao() {
	}

	public Transacao(TransacaoNovaDto transacaoNova) {
		this.numeroCartao = transacaoNova.getNumeroCartao();
		this.senhaCartao = transacaoNova.getSenhaCartao();
		this.valor = transacaoNova.getValor();
	}

	public Transacao(TransacaoNovaDto transacaoNova, TransacaoEnum status) {
		this.numeroCartao = transacaoNova.getNumeroCartao();
		this.senhaCartao = transacaoNova.getSenhaCartao();
		this.valor = transacaoNova.getValor();
		this.status = status;
	}

	public Transacao(String numeroCartao, String senhaCartao, BigDecimal valor, TransacaoEnum status) {
		this.numeroCartao = numeroCartao;
		this.senhaCartao = senhaCartao;
		this.valor = valor;
		this.status = status;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getSenhaCartao() {
		return senhaCartao;
	}

	public void setSenhaCartao(String senhaCartao) {
		this.senhaCartao = senhaCartao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TransacaoEnum getStatus() {
		return status;
	}

	public void setStatus(TransacaoEnum status) {
		this.status = status;
	}

}
