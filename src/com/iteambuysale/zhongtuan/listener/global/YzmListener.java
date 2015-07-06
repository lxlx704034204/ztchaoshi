package com.iteambuysale.zhongtuan.listener.global;

public interface YzmListener {
	// 倒数开始
	void onCountDownBegin();

	// 经过一秒，返回当前倒数秒数
	void onFlashSecond(int second);

	// 倒数结束
	void onCountDownEnd();
}
