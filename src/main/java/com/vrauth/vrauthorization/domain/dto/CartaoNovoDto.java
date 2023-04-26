package com.vrauth.vrauthorization.domain.dto;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CartaoNovoDto implements Serializable  {

	private static final long serialVersionUID = -6136282345225004572L;

	@NotNull
	@Size(min = 16, message = "O cartão deve ter 16 números")
	@Size(max = 16, message = "O cartão deve ter 16 números")
	@Schema(description = "Número do cartão", example = "4000000000000001")
	private String numeroCartao;

	@NotNull
	@Size(min = 4, message = "A senha deve ter 4 números")
	@Size(max = 4, message = "A senha deve ter 4 números")
	@Schema(description = "Senha do cartão", example = "1234")
	private String senha;

	public CartaoNovoDto() {
	}

	public CartaoNovoDto(String numeroCartao, String senha) {
		this.numeroCartao = numeroCartao;
		this.senha = senha;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Cartao [numeroCartao=" + numeroCartao + ", senha=" + senha + "]";
	}

}