package com.iteambuysale.zhongtuan.activity.login;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.actor.login.LoginActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.login.LoginListener;
import com.iteambuysale.zhongtuan.model.Collection;
import com.iteambuysale.zhongtuan.model.Evaluation;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderTG;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.model.TeMaiEvaluation;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class LoginActivity extends SuperActivity implements LoginListener{
	private LoginActor actor;
	private CustomProgressDialog progress;
	private static String lognum;
	private static String beforphoneNumber;
	private boolean isfromme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		boolean isHome=getIntent().getBooleanExtra("isHome", true);
		super.onCreate(savedInstanceState,isHome);
		//super.onCreate(savedInstanceState, false);
		actor = new LoginActor(this);
		setContentView(R.layout.login_main2);
		isfromme = getIntent().getBooleanExtra("isfromme",false);
		actor.initView();
		Button new_user=(Button) findViewById(R.id.new_user);
		new_user.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		TextView forget_password=(TextView)findViewById(R.id.forget_password);
		forget_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
	}
	
	/* ==================== Button Click Events ================= */

	/**
	 * 登录按钮被点击
	 * @param v
	 */
	public void onClickLoginBtn(View v){
		progress=CustomProgressDialog.createDialog(this);
		progress.show();
		beforphoneNumber = ZhongTuanApp.getInstance().getAppSettings().phoneNumber;
		lognum = actor.getReversText(actor.$et("phone"));
		actor.Login();
	}
	
	/**
	 * 忘记密码按钮被点击
	 * @param v
	 */
	public void onClickForgetPwdBtn(View v){
		LogUtilities.Log(D.DEBUG, "click forget",D.DEBUG_DEBUG);
		Intent intent = new Intent(this, GetPhoneNumber.class);
		startActivity(intent);
	}
	
	/**
	 * 注册按钮被点击
	 * @param v
	 */
	public void onClickRegistBtn(View v){
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	/*============================ Status Events ============================*/
   public void finishthisActivity(View v){
	   finish();
   }
	/**
	 * 登录失败
	 */
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		progress.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 登录成功
	 */
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		progress.dismiss();
		//清除必要的数据库数据
		cleardatabase();
		/*Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);*/
		/*ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(10);
		for(RunningTaskInfo info :runningTasks){
			System.out.println(info.hashCode());//;
		}
		System.out.println("-----------------");*/
		if(isfromme){
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		finish();
	}
	
	public static void cleardatabase() {
			Model.delete(OrderTM.class);
			Model.delete(OrderTG.class);
			Model.delete(Collection.class);
			Model.delete(UserAddress.class);
			Model.delete(Evaluation.class);
			Model.delete(TeMaiEvaluation.class);
			//Model.delete(User.class);
		
	}

	/**
	 * 校验失败
	 */
	@Override
	public void onValidateFailed(String errMsg) {
		progress.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 */
	@Override
	public void onTokenTimeout() {
//		ZhongTuanApp.getInstance().logout(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	/*@Override
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
	}*/
	
	
}