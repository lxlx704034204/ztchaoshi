package com.iteambuysale.zhongtuan.listener;

import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;

public interface SaleListener extends NetAsyncListener{

	// 刷新图片
	void onLoadPicSuccess();

	// 当id为itemId的产品项被点击
	void onSelectProductItem(String pid);
	
	void onSelectWebviewItem(String url);

}
