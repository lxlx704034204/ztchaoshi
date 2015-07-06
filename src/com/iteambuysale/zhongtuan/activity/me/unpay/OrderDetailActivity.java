package com.iteambuysale.zhongtuan.activity.me.unpay;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.near.pay.PayActivity;
import com.iteambuysale.zhongtuan.activity.specialsale.Paymoney;
import com.iteambuysale.zhongtuan.activity.specialsale.PhotoWordDtetial;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.model.ZTQ;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class OrderDetailActivity extends BaseActivity implements
		OnClickListener, NetAsyncListener {
	TextView tittle, tv_tittle, tv_price, tv_prime_price, tv_detail, tv_taocan,
			tv_ordno,tv_time_tag, tv_time, tv_mobile, tv_sum, tv_all, tv_end_time, tv_ztq,
			tv_Qstate, tv_Ostate;
	Button back, detailbtn, btn_buy, refund_btn;
	RelativeLayout rl, rl_ztq;
	String id,  ordno, picUrl, productName, primePrice, nowPrice, content,
			taocan, payTime, mobile, sum, allPrice, productId, shopId, edate,
			ztq, state, ztqState, ordState;
	SQLiteDatabase db;
	ImageView picView;
	CustomProgressDialog mDialog;
	Cursor orderCursor, productCursor, ztqCursor;
	private int TGorTM;
	private int TUANGOU=0;
	private int TEMAI=1;
	private static final int  UNPAY=0;
	private static final int PAYED=1;
	private int activityTag=0;
	private TextView time_tag;
	private TextView t2;
	private boolean istuikuanstate=false;
	@Override
	protected void onDestroy() {
		if (orderCursor != null) {
			super.onDestroy();
		}
		if (productCursor != null) {
			orderCursor.close();
		}
		if (ztqCursor != null) {
			ztqCursor.close();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		db = ZhongTuanApp.getInstance().getRDB();
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		activityTag = intent.getIntExtra("activityTag",0);
		state = intent.getStringExtra("payState");
		TGorTM = intent.getIntExtra("TGorTM", 0);
		rl_ztq = (RelativeLayout) findViewById(R.id.rl_ztq);
		t2 = (TextView) findViewById(R.id.t2);
		switch (activityTag) {
		case UNPAY:
			tv_time_tag=(TextView) findViewById(R.id.tv_time_tag);
			tv_time_tag.setText("下单时间");
			btn_buy = (Button) findViewById(R.id.btn_buy);
			refund_btn = (Button) findViewById(R.id.refund_btn);
			btn_buy.setOnClickListener(this);
			break;
		case PAYED:
			btn_buy = (Button) findViewById(R.id.btn_buy);
			btn_buy.setVisibility(View.GONE);
			rl_ztq.setVisibility(View.VISIBLE);
			tv_end_time = (TextView) findViewById(R.id.endtime);
			tv_ztq = (TextView) findViewById(R.id.ztq);
			refund_btn = (Button) findViewById(R.id.refund_btn);
			refund_btn.setVisibility(View.VISIBLE);
			refund_btn.setOnClickListener(this);
			break;
		}
		
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		
		tv_tittle = (TextView) findViewById(R.id.tv_tittle);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_prime_price = (TextView) findViewById(R.id.tv_prime_price);
		tv_Qstate = (TextView) findViewById(R.id.state);
		tv_Ostate = (TextView) findViewById(R.id.tv_state);
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		tv_taocan = (TextView) findViewById(R.id.tv_tc);
		tv_ordno = (TextView) findViewById(R.id.tv_ordno);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_all = (TextView) findViewById(R.id.tv_all);
		back = (Button) findViewById(R.id.back);
		detailbtn = (Button) findViewById(R.id.detailbtn);
		rl = (RelativeLayout) findViewById(R.id.rl);
		picView = (ImageView) findViewById(R.id.pic);
		tittle.setText("订单详情");
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(this);
		detailbtn.setOnClickListener(this);
		rl.setOnClickListener(this);
		
		if(TGorTM==TUANGOU){
		orderCursor = db.query("ORDER_TG_LIST", null, "_id=?",
				new String[] { id }, null, null, null);
		}
		if(TGorTM==TEMAI){
			orderCursor = db.query("ORDER_TM_LIST", null, "_id=?",
					new String[] { id }, null, null, null);
		}
		orderCursor.moveToFirst();
		ordno = orderCursor.getString(orderCursor.getColumnIndex("_ordno"));
		payTime = orderCursor.getString(orderCursor
				.getColumnIndex("_dateandtime"));
		ordState = orderCursor.getString(orderCursor.getColumnIndex("_ordzt"));
		System.out.println(ordState);
		if(TGorTM==TUANGOU){}else if((TGorTM==TEMAI&&ordState.equals("7")||ordState.equals("3")||ordState.equals("6")||ordState.equals("8")||ordState.equals("9"))){
			refund_btn.setVisibility(View.GONE);
		}else if(TGorTM==TEMAI&&TextUtils.equals(ordState, "5")){
			refund_btn.setText("取消退款");
			istuikuanstate=true;
		}
		productName = orderCursor.getString(orderCursor
				.getColumnIndex("_fcpmc"));
		picUrl = orderCursor.getString(orderCursor.getColumnIndex("_fcppic"));
		sum = orderCursor.getString(orderCursor.getColumnIndex("_ordsl"));
		allPrice = orderCursor.getString(orderCursor.getColumnIndex("_ordje"));
		mobile = orderCursor.getString(orderCursor.getColumnIndex("_tel"));
		productId = orderCursor
				.getString(orderCursor.getColumnIndex("_fcpmid"));
		shopId = orderCursor.getString(orderCursor.getColumnIndex("_shopid"));
		if (TGorTM == TUANGOU) {
			productCursor = db.query("PRODUCT_LIST", null, "_id=?",
					new String[] { productId }, null, null, null);
		}else{
			productCursor=db.query("TEMAI_LIST_VERSON", null, "_id=?",
					new String[] { productId }, null, null, null);
			if (productCursor.getCount()<1) {
				loadSellProduct(productId);
			}
		}
		

		productCursor.moveToFirst();
		if (productCursor.getCount() == 1) {
			if (TGorTM == TUANGOU) {
				float danjia = Float.parseFloat(allPrice)
						/ Float.parseFloat(sum);
				primePrice = productCursor.getString(productCursor.getColumnIndex("_dj0"));
				nowPrice = danjia + "";
				ImageUtilities.loadBitMap(picUrl, picView);
				tv_price.setText(nowPrice);
			}else {
				loadTmProdctmsg();
			}
		} else {
			if (TGorTM == TUANGOU) {
				Toast.makeText(this, "产品已下架！", Toast.LENGTH_SHORT).show();
				refund_btn.setClickable(false);
				refund_btn.setVisibility(View.GONE);
				detailbtn.setVisibility(View.GONE);
			}
		}
		if (activityTag==PAYED) {
			payMsgShow();
		} else {
			tv_Ostate.setText("待支付");
		}
		tv_Ostate.setText(state);
		
		tv_tittle.setText(productName);
		tv_prime_price.setText(primePrice);
		tv_detail.setText(content);
		tv_taocan.setText(taocan);
		tv_ordno.setText(ordno);
		tv_time.setText(payTime);
		tv_mobile.setText(mobile.substring(0, 4)+"..."+mobile.subSequence(8, 11));
		tv_sum.setText(sum);
		tv_all.setText(allPrice);

	}

	private void payMsgShow() {
		ztqCursor = db.query("ZTQ_LIST", null, "_ordno=?",
				new String[] { ordno }, null, null, null);
		ztqCursor.moveToFirst();
		if (ztqCursor.getCount() == 0) {
			loadZTQByOrd();
		} else {
			ztq = "";
			ztqState = "";
			for (int i = 0; i < ztqCursor.getCount(); i++) {
				edate = ztqCursor.getString(ztqCursor
						.getColumnIndex("_edate"));
				ztq = ztq
						+ ztqCursor.getString(ztqCursor
								.getColumnIndex("_qno")) + "\n";
				String qzt = ztqCursor.getString(ztqCursor
						.getColumnIndex("_qzt"));
				switch (qzt) {
				case "-1":
					ztqState = ztqState + "已退款" + "\n";
					break;
				case "0":
					ztqState = ztqState + "未使用" + "\n";
					break;
				case "1":
					ztqState = ztqState + "已使用" + "\n";
					break;

				}
				ztqCursor.moveToNext();
			}

			tv_end_time.setText(edate);
			tv_ztq.setText(ztq);
			tv_Qstate.setText(ztqState);
		}
	}
	private void loadTmProdctmsg() {
		if (productCursor.moveToFirst()) {
			primePrice = productCursor.getString(productCursor
					.getColumnIndex("_dj0"));
			nowPrice = productCursor.getString(productCursor
					.getColumnIndex("_tmdj"));
			tv_price.setText(nowPrice);
			tv_prime_price.setText(primePrice);
			rl_ztq.setVisibility(View.GONE);
			t2.setVisibility(View.GONE);
			tv_taocan.setVisibility(View.GONE);
			ImageUtilities.loadBitMap(picUrl, picView);
		}
	}

	private void loadZTQByOrd() {
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync loadTask = new NetAsync(D.API_GETQBYORDNO, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, ZTQ>>() {
				}.getType();
				Map<String, ZTQ> ztqMap = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(ZTQ.class);
				for (String temp : ztqMap.keySet()) {

					ZTQ ztq = ztqMap.get(temp);
					ztq.save();

				}

				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno", ordno));

			}
		};
		loadTask.execute();

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.detailbtn:
			if (TGorTM==TUANGOU) {
				Intent intent = new Intent(OrderDetailActivity.this,
						PicAndWordActivity.class);
				intent.putExtra("shop", shopId);
				intent.putExtra("cpid", productId);
				intent.putExtra("productname", productName);
				intent.putExtra("cppic", picUrl);
				intent.putExtra("cpje", nowPrice);
				//TODO 为订单页面准备的数据
				intent.putExtra("cpdj", nowPrice);
				intent.putExtra("pprice", primePrice);
				intent.putExtra("ordno", ordno);
				intent.putExtra("tag", activityTag);
				intent.putExtra("TGorTM", TGorTM);
				intent.putExtra("id", productId);
				intent.putExtra("sum",Integer.parseInt(sum));
				startActivity(intent);
			}else{
				Intent intent2 =new Intent(getApplicationContext(), PhotoWordDtetial.class);
				intent2.putExtra("shopId", shopId);
				intent2.putExtra("productId", productId);
				intent2.putExtra("beforPrice", primePrice);
				intent2.putExtra("nowprice", nowPrice);
				intent2.putExtra("id", id);
				intent2.putExtra("sum",Integer.parseInt(sum));
				startActivity(intent2);
			}
			break;
		case R.id.btn_buy:
			Intent intentPay;
			if (TGorTM==TEMAI) {
				intentPay = new Intent(OrderDetailActivity.this,
						Paymoney.class);
			}else{
				intentPay = new Intent(OrderDetailActivity.this,
						PayActivity.class);
			}
			
			intentPay.putExtra("productname", productName);
			intentPay.putExtra("cpje", allPrice);
			intentPay.putExtra("ordno", ordno);
			intentPay.putExtra("id", productId);
			intentPay.putExtra("sum",Integer.parseInt(sum));
			//intentPay.putExtra("currentstate", TGorTM);
			startActivity(intentPay);
			break;
		case R.id.rl:

			break;
		case R.id.refund_btn:
			//System.out.println(istuikuanstate);
			if(istuikuanstate){
				doCanceltuiKuan();
				break;
			}
			if (ordState.equals("1")||(TGorTM==TEMAI&&!ordState.equals("5")&&!ordState.equals("6")&&!ordState.equals("8")&&!ordState.equals("9"))) {
				Intent apply_intent = new Intent(OrderDetailActivity.this,
						ApplyRefundActivity.class);
				apply_intent.putExtra("ordno", ordno);
				apply_intent.putExtra("cost", nowPrice);
				apply_intent.putExtra("id", id);// "ORDER_LIST"的"_id"
				apply_intent.putExtra("sum", sum);
				apply_intent.putExtra("productName", productName);
				apply_intent.putExtra("currentstate", TGorTM);
				startActivity(apply_intent);
				finish();
			} 
			break;

		default:
			break;
		}

	}

	private void doCanceltuiKuan() {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_CANCEL_ORDREFUND, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno",ordno ));
			}
		};
		task_loadTemai.execute();
		mDialog.show();
		
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		// Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if (mDialog!=null) {
			mDialog.dismiss();
		}
        switch (reqUrl) {
		case D.API_SPECIAL_GETATEMAI:
			productCursor=db.query("TEMAI_LIST_VERSON", null, "_id=?",
					new String[] { productId }, null, null, null);
			loadTmProdctmsg();
			payMsgShow();
			break;
		case D.API_GETQBYORDNO:
			ztqCursor = db.query("ZTQ_LIST", null, "_ordno=?",
					new String[] { ordno }, null, null, null);
			ztqCursor.moveToFirst();
			if (ztqCursor.isAfterLast()) {
				loadZTQByOrd();
			} else {
				ztq = "";
				ztqState = "";
				for (int i = 0; i < ztqCursor.getCount(); i++) {
					edate = ztqCursor.getString(ztqCursor.getColumnIndex("_edate"));
					ztq = ztq
							+ ztqCursor.getString(ztqCursor.getColumnIndex("_qno"))
							+ "\n";
					String qzt = ztqCursor.getString(ztqCursor
							.getColumnIndex("_qzt"));
					switch (qzt) {
					case "-1":
						ztqState = ztqState + "已退款" + "\n";
						break;
					case "0":
						ztqState = ztqState + "未使用" + "\n";
						break;
					case "1":
						ztqState = ztqState + "已使用" + "\n";
						break;
					}
					ztqCursor.moveToNext();
				}
				tv_end_time.setText(edate);
				tv_ztq.setText(ztq);
				tv_Qstate.setText(ztqState);
			}
			break;
		case D.API_SPECIAL_CANCEL_ORDREFUND:
			istuikuanstate=false;
			refund_btn.setVisibility(View.GONE);
			Toast.makeText(this, "您成功取消了退款",1).show();
			
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
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	//加载某一个特卖产品的列表
	private void loadSellProduct(final String productid) {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_GETATEMAI, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2>() {
				}.getType();
				TemaiVerson2 temaiList = JsonUtilities.parseModelByType(
						elData, type);
				Model.save(new TemaiVerson2[]{temaiList});
				return temaiList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid",productid ));
			}
		};
		task_loadTemai.execute();
	}

}
