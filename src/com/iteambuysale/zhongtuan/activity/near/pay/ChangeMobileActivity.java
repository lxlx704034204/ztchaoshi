package com.iteambuysale.zhongtuan.activity.near.pay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;

public class ChangeMobileActivity extends BaseActivity implements OnClickListener {
	Button back, commit, cancel;
	EditText inputNumber;
	TextView tittle;
	String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_mobile);
		findViews();
		initData();
		
	}
	/**
	 * 查找views
	 * 2015-1-13
	 * lforxeverc
	 */
	private void findViews() {
		back = (Button) findViewById(R.id.back);
		commit = (Button) findViewById(R.id.commit);
		cancel = (Button) findViewById(R.id.cancel);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		inputNumber=(EditText) findViewById(R.id.et_input);
		
	}
	/**
	 * 初始化view的值和监听器
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void initData() {
		tittle.setText("更换手机号码");
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(this);
		commit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		//返回按钮事件
		case R.id.back:
			Intent intent=new Intent(ChangeMobileActivity.this,OrderActivity.class);
			setResult(0, intent);
			finish();
			break;
		//确定按钮事件
		case R.id.commit:
		mobile=inputNumber.getText().toString().trim();
		if(mobile.length()==11){
			showDialog();
		}
		else{
			Toast.makeText(ChangeMobileActivity.this, "手机号码长度不对！", Toast.LENGTH_SHORT).show();
		}
			break;
		case R.id.cancel:
			Intent intent2=new Intent(ChangeMobileActivity.this,OrderActivity.class);
			setResult(0, intent2);
			finish();
			break;

		default:
			break;
		}

	}
	/**
	 * 显示确认信息对话框
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void showDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("确认信息:");
		builder.setMessage("请确认您的号码："+inputNumber.getText().toString());
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				Intent intent1=new Intent(ChangeMobileActivity.this,OrderActivity.class);
				intent1.putExtra("mobile",mobile);
				setResult(1, intent1);
				finish();
			}
		}); 
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();				
			}
		});
		builder.create().show();
			
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