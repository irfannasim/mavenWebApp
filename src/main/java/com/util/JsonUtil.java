package com.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {
	public static String toJson(Object object) {
		String jsonString = "";
		
		try {
			jsonString = GsonSingleton.getInstance().toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}

	public static String toJsonExcludedNull(Object object) {
		String jsonString = "";

		try {
			jsonString = GsonSingleton.getBuilderInstance().excludeFieldsWithoutExposeAnnotation().create().toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	public static <T> Object fromJson(String jsonString, Class<T> clazz) {
		T object = null;
		try {
			object = GsonSingleton.getInstance().fromJson(jsonString, clazz);
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return object;
	}
	
	public static <T> List<T> fromJsonList(String jsonString, Type listType){
		
		List<T> lst = null;
		try {
			lst = GsonSingleton.getInstance().fromJson(jsonString, listType);
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return lst;				 
	}

	public static boolean isValidJSON(String json) {
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonSyntaxException jse) {
			return false;
		}
	}
}
