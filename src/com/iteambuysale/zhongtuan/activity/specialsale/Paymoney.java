package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.iteambuysale.zhongtuan.activity.me.unpay.UnpayActivity_l;
import com.iteambuysale.zhongtuan.background.FileDownLoadService;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.pingplusplus.android.PaymentActivity;

public class Paymoney extends BaseActivity implements OnCheckedChangeListener,
		NetAsyncListener, OnClickListener, android.view.View.OnClickListener {
	private static final int REQUEST_CODE_PAYMENT = 1;

	private TextView tittle/*,tv_productname,tv_primeprice*/,tv_price;
	private RadioButton rbtn1,rbtn2,rbtn3;
	private Button btn_submit,backBtn;
	CustomProgressDialog dialog;
	RelativeLayout rl1,rl2,rl3;
	private String addrid;
	private String ordno;
	private String payMetho;

	private TextView tv_sum_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		payMetho = D.PAY_BY_ALI;
		initview();
		initdata();
	}

	private void initview() {
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		backBtn = (Button) findViewById(R.id.back);
		//tv_productname = (TextView) findViewById(R.id.tv_productname);
		//tv_primeprice = (TextView) findViewById(R.id.tv_primeprice);
		tv_sum_price=(TextView)findViewById(R.id.tv_sum_price);
		tv_price = (TextView) findViewById(R.id.tv_price);
		rbtn1 = (RadioButton) findViewById(R.id.rbtn1);
		rbtn1.setChecked(true);
		rbtn2 = (RadioButton) findViewById(R.id.rbtn2);
		rbtn3 = (RadioButton) findViewById(R.id.rbtn3);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		rl1=(RelativeLayout) findViewById(R.id.rl1);
		rl2=(RelativeLayout) findViewById(R.id.rl2);
		rl3=(RelativeLayout) findViewById(R.id.rl3);
		btn_submit.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		radiogroup.setOnCheckedChangeListener(this);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl3.setOnClickListener(this);
		
	}

	private void initdata() {
		tittle.setText("支付收银台");
		backBtn.setBackgroundResource(R.drawable.header_back);
		//String title = (String) getIntent().getCharSequenceExtra("productname");
		String xiaoji = (String) getIntent().getCharSequenceExtra("cpje");
		//String id=getIntent().getStringExtra("id");
		//int sum=getIntent().getIntExtra("sum",1);
		ordno = (String) getIntent().getCharSequenceExtra("ordno");
		tv_sum_price.setText("￥"+xiaoji);
	/*	Cursor cr=ZhongTuanApp.getInstance().getWDB().query("TEMAI_LIST_VERSON",null,"_id=?",new String[]{id} , null,null,null);
		String primePrice="1";
		if(cr.moveToFirst()){
			primePrice=cr.getString(cr.getColumnIndex("_dj0"));
		}
//		String primePrice=getIntent().getStringExtra("primePrice");
		
		float pAll=Math.round(Float.parseFloat(primePrice)*100*sum)/100;
	//	tv_productname.setText(title);
		tv_price.setText(xiaoji + "");
		//tv_primeprice.setText(pAll + "");
		Cursor cursor = DBUtilities.getdefmsg();
		if (cursor.moveToFirst()) {
			addrid = cursor.getString(cursor.getColumnIndex("_id"));
		}*/
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
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
	private void pay() {
		dialog = CustomProgressDialog.createDialog(this);
		dialog.show();
		NetAsync repayTask = new NetAsync(D.API_SPECIAL_CREATEPAY, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, JsonElement>>() {
				}.getType();
				Map<String, JsonElement> mResult = JsonUtilities
						.parseModelByType(elData, type);
				JsonElement jCredential = mResult.get("charge");
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
	public void onResultError(String reqUrl, String errMsg) {
		dialog.dismiss();
		if (TextUtils.equals(reqUrl, D.API_SPECIAL_CREATEPAY)) {
			String msg = "支付失败！";
			jumpToMyOrder(1);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {

		case D.API_SPECIAL_CREATEPAY:
			dialog.dismiss();
			String vouchers = (String) data;
			System.out.println("data:"+data);
			Intent intent_co = new Intent();
			String packageName = getPackageName();
			ComponentName componentName = new ComponentName(packageName,
					packageName + ".wxapi.WXPayEntryActivity");
			intent_co.setComponent(componentName);
			LogUtilities.Log(D.DEBUG + " input", vouchers,D.DEBUG_DEBUG);
			intent_co.putExtra(PaymentActivity.EXTRA_CHARGE, vouchers); // credential
			startActivityForResult(intent_co, REQUEST_CODE_PAYMENT);
			break;
		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		dialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);

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
				Intent intent = new Intent(Paymoney.this, SpecialSalePaySuccess.class);
				Intent intent_clear=new Intent(Paymoney.this,HomeActivity.class);
				intent_clear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_clear);
				startActivity(intent);
//				startActivities(new Intent[]{intent_clear,intent});
				
				return;
			} else if (str.equalsIgnoreCase("fail")) {
				msg = "支付失败！";
				jumpToMyOrder(1);
				return;
			} else if (str.equalsIgnoreCase("cancel")) {
				msg = "用户取消了支付";
				jumpToMyOrder(1);
				return;
			} else if (str.equalsIgnoreCase("invalid")) {
				msg = "未安装第三方支付控件";
				// mActor.notifyDownLoadPaymentServices();
				dialog.dismiss();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
				if (TextUtils.equals(payMetho, D.PAY_BY_ALI)) {
					builder.setTitle("下载");
					builder.setMessage("缺少支付宝客户端，是否现在下载？");
					builder.setPositiveButton("立刻下载", this);
					builder.setNegativeButton("等等，我考虑一下", this);
				}else if(TextUtils.equals(payMetho, D.PAY_BY_YINLIAN)){
					builder.setTitle("下载");
					builder.setMessage("缺少银联客户端，是否现在下载？");
					builder.setPositiveButton("立刻下载", this);
					builder.setNegativeButton("等等，我考虑一下", this);
				}else if(TextUtils.equals(payMetho, D.PAY_BY_WECHAT)){
					builder.setTitle("您还未安装微信，请先下载微信");
				}
				
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
		/*Intent intent = new Intent(this, UnpayActivity.class);
		intent.putExtra("tgOrtm", tag);*/
		Intent intent = new Intent(this, UnpayActivity_l.class);
		intent.putExtra("orderStatus", 0);
		startActivity(intent);
		Intent intent_clear=new Intent(Paymoney.this,HomeActivity.class);
		intent_clear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivities(new Intent[]{intent_clear,intent});
//		finish();
		startActivity(intent_clear);
		startActivity(intent);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			Intent intent = new Intent(this, FileDownLoadService.class);
			intent.putExtra("url", D.DOWN_YIN_LIAN);
			startService(intent);
			break;
		case Dialog.BUTTON_NEGATIVE:
			dialog.dismiss();
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_submit:
			pay();
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

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
