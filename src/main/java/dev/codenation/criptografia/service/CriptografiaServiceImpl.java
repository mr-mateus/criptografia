package dev.codenation.criptografia.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import dev.codenation.criptografia.controller.dto.Mensagem;
import dev.codenation.criptografia.controller.dto.MensagemCodenation;
import dev.codenation.criptografia.controller.dto.MensagemComResultado;
import dev.codenation.criptografia.controller.dto.Resultado;

@Service
public class CriptografiaServiceImpl implements CriptografiaService {
	private final String CODENATION_URI_GENERATE_DATA = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=81ad1e48dd700f3dabd2efdd57d36eb3a1ef2a13";
	private final String CODENATION_SUBMIT_SOLUTION = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=81ad1e48dd700f3dabd2efdd57d36eb3a1ef2a13";

	@Autowired
	private RestTemplate rest;

	@Autowired
	private MultipartFormDataService multipartFormDataService;

	@Override
	public MensagemCodenation buscarMensagem() {
		ResponseEntity<MensagemCodenation> res = rest.getForEntity(CODENATION_URI_GENERATE_DATA,
				MensagemCodenation.class);
		return res.getBody();
	}


	@Override
	public String descriptografar(Mensagem mensagem) {
		String[] split = mensagem.getTexto().toLowerCase().split("");
		StringBuilder sb = new StringBuilder();
		int numeroCasas = mensagem.getNumero_casas();
		for (String character : split) {
			char c = character.toCharArray()[0];

			if (Character.isLetter(c)) {
				if (c - numeroCasas < 97) {
					int difference = (int) c - 97;
					int positionZero = 123;
					int charPosition = positionZero - (numeroCasas - difference);
					sb.append((char) charPosition);
				} else {
					sb.append(String.valueOf((char) (c - numeroCasas)).toCharArray()[0]);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	@Override
	public MensagemComResultado enviarMensagemDescriptografadaParaCodenation() {
		MensagemCodenation mensagemCodenation = this.buscarMensagem();
		Mensagem mensagem = new Mensagem(mensagemCodenation.getCifrado(), mensagemCodenation.getNumero_casas());
		mensagemCodenation.setDecifrado(this.descriptografar(mensagem));
		mensagemCodenation.setResumo_criptografico(stringToSha1(mensagemCodenation));

		String keyName = "answer";
		String fileName = "answer.json";
		String score =  multipartFormDataService.post(CODENATION_SUBMIT_SOLUTION, keyName, fileName, mensagemCodenation);
		ObjectMapper om = new ObjectMapper();
		try {
			Resultado resultado = om.readValue(score, Resultado.class);
			MensagemComResultado mensagemComResultado = new MensagemComResultado(mensagemCodenation,resultado);
			return mensagemComResultado;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private String stringToSha1(MensagemCodenation mensagemCodenation) {
		return Hashing.sha1().hashString(mensagemCodenation.getDecifrado(), StandardCharsets.UTF_8).toString();
	}
}
