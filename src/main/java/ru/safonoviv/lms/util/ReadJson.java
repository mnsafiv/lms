package ru.safonoviv.lms.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ReadJson {
	private String str ="{\n" +
			"  \"successResponse\": {\n" +
			"    \"code\": 200,\n" +
			"    \"status\": \"success\",\n" +
			"    \"message\": \"json array\"\n" +
			"  },\n" +
			"  \"forbiddenResponse\": {\n" +
			"    \"code\": 403,\n" +
			"    \"status\": \"Forbidden\",\n" +
			"    \"message\": \"Forbidden\"\n" +
			"  },\n" +
			"  \"badRequest\": {\n" +
			"    \"code\": 400,\n" +
			"    \"status\": \"Bad request\",\n" +
			"    \"message\": \"Bad request\"\n" +
			"  },\n" +
			"  \"unauthorized\": {\n" +
			"    \"code\": 401,\n" +
			"    \"status\": \"unauthorized\",\n" +
			"    \"message\": \"unauthorized\"\n" +
			"  }\n" +
			"}";
	public JSONObject read() {
//		String file="src/main/resources/openapi/response.json";
//		String content="";
//		try {
//			content= new String(Files.readAllBytes(Paths.get(file)));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return new JSONObject(str);
	}

}
