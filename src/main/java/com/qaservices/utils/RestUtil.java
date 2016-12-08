package com.qaservices.utils;

import io.restassured.RestAssured;

import java.io.File;
import java.util.Map;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtil {

	private static void setBaseURI(String baseUri) {
		if (baseUri != null && !baseUri.isEmpty()) {
			RestAssured.baseURI = baseUri;
		} else {
			throw new RuntimeException("Base URI is not set");
		}
	}

	private static RequestSpecification getRequestSpecification(String baseUrl, String username, String password, Map<String, String> headers, String contentType, String body) {
		setBaseURI(baseUrl);
		return RestAssured.given().auth().basic(username, password).headers(headers).contentType(contentType).body(body);
	}

	private static RequestSpecification getRequestSpecification(String baseUrl, Map<String, String> headers, Map<String, String> inputData) {
		setBaseURI(baseUrl);
		return RestAssured.given().headers(headers).formParams(inputData);
	}

	private static RequestSpecification getRequestSpecification(String baseUrl, String username, String password, Map<String, String> headers, String contentType, String filePath, String body) {
		setBaseURI(baseUrl);
		return RestAssured.given().auth().basic(username, password).headers(headers).contentType(contentType).multiPart("files", new File(filePath)).body(body);
	}

	private static Response request(Method method, RequestSpecification requestSpecification, String pathUrl) {
		Response response = null;

		switch (method) {
		case GET:
			response = requestSpecification.get(pathUrl);
			break;

		case POST:
			response = requestSpecification.post(pathUrl);
			break;

		case PUT:
			response = requestSpecification.put(pathUrl);
			break;

		case DELETE:
			response = requestSpecification.delete(pathUrl);
			break;

		default:
			throw new RuntimeException("Not yet implemented for " + method);
		}

		return response;
	}

	public static Response sendRequest(Method method, String baseUrl) {
		RequestSpecification requestSpecification = RestAssured.given();
		return request(method, requestSpecification, baseUrl);
	}

	public static Response sendRequest(Method method, String baseUrl, String username, String password, Map<String, String> headers, String contentType, String body, String pathUrl) {
		RequestSpecification requestSpecification = getRequestSpecification(baseUrl, username, password, headers, contentType, body);
		return request(method, requestSpecification, pathUrl);
	}

	public static Response sendRequest(Method method, String baseUrl, String username, String password, Map<String, String> headers, String contentType, String filePath, String body, String pathUrl) {
		RequestSpecification requestSpecification = getRequestSpecification(baseUrl, username, password, headers, contentType, filePath, body);
		return request(method, requestSpecification, pathUrl);
	}

	public static Response sendRequest(Method method, String baseUrl, Map<String, String> headers, Map<String, String> inputData, String pathUrl) {
		RequestSpecification requestSpecification = getRequestSpecification(baseUrl, headers, inputData);
		return request(method, requestSpecification, pathUrl);
	}
}
