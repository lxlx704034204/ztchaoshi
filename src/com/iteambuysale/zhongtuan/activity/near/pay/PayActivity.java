package com.iteambuysale.zhongtuan.activity.near.pay;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.UnpayActivity;
import com.iteambuysale.zhongtuan.background.FileDownLoadService;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.near.OrderListener;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.pingplusplus.android.PaymentActivity;
/**
 * 支付页面
 * @author lforxeverc
 * 2015-1-13
 */
public class PayActivity extends BaseActivity implements OnClickListener,
		OrderListener, NetAsyncListener, OnCheckedChangeListener {
	TextView tittle, tv_productname, tv_primeprice, tv_price;
	Button backBtn, submitBtn;
	String productName, price, ordno, payMetho,productId;
	float all;
	RadioGroup radiogroup;
	RadioButton rbtn1, rbtn2, rbtn3;
	RelativeLayout rl1,rl2,rl3;
	CustomProgressDialog mDialog;
	OrderListener mOrderListener;
	NetAsyncListener mListener;
	private static int REQUEST_CODE_PAYMENT = 1;
	//private int currentstate = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);

		findViews();
		initData();
		initListener();

		rbtn1.performClick();

	}

	/**
	 * 初始化监听器 2015-1-13 lforxeverc
	 */
	private void initListener() {
		mOrderListener = this;
		mListener = this;
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		radiogroup.setOnCheckedChangeListener(this);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl3.setOnClickListener(this);

	}

	/**
	 * 设置Views
	 * 
	 * 2015-1-13 Administrator
	 */
	private void initData() {
		productName = getIntent().getStringExtra("productname");
		price = getIntent().getStringExtra("cpje");
		ordno = getIntent().getStringExtra("ordno");
		productId=getIntent().getStringExtra("id");
		int sum=getIntent().getIntExtra("sum",1);
		Cursor c=ZhongTuanApp.getInstance().getRDB().query("PRODUCT_LIST",null,"_id = ?", new String[]{productId}, null,null,null);
		if(c.moveToFirst()){
		String primePrice=c.getString(c.getColumnIndex("_dj0"));
		c.close();
		all = (float) Math.round(Float.parseFloat(primePrice) * sum * 100) / 100;
		}
		//currentstate = getIntent().getIntExtra("currentstate", -1);
		tittle.setText("支付收银台");
		backBtn.setBackgroundResource(R.drawable.header_back);
		tv_productname.setText(productName);
		tv_primeprice.setText(all + "元");
		tv_price.setText(price + "元");

	}

	/**
	 * 找到views 2015-1-13 lforxeverc
	 */
	private void findViews() {
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		backBtn = (Button) findViewById(R.id.back);
		submitBtn = (Button) findViewById(R.id.btn_submit);
		tv_productname = (TextView) findViewById(R.id.tv_productname);
		//tv_primeprice = (TextView) findViewById(R.id.tv_primeprice);
		tv_price = (TextView) findViewById(R.id.tv_price);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		rbtn1 = (RadioButton) findViewById(R.id.rbtn1);
		rbtn2 = (RadioButton) findViewById(R.id.rbtn2);
		rbtn3 = (RadioButton) findViewById(R.id.rbtn3);
		rl1=(RelativeLayout) findViewById(R.id.rl1);
		rl2=(RelativeLayout) findViewById(R.id.rl2);
		rl3=(RelativeLayout) findViewById(R.id.rl3);

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		// 返回事件
		case R.id.back:
			finish();
			break;
		// 提交按钮触发事件
		case R.id.btn_submit:
			/*if (currentstate == 1) {
              
				pay(D.API_SPECIAL_CREATEPAY);
			} else {*/
				pay(D.API_CPORD_CREATEPAY);
			//}

			break;
		case R.id.rl1:
			rbtn1.setChecked(true);
			break;
		case R.id.rl2:
			rbtn2.setChecked(true);
			break;
		case R.id.rl3:
			rbtn3.setChecked(true);
			break;
		default:
			break;
		}

	}

	/**
	 * 按照特卖或团购支付订单
	 * 
	 * @param url
	 *            2015-1-13 yg
	 */
	private void pay(String url) {
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync repayTask = new NetAsync(url, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, JsonElement>>() {
				}.getType();
				Map<String, JsonElement> mResult = JsonUtilities
						.parseModelByType(elData, type);
				JsonElement jCredential = mResult.get("credential");
				return jCredential.toString();

			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno", ordno));
				params.add(new BasicNameValuePair("paym", payMetho));

			}
		};
		repayTask.execute();

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		// 选择支付方式		
		case R.id.rbtn1:
			payMetho = D.PAY_BY_ALI;
			break;		
		case R.id.rbtn2:
			payMetho = D.PAY_BY_YINLIAN;
			break;
		case R.id.rbtn3:
			payMetho = D.PAY_BY_WECHAT;
			break;
		default:
			break;
		}

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_SPECIAL_CREATEPAY:
		case D.API_CPORD_CREATEPAY:
			mDialog.dismiss();
			String vouchers = (String) data;
			Intent intent_co = new Intent();
			String packageName = getPackageName();
			ComponentName componentName = new ComponentName(packageName,
					packageName + ".wxapi.WXPayEntryActivity");
			intent_co.setComponent(componentName);
			LogUtilities.Log(D.DEBUG + " input", vouchers, D.DEBUG_DEBUG);
			intent_co.putExtra(PaymentActivity.EXTRA_CREDENTIAL, vouchers); // credential
			startActivityForResult(intent_co, REQUEST_CODE_PAYMENT);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		switch (arg1) {
		case Dialog.BUTTON_POSITIVE:
			Intent intent = new Intent(this, FileDownLoadService.class);
			intent.putExtra("url", D.DOWN_YIN_LIAN);
			startService(intent);
			break;
		case Dialog.BUTTON_NEGATIVE:
			arg0.dismiss();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (requestCode == D.REQUEST_UAID && resultCode == D.RESPONSE_UAID) {
		// uaid = data.getStringExtra("uaid");
		// UserAddress address = Model.load(new UserAddress(), uaid);
		// mActor.upDateRecieveInfo(address);
		// return;
		// }
		// payment result process
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (data == null) {
				return;
			}
			String msg = null;
			/*
			 * 返回字符串:success, fail, cancel, invalid
			 * 分别代表支付成功，支付失败，支付取消，未安装第三方支付控件
			 */
			String str = data.getExtras().getString("pay_result");
			if (str.equalsIgnoreCase("success")) {
				msg = "支付成功！";
				// jumpToMyOrder(D.OPT_CONFORM);
				Intent intent = new Intent(PayActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("ordno", ordno);
				Intent intent_clear=new Intent(PayActivity.this,HomeActivity.class);
				intent_clear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_clear);
				startActivity(intent);
//				startActivities(new Intent[]{intent_clear,intent});
//				finish();
				return;
			} else if (str.equalsIgnoreCase("fail")) {
				msg = "支付失败！";
				jumpToMyOrder(0);
				return;
			} else if (str.equalsIgnoreCase("cancel")) {
				msg = "用户取消了支付";
				jumpToMyOrder(0);
				return;
			} else if (str.equalsIgnoreCase("invalid")) {
				msg = "未安装第三方支付控件";
				// mActor.notifyDownLoadPaymentServices();
				mDialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("下载");
				builder.setMessage("缺少银联客户端，是否现在下载？");
				builder.setPositiveButton("立刻下载", mOrderListener);
				builder.setNegativeButton("等等，我考虑一下", mOrderListener);
				builder.create().show();
			}
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 跳转到未支付订单页面，由于重用了特卖和团购的未支付页面，所以用Tag分割两个页面的跳转，0为团购，1为特卖
	 * @param tag 
	 * 2015-1-20
	 * lforxeverc
	 */
	public void jumpToMyOrder(int tag) {
		Intent intent = new Intent(this, UnpayActivity.class);
		intent.putExtra("tgOrtm", tag);
		Intent intent_clear=new Intent(PayActivity.this,HomeActivity.class);
		intent_clear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivities(new Intent[]{intent_clear,intent});
//		finish();
		startActivity(intent_clear);
		startActivity(intent);
		
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
