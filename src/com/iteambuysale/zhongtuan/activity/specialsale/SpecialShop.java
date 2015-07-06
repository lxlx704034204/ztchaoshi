package com.iteambuysale.zhongtuan.activity.specialsale;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.views.SlidingMenu;

public class SpecialShop extends FragmentActivity {
	private SlidingMenu mSlidingMenu;
	private SpecialShopCenter centerFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shop_sample);
		init();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		SpecialShopLeft leftFragment = new SpecialShopLeft();
		t.replace(R.id.left_frame, leftFragment);
		centerFragment = new SpecialShopCenter();
		t.replace(R.id.center_frame, centerFragment);
		t.commit();

		// mSlidingMenu.setCanSlidingleft(true);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (getIntent().getBooleanExtra("showleft", false)) {
			showleft();
		}
	}

	public void showleft() {
		mSlidingMenu.showLeftView();
	}

	public boolean isclickfleft() {
		return mSlidingMenu.hasClickLeft;
	}

	public String getcenter() {
		return getIntent().getStringExtra("shopId");
	}

	public SpecialShopCenter getCenterFragment() {
		return centerFragment;
	}
}
