package com.iteambuysale.zhongtuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.activity.me.setting.SettingActivity;
import com.tencent.stat.StatService;

public abstract class SuperActivity extends FragmentActivity {
	private boolean mIsHome = false;
	private long exitTime = 0;

	protected void onCreate(Bundle savedInstanceState, boolean isHome) {
		super.onCreate(savedInstanceState);
		mIsHome = isHome;
	}
   
	/**
	 * “title Bar” 的设置按钮被点击
	 */
	public void onClickBtnSetting(View view) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	/**
	 * “title Bar” 的返回按钮被点击
	 */
	public void onClickBtnBack(View view) {
		finish();
	}
    
	/**
	 * 重写系统返回键点击事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && this.mIsHome) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(getApplicationContext(),
						"再次点击返回退出", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
				return true;
			} else {
				if(mIsHome)
				System.exit(0);
				//点击后不会退出程序，让程序在后台运行
				 moveTaskToBack(false);  
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

}
