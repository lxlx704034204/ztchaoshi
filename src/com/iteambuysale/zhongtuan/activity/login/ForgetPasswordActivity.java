package com.iteambuysale.zhongtuan.activity.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

public class ForgetPasswordActivity extends SuperActivity implements EditPasswordListener {
	EditPasswordActor mActor;
	CustomProgressDialog mProgressDialog;
	String mTag;
	String mPhoneNumber;
	Button back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_password);
		back=(Button) findViewById(R.id.back);
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();			
			}				
		});
		Intent intent=getIntent();
		mTag=intent.getExtras().getString("tag");
		mPhoneNumber=intent.getExtras().getString("phone");
		
		mActor = new EditPasswordActor(this);
		mActor.setTag(mTag);
		mActor.setPhoneNumber(mPhoneNumber);
		mActor.initView();
	}
	/**
	 * 点击验证码按钮
	 * @param v
	 */
	public void onClickGetYzmBtn(View v){
		mActor.requestYzm();
		mActor.$btn("getYzm").setClickable(false);
		
	}
	
	public void onClickPostBtn(View v){
		mProgressDialog = CustomProgressDialog.createDialog(this);
		mProgressDialog.show();
		mActor.postEdit();
	}
	@Override
	public void onCountDownBegin() {
		mActor.turnOffYzmButton();
		
	}
	@Override
	public void onFlashSecond(int second) {
		mActor.flashYzmButton(second+"");
		
	}
	@Override
	public void onCountDownEnd() {
		mActor.activeYzmButton();
		
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub
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
					Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
					startActivity(intent);
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
//		ZhongTuanApp.getInstance().logout(this);
	}
}

