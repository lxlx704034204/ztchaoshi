package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.DiscountCouponAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.DiscountQuan;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class DiscountCoupon extends BaseActivity implements NetAsyncListener, OnClickListener, OnItemClickListener {
	private String shopid;
	private String ordje;
	private ListView lv_discount_count;
	private DiscountQuan[] quans;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_coupon);
		shopid=getIntent().getStringExtra("shopid");
		ordje=getIntent().getStringExtra("ordje");
		lv_discount_count = (ListView) findViewById(R.id.lv_discount_coupon);
		loadtemaiQuan();
		initview();
		initdata();
	}
	private void initdata() {
		lv_discount_count.setOnItemClickListener(this);
		
	}
	private void initview(){
		Button back=(Button) findViewById(R.id.back);
		back.setBackgroundResource(R.drawable.header_back);
		TextView tv_header_tittle=(TextView) findViewById(R.id.tv_header_tittle);
		tv_header_tittle.setText("优惠券");
		Button setting=(Button) findViewById(R.id.setting);
		back.setOnClickListener(this);
		
	}
	private void loadtemaiQuan(){
		NetAsync quanAsync=new NetAsync(D.GET_TEMAI_QUAN,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type=new TypeToken<DiscountQuan[]>(){}.getType();
				DiscountQuan[] quan=JsonUtilities.parseModelByType(elData, type);
				return quan;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("shopid", shopid));
				params.add(new BasicNameValuePair("ordje", ordje));
				
			}
		};
		quanAsync.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		Toast.makeText(this, errMsg, 0).show();
		
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.GET_TEMAI_QUAN:
			quans = (DiscountQuan[]) data;
			lv_discount_count.setAdapter(new DiscountCouponAdapter(quans,this));
			break;

		default:
			break;
		}
	}
	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent =new Intent(this, SpecialSaleBuyAtOnce.class);
		intent.putExtra("tmqno", quans[position].getTmqno());
		intent.putExtra("quanmoney", quans[position].getTmqje());
		  setResult(3, intent);
		  finish();
		
	}
}
