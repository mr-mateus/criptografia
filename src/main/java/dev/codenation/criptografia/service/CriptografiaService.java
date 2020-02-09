package dev.codenation.criptografia.service;

import dev.codenation.criptografia.controller.dto.Mensagem;
import dev.codenation.criptografia.controller.dto.MensagemCodenation;
import dev.codenation.criptografia.controller.dto.MensagemComResultado;

public interface CriptografiaService {

	MensagemCodenation buscarMensagem();

	String descriptografar(Mensagem mensagem);

	MensagemComResultado enviarMensagemDescriptografadaParaCodenation();

}
