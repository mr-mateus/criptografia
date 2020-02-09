package dev.codenation.criptografia.controller.dto;

public class MensagemComResultado {
	private MensagemCodenation mensagem;
	private Resultado resultado;
	
	public MensagemComResultado() {
	}
	
	public MensagemComResultado(MensagemCodenation mensagem, Resultado resultado) {
		this.mensagem = mensagem;
		this.resultado = resultado;
	}
	
	public MensagemCodenation getMensagem() {
		return mensagem;
	}
	public void setMensagem(MensagemCodenation mensagem) {
		this.mensagem = mensagem;
	}
	public Resultado getResultado() {
		return resultado;
	}
	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}
	
	
	
}
