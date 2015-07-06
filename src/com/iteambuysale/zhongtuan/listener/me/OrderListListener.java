package com.iteambuysale.zhongtuan.listener.me;

import android.widget.AdapterView.OnItemClickListener;

import com.iteambuysale.zhongtuan.listener.global.PullDownListener;

public interface OrderListListener extends OnItemClickListener,PullDownListener{
	void onLoadPicSuccess();
}
