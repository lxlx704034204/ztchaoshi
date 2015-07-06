package com.iteambuysale.zhongtuan.activity.me.setting;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.actor.me.setting.FeedbackActor;
import com.iteambuysale.zhongtuan.listener.me.FeedBackListener;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class FeedbackActivity extends SuperActivity implements FeedBackListener{
	FeedbackActor mActor;
	CustomProgressDialog mProgressDialog;
	Button back,advicesBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false);
		setContentView(R.layout.me_feedback);
		mActor = new FeedbackActor(this);
		mActor.initview();
		back=(Button) findViewById(R.id.back);
		advicesBtn=(Button) findViewById(R.id.setting);
		advicesBtn.setVisibility(View.VISIBLE);
		advicesBtn.setText("更多反馈");
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		advicesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(FeedbackActivity.this,FeedbackListActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	/* ================================ Button Click Events ======================== */
	public void onClickPostBtn(View v){
		mProgressDialog = CustomProgressDialog.createDialog(this);
		mProgressDialog.show();
		mActor.postFeedback();
	}

	/* =================================== Status Events ============================= */
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mProgressDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mProgressDialog.dismiss();
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("反馈意见");
		builder.setMessage("您的意见我们已经收到，感谢您的支持！我们将继续努力做到最好");
		builder.setPositiveButton("完成", this);
		builder.create().show();
	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
		finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		finish();
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
