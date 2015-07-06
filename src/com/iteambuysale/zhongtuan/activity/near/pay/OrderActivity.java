package com.iteambuysale.zhongtuan.activity.near.pay;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class OrderActivity extends BaseActivity implements OnClickListener,
		NetAsyncListener {
	Button back, btn_decrease, btn_plus, btn_order, btn_changephone;
	TextView tittle, tv_productname, tv_productprice, tv_sum, tv_cost, tv_tel,
			costmoney;
	int sum = 1;
	float cost = 0;
	String shop, cpid, cpmc, cppic, cpdj, mobile, truename, lngo, lato,
			province, city;
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.near_buynow);
		
		initData();
		findViews();
		setViews();
		initListener();

	}
	/**
	 * 初始化所有提交订单需要的消息
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void initData() {
		Intent intent = getIntent();
		shop = intent.getStringExtra("shop");
		cpid = intent.getStringExtra("cpid");
		cpmc = intent.getStringExtra("cpmc");
		if(cpmc==null){
			cpmc = intent.getStringExtra("productname");
		}
		cppic = intent.getStringExtra("cppic");
		cpdj = intent.getStringExtra("cpdj");
		SharedPreferences pre = getSharedPreferences("zhongtuan_preference",
				Context.MODE_PRIVATE);
		lngo = pre.getString("lgn", "");
		lato = pre.getString("lat", "");
		city = pre.getString("cityId", "");

		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		Cursor cursor = db.query("USER_LIST", null, null, null, null, null,
				null);
		cursor.moveToFirst();
		mobile = cursor.getString(cursor.getColumnIndex("_mobile"));
		truename = cursor.getString(cursor.getColumnIndex("_nickname"));
		cursor.close();
		SQLiteDatabase cityDB = ZhongTuanApp.getInstance().getPcdDB();
		Cursor location = cityDB.query(D.DB_TABLE_CITY, null,D.DB_CITY_COL_ID+" = ?",
				new String[] { city }, null, null, null);
		if(location.moveToFirst()){
		province = location.getString(location.getColumnIndex(D.DB_CITY_COL_PROVINCE_ID));
		}
	}
	
	/**
	 * 初始化所有控件
	 * 2015-1-13
	 * lforxeverc
	 */
	private void findViews() {
		tv_productname = (TextView) findViewById(R.id.tv_productname);
		tv_productprice = (TextView) findViewById(R.id.tv_productprice);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_cost = (TextView) findViewById(R.id.tv_cost);
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		costmoney = (TextView) findViewById(R.id.costmoney);
		btn_decrease = (Button) findViewById(R.id.btn_decrease);
		btn_plus = (Button) findViewById(R.id.btn_plus);
		btn_order = (Button) findViewById(R.id.btn_order);
		btn_changephone = (Button) findViewById(R.id.btn_changephone);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		back = (Button) findViewById(R.id.back);
		
	}
	
	/**
	 * 设置控件的值
	 * 2015-1-13
	 * lforxeverc
	 */
	private void setViews() {
		tittle.setText("提交订单");
		tv_productname.setText(cpmc);
		tv_cost.setText(cpdj);
		tv_productprice.setText(cpdj);
		costmoney.setText(cpdj);
		tv_tel.setText(mobile);
		cost = Float.parseFloat(cpdj);
		back.setBackgroundResource(R.drawable.header_back);
		
	}

	/**
	 * 给控件添加监听器
	 * 2015-1-13
	 * lforxeverc
	 */
	private void initListener() {
		back.setOnClickListener(this);
		btn_decrease.setOnClickListener(this);
		btn_plus.setOnClickListener(this);
		btn_order.setOnClickListener(this);
		btn_changephone.setOnClickListener(this);
		tv_tel.setOnClickListener(this);
		
	}
	

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		//数目减少监听
		case R.id.btn_decrease:
			countDec();			
			break;
		//数目增加监听事件
		case R.id.btn_plus:
			
			countInc();
			
			break;
		//下单监听事件
		case R.id.btn_order:

			orderRequest();

			break;
		//更改手机号码事件，跳转到更改电话页面
		case R.id.btn_changephone:
			Intent intent = new Intent(OrderActivity.this,
					ChangeMobileActivity.class);
			startActivityForResult(intent, 1);
			break;
		//整行监听更改电话事件
		case R.id.tv_tel:
			Intent intent1 = new Intent(OrderActivity.this,
					ChangeMobileActivity.class);
			startActivityForResult(intent1, 1);
			break;
		//顶部返回按钮
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}
	/**
	 * 点击数目增加按钮触发事件
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void countInc() {
		if (Integer.parseInt(tv_sum.getText().toString()) >= 0) {
			btn_decrease.setBackgroundResource(R.drawable.btn_decrease_on);
			sum++;
			tv_sum.setText(sum + "");
			cost = (float) Math.round(Float.parseFloat(cpdj) * sum * 100) / 100;
			tv_cost.setText(cost + "");
			costmoney.setText(cost + "");
		}
		
	}
	/**
	 * 点击数目减少按钮触发事件
	 * 
	 * 2015-1-13
	 * lforxeverc
	 */
	private void countDec() {
		if (Integer.parseInt(tv_sum.getText().toString()) > 1) {
			btn_decrease.setBackgroundResource(R.drawable.btn_decrease_on);
			sum--;
			if (sum == 1) {
				btn_decrease
						.setBackgroundResource(R.drawable.btn_decrease_off);
			}

			cost = (float) Math.round(Float.parseFloat(cpdj) * sum * 100) / 100;
			tv_sum.setText(sum + "");
			tv_cost.setText(cost + "");
			costmoney.setText(cost + "");
		} else {
			btn_decrease.setBackgroundResource(R.drawable.btn_decrease_off);
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		//修改了电话信息
		case 1:
			mobile=data.getStringExtra("mobile");
			tv_tel.setText(mobile);
			break;
		//未修改电话信息，不作处理
		case 0:

			break;

		default:
			break;
		}
	}
	/**
	 * 根据信息下订单请求
	 * 2015-1-13
	 * lforxeverc
	 */
	private void orderRequest() {
		dialog = CustomProgressDialog.createDialog(this);
		dialog.show();
		NetAsync orderTask = new NetAsync(D.API_PRODUCT_TG_ORDER, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> item = JsonUtilities.parseModelByType(
						elData, type);
				//获得订单号
				String ordno = item.get("ordno");
				return ordno;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("lngo", lngo));
				params.add(new BasicNameValuePair("lato", lato));
				params.add(new BasicNameValuePair("shop", shop));
				params.add(new BasicNameValuePair("province", province));
				params.add(new BasicNameValuePair("city", city));
				params.add(new BasicNameValuePair("truename", truename));
				params.add(new BasicNameValuePair("mobile", mobile));
				params.add(new BasicNameValuePair("cpid", cpid));
				params.add(new BasicNameValuePair("cpmc", cpmc));
				params.add(new BasicNameValuePair("cppic", cppic));
				params.add(new BasicNameValuePair("cpsl", sum + ""));
				params.add(new BasicNameValuePair("cpdj", cpdj));
				params.add(new BasicNameValuePair("cpje", cost + ""));
			}
		};
		orderTask.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		dialog.dismiss();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		dialog.dismiss();
		//下单成功跳转到支付页面
		Intent intent = new Intent(OrderActivity.this, PayActivity.class);
		intent.putExtra("ordno", data.toString());
		intent.putExtra("productname", cpmc);
		intent.putExtra("cpje", cost + "");
		intent.putExtra("sum",sum);
		intent.putExtra("id", cpid);
		startActivity(intent);
	}

	@Override
	public void onTokenTimeout() {
		dialog.dismiss();
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
