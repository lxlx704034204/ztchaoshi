package com.iteambuysale.zhongtuan.activity.me.setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.GuideActivity;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.activity.login.LoginActivity;
import com.iteambuysale.zhongtuan.actor.me.setting.SettingActor;
import com.iteambuysale.zhongtuan.define.AppSetting;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.SettingListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.model.Version;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SettingActivity extends SuperActivity implements SettingListener {
	SettingActor mActor;
	CustomProgressDialog mProgressDialog;
	int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);
		setContentView(R.layout.me_setting);
		mActor = new SettingActor(this);
		mActor.initView();
	}

	// “修改密码”按钮被点击
	public void onClickPassBtn(View v) {
		Intent intent = new Intent(this,PasswordActivity.class);
		startActivity(intent);
	}

	// “版权信息”按钮被点击
	public void onClickCopyrightBtn(View v) {
		Intent intent = new Intent(this,CopyrightActivity.class);
		startActivity(intent);

	}

	// “意见反馈”按钮被点击
	public void onClickFeedbackBtn(View v) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		startActivity(intent);
	}

	// “检查版本”按钮被点击
	public void onClickCheckVersionBtn(View v) {
		mProgressDialog = CustomProgressDialog.createDialog(this);
		mProgressDialog.show();
		mActor.checkVersion();
	}

	// “退出登录” 按钮被点击
	public void onClickQuiteBtn(View v) {
//		ZhongTuanApp.getInstance().pushActivity(this);
		LoginActivity.cleardatabase();
		Model.delete(User.class);
		clearperfer();
		ZhongTuanApp.getInstance().logout(this,true);
	}
    private void clearperfer(){
    	AppSetting setting = ZhongTuanApp.getInstance().getAppSettings();
		setting.uid = " ";
		setting.ackToken = " ";
		setting.session="";
		ZhongTuanApp.getInstance().persistSave();
    }
	// "关于我们"按钮 
	public void onClickAboutUsBtn(View v){
		Intent intent = new Intent(this,GuideActivity.class);
		intent.putExtra("IsHome", false);
		intent.putExtra("IsFromAbout",true);
		startActivity(intent);
//		finish();
	}	
	
	// "内部版本号区域点击"
	public void onClickVersionNameBtn(View v){
		count +=1;
		if (count == 8){
			count = 0;
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("版本信息");
			builder.setMessage("内部版本识别名:"+D.VERSION_NAME);
			builder.setPositiveButton("关闭提示", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.setCancelable(false);
			builder.create().show();
		}
	}
	

	/*================================== Status Events =============================*/
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mProgressDialog.dismiss();
		Toast.makeText(this, "获取版本信息失败:"+errMsg, Toast.LENGTH_LONG).show();		
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		final Version version = (Version)data;
		mProgressDialog.dismiss();
		if(D.VERSION.equals(version.getAppver())){
			Toast.makeText(this, "已经是最新的版本", Toast.LENGTH_LONG).show();
			return;
		}
		Builder alertVersionBuilder = new AlertDialog.Builder(this);
		alertVersionBuilder.setTitle("更新");
		alertVersionBuilder.setMessage("有新的版本" + version.getAppver() + "可以下载!");
		alertVersionBuilder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri download_url = Uri.parse(version.getAppurl());
				intent.setData(download_url);
				startActivity(intent);
			}
		});
		alertVersionBuilder.setNegativeButton("下次再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertVersionBuilder.create().show();		
	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);	
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
