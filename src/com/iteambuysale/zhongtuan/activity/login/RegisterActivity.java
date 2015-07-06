package com.iteambuysale.zhongtuan.activity.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.actor.login.RegisterActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.login.RegisterListener;
import com.iteambuysale.zhongtuan.model.DateTime;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class RegisterActivity extends SuperActivity implements DatePickerDialog.OnDateSetListener, RegisterListener {

	RegisterActor actor;
	CustomProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);
		setContentView(R.layout.register_main2);
		actor = new RegisterActor(this);
		actor.initViews();
	}

	/* ==================== Button Click Events ================= */
	/**
	 * 点击系统返回键
	 */
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		 return super.onKeyDown(keyCode, event);
	 }
	
	/**
	 * 点击下一步按钮
	 * @param v
	 */
	public void onClickNextBtn(View v) {
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.show();
		//actor.checkRegistAble();
		actor.veryYzm();
	}

	/**
	 * 点击生日区域
	 * @param v
	 */
	public void onClickBirthdayArea(View v) {
		DateTime date = actor.getDateTime();
		new DatePickerDialog(this, RegisterActivity.this, date.getYear(), date.getMonth(), date.getDay()).show();
	}

	/**
	 * 点击获取验证码按钮
	 * @param v
	 */
	public void onClickGetYzmBtn(View v) {
		actor.beginCountDown();
		actor.checkRegistAble();
		//actor.getYzm();
	}

	/**
	 * 点击注册按钮
	 * @param v
	 */
	public void onClickRegisterBtn(View v) {
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.show();
		actor.regist();
	}
   public void finishthisActivity(View view){
	   onClickBtnBack(view);
   }
	/**
	 * 点击返回按钮
	 */
	@Override
	public void onClickBtnBack(View view) {
		if (View.VISIBLE == actor.$ll("first").getVisibility()) {
			super.onClickBtnBack(view);
		}
		actor.toFirstPage();
	}

	/* ==================== Status Events ==================== */

	/**
	 * 处理日期选择器结果
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		DateTime date = new DateTime( year, monthOfYear,dayOfMonth);
		actor.showDateTime(date);
	}

	/**
	 * 处理失败结果
	 */
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_USER_SENDYZM:
			//TODO:发送验证码返回失败结果
			//actor.stopCountDown();
			break;
		case  D.API_USER_CHECKUSERNAME:
			actor.stopCountDown();
			break;
		
		default:
			if(progressDialog!=null&&progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			break;
		}
		Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 处理成功结果
	 */
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if(progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_USER_CHECKUSERNAME: // 用户名可以注册
			actor.getYzm();
			//actor.nextPage();
			return;
		case D.API_USER_SENDYZM:
			Toast.makeText(this, "验证码已发送,请留意查收", Toast.LENGTH_LONG).show();
			return;
		case D.API_USER_REGISTER:
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("tag", D.OPT_NEAR);
			startActivity(intent);
			finish();
			break;
		case D.VERY_YZM:
			actor.nextPage(); 
			return;
		}
	}

	/**
	 * 倒计时开始
	 */
	@Override
	public void onCountDownBegin() {
		actor.turnOffYzmButton();
	}

	/**
	 * 刷新倒计时显示
	 */
	@Override
	public void onFlashSecond(int second) {
		actor.flashYzmButton(String.valueOf(second));
	}

	/**
	 * 倒计时完成
	 */
	@Override
	public void onCountDownEnd() {
		actor.activeYzmButton();
	}

	/**
	 * 校验失败
	 */
	@Override
	public void onValidateFailed(String result) {
		if(progressDialog!=null&&progressDialog.isShowing()){
			
			progressDialog.dismiss();
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 
	 */
	@Override
	public void onTokenTimeout() {
		
	}

	/*==================================== text watcher =============================*/
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if(s.length()==6){
			actor.activeRegist();
			return;
		}
		actor.turnOffRegist();
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
