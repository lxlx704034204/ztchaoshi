package com.iteambuysale.zhongtuan.activity.login;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.actor.EditPasswordActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.me.EditPasswordListener;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class GetPhoneNumber extends BaseActivity implements NetAsyncListener,EditPasswordListener{
	EditText inputText;
	Button nextBtn,back;
	EditPasswordActor mActor;
	CustomProgressDialog mDialog;
	String mPhoneNumber;
	TextView tittle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getphone);
		inputText=(EditText) findViewById(R.id.input_num);
		nextBtn=(Button) findViewById(R.id.nextbtn);
		back=(Button) findViewById(R.id.back);
		tittle=(TextView) findViewById(R.id.tv_header_tittle);
		mActor=new EditPasswordActor(this);
		tittle.setText("找回密码");
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
			finish();				
			}
		});
		
		
	}

	public void onNextClick(View v) {
		if(inputText.getText().toString().trim().length()==11){
			mPhoneNumber=inputText.getText().toString().trim();
			mActor.setTag("0");
			mActor.setPhoneNumber(mPhoneNumber);
			mDialog=CustomProgressDialog.createDialog(this);
			mDialog.show();
			checkRegistAble();			
		}
		else{
			Toast.makeText(this, "手机号码长度不对！",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		Intent intent=new Intent(GetPhoneNumber.this,ForgetPasswordActivity.class);
		intent.putExtra("phone",mActor.getPhoneNumber());
		intent.putExtra("tag",mActor.getTag());			
		startActivity(intent);
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onTokenTimeout() {
//		ZhongTuanApp.getInstance().logout(this);
		
	}

	public void checkRegistAble() {

		NetAsync task_checkName = new NetAsync(D.API_USER_CHECKUSERNAME,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_LOGIN_PHONE, StringUtilities.getReverseString(mPhoneNumber)));
			}
		};
		task_checkName.execute();
	}

	@Override
	public void onCountDownBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlashSecond(int second) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCountDownEnd() {
		// TODO Auto-generated method stub
		
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
