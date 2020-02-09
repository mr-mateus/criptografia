package dev.codenation.criptografia.controller.dto;

public class Mensagem {
	private Integer numero_casas;
	private String texto;

	public Mensagem() {
	}
	public Mensagem(String cifrado, Integer numero_casas) {
		this.texto = cifrado; 
		this.numero_casas = numero_casas;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Integer getNumero_casas() {
		return numero_casas;
	}

	public void setNumero_casas(Integer numero_casas) {
		this.numero_casas = numero_casas;
	}
	
	
}
