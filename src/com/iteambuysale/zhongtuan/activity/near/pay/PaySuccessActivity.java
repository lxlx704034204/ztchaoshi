package com.iteambuysale.zhongtuan.activity.near.pay;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.ZTQActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTG;
import com.iteambuysale.zhongtuan.model.ZTQ;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
/**
 * 支付成功页面，此页面要清activity栈
 * @author Administrator
 * 2015-1-13
 */
public class PaySuccessActivity extends BaseActivity implements NetAsyncListener,
		OnClickListener {
	CustomProgressDialog mDialog;
	String mobile, qno,ordno;
	TextView tvMobile, tvNo, tittle;
	Button conBtn, seeBtn, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_success);
		//支付成功，清栈，防止又一步一步返回
	
		findViews();
		initListener();		
		loadZTQ();

	}
	/**
	 * 找到views
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void findViews() {
		tvMobile = (TextView) findViewById(R.id.tel);
		tvNo = (TextView) findViewById(R.id.code);
		conBtn = (Button) findViewById(R.id.btn_buy_continue);
		seeBtn = (Button) findViewById(R.id.btn_see_ztq);
		back = (Button) findViewById(R.id.back);
		tittle=(TextView) findViewById(R.id.tv_header_tittle);		
		back.setBackgroundResource(R.drawable.header_back);
		tittle.setText("支付结果");
		ordno=getIntent().getStringExtra("ordno");
		
	}
	/**
	 * 初始化监听器
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void initListener() {
		conBtn.setOnClickListener(this);
		seeBtn.setOnClickListener(this);
		back.setOnClickListener(this);	
	}
	/**
	 * 加载中团券
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	public void loadZTQ() {
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync loadSumTask = new NetAsync(D.API_ME_ZTQ, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, ZTQ>>() {
				}.getType();
				Map<String, ZTQ> ztqMap = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(ZTQ.class);
				for (String key : ztqMap.keySet()) {
					ZTQ ztq = ztqMap.get(key);
					ztq.save();
					OrderDetailsTG[] details = ztq.getCpmx();
					OrderDetailsTG.save(details);
				}
				return ztqMap;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		loadSumTask.execute();

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mDialog.dismiss();
		updateView();
		

	}
	/**
	 * 成功加载后到数据库查询相应信息并显示在页面上
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void updateView() {
		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		Cursor ztqCursor = db.query("ZTQ_LIST", null, "_ordno = ?", new String[]{ordno}, null, null,
				null);
		ztqCursor.moveToFirst();
		mobile = ztqCursor.getString(ztqCursor.getColumnIndex("_mobile"));
		qno = ztqCursor.getString(ztqCursor.getColumnIndex("_qno"));
		tvMobile.setText(mobile);
		tvNo.setText(qno);
		ztqCursor.close();
		Toast.makeText(PaySuccessActivity.this,"中团券密码已发送至手机号码"+mobile , Toast.LENGTH_LONG).show();
		
	}
	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		//继续购物按钮，跳转到商家列表
		/*case R.id.btn_buy_continue:
			Intent intent1 = new Intent(PaySuccessActivity.this,
					NearBusinessActivity.class);
			intent1.putExtra("pTag", D.CATE_PTAG_1);
			intent1.putExtra("cTag", "");
			startActivity(intent1);
			finish();
			break;*/
		//查看中团券页面
		case R.id.btn_see_ztq:
			Intent intent2 = new Intent(PaySuccessActivity.this,
					ZTQActivity.class);
			startActivity(intent2);
			finish();
			break;
		case R.id.back:
			finish();

		default:
			break;
		}
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
