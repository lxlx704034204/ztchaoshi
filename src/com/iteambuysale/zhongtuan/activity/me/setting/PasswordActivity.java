package com.iteambuysale.zhongtuan.activity.me.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.actor.EditPasswordActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.EditPasswordListener;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class PasswordActivity extends SuperActivity implements EditPasswordListener {

	EditPasswordActor mActor;
	CustomProgressDialog mProgressDialog;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false);
		setContentView(R.layout.me_password);
		btn=(Button) findViewById(R.id.getYzm);
		mActor = new EditPasswordActor(this);
		mActor.initView();
	}
	
	/*============================ Button Click Events =========================*/
	/**
	 * 点击验证码按钮
	 * @param v
	 */
	public void onClickGetYzmBtn(View v){
		mActor.requestYzm();
		btn.setClickable(false);
		
	}

	/**
	 * 发送更改要求
	 * @param v
	 */
	public void onClickPostBtn(View v){
		mProgressDialog = CustomProgressDialog.createDialog(this);
		mProgressDialog.show();
		mActor.postEdit();
	}


	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_USER_CHPWD:
			mProgressDialog.dismiss();
			break;
		default:
			break;
		}
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_USER_CHPWD:
			mProgressDialog.dismiss();
			new AlertDialog.Builder(this).setTitle("密码修改").setMessage("密码修改成功").setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
			break;
		case D.API_USER_SENDYZM:
			break;
		default:
			break;
		}
	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);	
	}
	

	@Override
	public void onCountDownBegin() {
		mActor.turnOffYzmButton();
		
	}
	
	

	@Override
	public void onFlashSecond(int second) {
		mActor.flashYzmButton(String.valueOf(second));
	}
	

	@Override
	public void onCountDownEnd() {
		mActor.activeYzmButton();
		btn.setClickable(true);
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}

