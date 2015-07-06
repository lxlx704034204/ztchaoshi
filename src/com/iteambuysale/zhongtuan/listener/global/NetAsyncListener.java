package com.iteambuysale.zhongtuan.listener.global;


public interface NetAsyncListener {
	
	void onResultError(String reqUrl,String errMsg);

	void onResultSuccess(String reqUrl,Object data);

	void onTokenTimeout();
}
