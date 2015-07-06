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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.af;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.WaitPayAdapter_l;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.me.OderDeleteListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class UnpayActivity_l extends BaseActivity implements OnClickListener, NetAsyncListener, OderDeleteListener {
	private ListView lv_order_wait_pay;
	private int orderStatus;
	private final int UN_PAY=0;
	private final int PAYED=1;
	private final int WAIT_EVAL=2;
	private final int SALE_AFTER=3;
	private final int ALL_ORDER=4;
	private WaitPayAdapter_l adapter;
	CustomProgressDialog mProgressDialog;
	private Map<String, OrderTM> orderMap;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait_pay_l);
		mProgressDialog=CustomProgressDialog.createDialog(this);
		orderStatus= getIntent().getIntExtra("orderStatus", -1);
		
		
		
		}
     @Override
    protected void onStart() {
    	super.onStart();
    	initview();
		initdata();
    }
     private void initview() {
    	 ImageView iv_login_finish=(ImageView) findViewById(R.id.iv_finish);
    	 iv_login_finish.setOnClickListener(this);
 		 TextView tv_header_tittle=(TextView)findViewById(R.id.tv_center_title);
 		 if(orderStatus==0){
 			 
 			 tv_header_tittle.setText("待支付订单");
 		 }else if(orderStatus==1){
 			 tv_header_tittle.setText("待收货订单");
 		 }else if(orderStatus==WAIT_EVAL){
 			 tv_header_tittle.setText("待评价订单");
 		 }else if(orderStatus==SALE_AFTER){
 			 tv_header_tittle.setText("退换/售后订单");
 		 }else if(orderStatus==ALL_ORDER){
 			 tv_header_tittle.setText("全部订单");
 		 }
 		 lv_order_wait_pay = (ListView) findViewById(R.id.lv_order_wait_pay);
 		// lv_order_wait_pay.setOnItemClickListener(this);
 		
 	}
	private void initdata() {
		db = ZhongTuanApp.getInstance().getRDB();
		if(orderStatus==UN_PAY){
			loadWaitPayOrder("0");
		}else if(orderStatus==PAYED){
			loadWaitPayOrder("1,4");
		}else if(orderStatus==WAIT_EVAL){
			loadWaitPayOrder("2");
		}else if(orderStatus==SALE_AFTER){
			loadWaitPayOrder("1,2,4,5,6");
		}else if(orderStatus==ALL_ORDER){
			loadWaitPayOrder("0,1,2,3,4,5,6");
		}
		
	/*	Cursor cur=DBUtilities.getUnpayOrder();
		WaitPayAdapter_l adapter=new WaitPayAdapter_l(this, R.layout.unpay_adapter_l, cur, from, to);
		lv_order_wait_pay.setAdapter(adapter);*/
		
	}
	private void  loadWaitPayOrder(final String orderStatus){
		mProgressDialog.show(); 
		 NetAsync waitpayAsync=new NetAsync(D.API_MY_GETTMORDER,this) {
			

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, OrderTM>>() {
				}.getType();
				Map<String, OrderTM> orderMap = JsonUtilities
						.parseModelByType(elData, type);
				db.beginTransaction();
				for (OrderTM o : orderMap.values()) {
					o.save(db);
					OrderDetailsTM[] odList = o.getCpmx();
					Model.save(odList, db);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				return orderMap;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordzt", orderStatus));
			}
		};
		waitpayAsync.execute();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_finish:
			finish();
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		if(mProgressDialog!=null&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_MY_GETTMORDER:
			if(orderMap!=null){
				orderMap.clear();
				adapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
		Toast.makeText(this, errMsg, 0).show();
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if(mProgressDialog!=null&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_MY_GETTMORDER:
			orderMap = (Map<String, OrderTM>) data;
				 adapter = new WaitPayAdapter_l(this, orderMap,this,orderStatus);
				 lv_order_wait_pay.setAdapter(adapter);
				break;
		case D.API_SPECIAL_ORDRSCANCEL:
			if(orderStatus==UN_PAY){
				loadWaitPayOrder("0");
			}else if(orderStatus==PAYED){
				loadWaitPayOrder("1,4");
			}else if(orderStatus==WAIT_EVAL){
				loadWaitPayOrder("2");
			}else if(orderStatus==SALE_AFTER){
				
			}
			break;
		case D.API_SPECIAL_ORDER_ENSURE_GOODS:
			Intent intent = new Intent(this, UnpayActivity_l.class);//进入到待评价页面
			intent.putExtra("orderStatus", 2);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(this,true);
		
	}
	@Override
	public void deleteOrder(String orderno) {
		netdeleteOrder(orderno);
		
	}
	private void netdeleteOrder( final String orderno){
		mProgressDialog.show();
		   NetAsync deleteTask = new NetAsync(D.API_SPECIAL_ORDRSCANCEL, this) {

				@Override
				public Object processDataInBackground(JsonElement elData) {
					return null;
				}

				@Override
				public void beforeRequestInBackground(List<NameValuePair> params) {
					
					params.add(new BasicNameValuePair("ordnos", orderno));
				}
			};
			deleteTask.execute();
	   }
	
	/**
	 * 确认收货
	 * @param orderno
	 */
	private void ensureGetGoods(final String orderno) {
		mProgressDialog.show();
		NetAsync ensureorder = new NetAsync(
				D.API_SPECIAL_ORDER_ENSURE_GOODS, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno", orderno));
			}
		};
		ensureorder.execute();
	}
	@Override
	public void ensureorder(String orderno) {
		ensureGetGoods(orderno);
		
	}

	
}
