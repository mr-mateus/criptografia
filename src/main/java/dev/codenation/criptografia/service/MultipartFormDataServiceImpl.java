package dev.codenation.criptografia.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MultipartFormDataServiceImpl implements MultipartFormDataService {
	@Autowired
	private RestTemplate rest;
	
	@Override
	public String post(String url, String keyName, String fileName, Object requestBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		String contentType = "form-data";
		ContentDisposition contentDisposition = ContentDisposition.builder(contentType).name(keyName)
				.filename(fileName).build();
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		try {
			byte[] byteContent = getByteContent(requestBody, fileName);
			HttpEntity<byte[]> fileEntity = new HttpEntity<>(byteContent, fileMap);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileEntity);
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			ResponseEntity<String> response = rest.exchange(
					url,
					HttpMethod.POST,
					requestEntity,
					String.class);
			return response.getBody();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] getByteContent(Object requestBody, String fileName)
			throws IOException, JsonProcessingException, FileNotFoundException {
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		ObjectMapper objectMapper = new ObjectMapper();

		fw.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody));
		fw.close();
		InputStream inputStream = new FileInputStream(file);
		byte[] byteContent = inputStream.readAllBytes();
		inputStream.close();
		return byteContent;
	}
}
