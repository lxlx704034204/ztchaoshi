package com.iteambuysale.zhongtuan.background;

import android.os.CountDownTimer;

import com.iteambuysale.zhongtuan.listener.global.YzmListener;

public class CountDownAsync extends CountDownTimer {

	YzmListener listener;

	public CountDownAsync(int seconds, YzmListener _listener) {
		super((long) seconds * 1000, 1000);
		listener = _listener;
	}

	public void startCountDown() {
		listener.onCountDownBegin();
		super.start();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		listener.onFlashSecond((int) (millisUntilFinished / 1000));
	}

	@Override
	public void onFinish() {
		listener.onCountDownEnd();
	}
}
