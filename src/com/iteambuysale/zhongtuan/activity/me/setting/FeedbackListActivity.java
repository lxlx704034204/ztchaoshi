package com.iteambuysale.zhongtuan.activity.me.setting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.FeedbackAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Feedback;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class FeedbackListActivity extends Activity implements NetAsyncListener{
	ListView feedbackLv;
	CustomProgressDialog mDialog;
	TextView title;
	ArrayList<Feedback> fbData=new ArrayList<Feedback>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_list);
		feedbackLv=(ListView) findViewById(R.id.feedback_lv);
		title=(TextView) findViewById(R.id.tv_header_tittle);
		
		title.setText("反馈意见");
		mDialog=CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync task=new NetAsync(D.API_GET_FEEDBACK, this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
			java.lang.reflect.Type type=new TypeToken<ArrayList<Feedback>>(){}.getType();
			Gson gson=new Gson();
			fbData=gson.fromJson(elData, type);
				return fbData;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				
			}
		};
		task.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mDialog.dismiss();
		FeedbackAdapter adapter=new FeedbackAdapter(this, fbData);
		feedbackLv.setAdapter(adapter);
		
	}
	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,false);
		
	}
	

}
