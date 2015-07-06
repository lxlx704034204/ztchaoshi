package com.iteambuysale.zhongtuan.utilities;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class JsonUtilities {

	/**
	 * 根据类型返回解释对象
	 * 
	 * @param data
	 * @param clazz
	 * @return
	 */
	public static <T> T parseModelByType(String data, Type T) {
		Gson gson = new Gson();
		 T target = gson.fromJson(data, T);
		return target;
	}
	
	public static <T> T parseModelByType(JsonElement elData,Type T){
		Gson gson = new Gson();
		T target = gson.fromJson(elData, T);
		return target;
	}
}