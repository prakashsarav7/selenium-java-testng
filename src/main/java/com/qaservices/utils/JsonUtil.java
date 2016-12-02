package com.qaservices.utils;

import io.restassured.path.json.JsonPath;

public class JsonUtil {

	public static String getKey(String key, String jsonText) {
		JsonPath jsonPath = new JsonPath(jsonText);
		return jsonPath.getString(key);
	}

}
