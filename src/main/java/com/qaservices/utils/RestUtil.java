package com.qaservices.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestUtil {

	public static Response post(String url) {
		return RestAssured.given().post(url);
	}

}
