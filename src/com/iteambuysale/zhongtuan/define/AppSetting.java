package com.iteambuysale.zhongtuan.define;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSetting {
	public boolean isFistTime;	// 是否第一次进入app，影响是否显示引导图等判断
	public String uid;			// user id
	public String session;		// 网络访问 session
	public String cityId;		// 城市id
	public String cityCode;		// 城市编号，与city有区别
	public String lat;			// 定位经度
	public String lgn;			// 定位纬度
	public String ackToken;		// 用户token
	public String cityName;		// 定位城市名称
	public String phoneNumber;	// 用户手机号码，影响登陆账号

	public void persistGet(SharedPreferences prefers) {
		isFistTime = prefers.getBoolean("isFistTime", true);
		uid = prefers.getString("uid", "");
		session = prefers.getString("session", "");
		cityId = prefers.getString("cityId", "");
		cityCode = prefers.getString("cityCode", "");
		lat = prefers.getString("lat", "");
		lgn = prefers.getString("lgn", "");
		ackToken = prefers.getString("ackToken", "");
		cityName = prefers.getString("cityName", "");
		phoneNumber=prefers.getString("phoneNumber", "");
	}

	public void persistSave(SharedPreferences prefers) {
		SharedPreferences.Editor editor = prefers.edit();
		editor.putBoolean("isFistTime", isFistTime);
		editor.putString("uid", uid);
		editor.putString("session", session);
		editor.putString("cityId", cityId);
		editor.putString("cityCode", cityCode);
		editor.putString("lat", lat);
		editor.putString("lgn", lgn);
		editor.putString("ackToken", ackToken);
		editor.putString("cityName", cityName);
		editor.putString("phoneNumber", phoneNumber);
		editor.commit();
	}

	public void persistClear(SharedPreferences prefers) {
		SharedPreferences.Editor editor = prefers.edit();
		editor.clear();
		editor.commit();
	}
	public String getCityName(Context context){
		SharedPreferences prefers = context.getSharedPreferences(D.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return prefers.getString(cityName, "广州");
		
	}
}
