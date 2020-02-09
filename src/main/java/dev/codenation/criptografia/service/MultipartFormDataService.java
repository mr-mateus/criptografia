package dev.codenation.criptografia.service;

public interface MultipartFormDataService {

	String post(String url, String keyName, String fileName, Object requestBody);

}
