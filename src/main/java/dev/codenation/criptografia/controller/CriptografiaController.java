package dev.codenation.criptografia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.codenation.criptografia.controller.dto.Mensagem;
import dev.codenation.criptografia.controller.dto.MensagemComResultado;
import dev.codenation.criptografia.service.CriptografiaService;

@RestController
public class CriptografiaController {

	@Autowired
	private CriptografiaService service; 
	
	@PostMapping("/descriptografar")
	public String descriptografarMesagem(@RequestBody Mensagem mensagem) {
		return service.descriptografar(mensagem);
	}
	
	@PostMapping("/enviarMensagemDescriptografadaParaCodenation")
	public MensagemComResultado enviarMensagemDescriptografadaParaCodenation() {
		return service.enviarMensagemDescriptografadaParaCodenation();
	}
}
