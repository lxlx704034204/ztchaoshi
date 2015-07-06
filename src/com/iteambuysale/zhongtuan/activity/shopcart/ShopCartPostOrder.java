package com.iteambuysale.zhongtuan.activity.shopcart;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.address.SelectedAddressActivity;
import com.iteambuysale.zhongtuan.activity.specialsale.DiscountCoupon;
import com.iteambuysale.zhongtuan.activity.specialsale.Paymoney;
import com.iteambuysale.zhongtuan.adapter.ShopCartListAdapter;
import com.iteambuysale.zhongtuan.adapter.ShopCartPostAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ShopCart;
import com.iteambuysale.zhongtuan.model.Tuanbi;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopCartPostOrder extends BaseActivity implements
		NetAsyncListener, OnClickListener, OnCheckedChangeListener {
	private ListView listview;
	private LinearLayout ll_seleted_address;
	private LinearLayout ll_detail_address;
	private TextView tv_show_address;
	private TextView tv_show_name_and_tell;
	private String addrid;
	private ArrayList<ShopCart> shopCartList;
	private TextView cart_sum_money;
	private CustomProgressDialog dialog;
	private int listviewheight;
	private ScrollView sv_cart_ordermgs;
	private LinearLayout ll_buttom;
	private String sum;
	private String tmqno;
	private String quanmoney;
	private TextView tv_youhuimoney;
	private TextView tv_tuanbi_pay;
	private float tuanbimoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart_post_order);
		initview();
		initdata();
		
	}

	private void initview() {
		listview = (ListView) findViewById(R.id.lv_cart_list);
		ll_seleted_address = (LinearLayout) findViewById(R.id.ll_seleted_address);
		ll_detail_address = (LinearLayout) findViewById(R.id.ll_detail_address);
		tv_show_address = (TextView) findViewById(R.id.tv_show_add);
		tv_show_name_and_tell = (TextView) findViewById(R.id.tv_show_name_and_tell);
		TextView tv_header_title=(TextView) findViewById(R.id.tv_header_tittle);
		tv_header_title.setText("订单信息");
		cart_sum_money = (TextView) findViewById(R.id.cart_sum_money);
		Button bt_post_order=(Button) findViewById(R.id.bt_post_order);
		sv_cart_ordermgs = (ScrollView) findViewById(R.id.sv_cart_ordermgs);
		ll_buttom=(LinearLayout)findViewById(R.id.ll_buttom);
		LinearLayout ll_youhuiquan = (LinearLayout)findViewById(R.id.ll_youhuiquan);
		tv_youhuimoney=(TextView)findViewById(R.id.tv_youhuimoney);
		CheckBox cb_selet_tuanbi = (CheckBox)findViewById(R.id.cb_selet_tuanbi);
		cb_selet_tuanbi.setOnCheckedChangeListener(this);
		tv_tuanbi_pay = (TextView)findViewById(R.id.tv_tuanbi_pay);
		ll_youhuiquan.setOnClickListener(this);
		bt_post_order.setOnClickListener(this);
	};

	private void initdata() {
		loadUserAddress();
		sv_cart_ordermgs.setPadding(0, 0, 0,measurebuttonheight()+ZhongTuanApp.getInstance().getDensity()*15);
		shopCartList = getIntent().getParcelableArrayListExtra("cartlist");
		sum = getIntent().getStringExtra("sum");
		//Toast.makeText(this, shopCartList.size() + "", 0).show();
		ShopCartPostAdapter adapter = new ShopCartPostAdapter(this,
				shopCartList);
		listview.setAdapter(adapter);
		int[] heights = measurelisviewheigt();
		listviewheight=heights[1]
				* (shopCartList.size() - adapter.getshowshopnamenum())
				+ adapter.getshowshopnamenum() * heights[0];
		listview.getLayoutParams().height = listviewheight;// -heights[1]*(shopCartList.size()-adapter.getshowshopnamenum());
	//	Toast.makeText(
		//		this,
		///		"size1:" + shopCartList.size() + "size2:"
		//				+ adapter.getshowshopnamenum(), 0).show();
		ll_seleted_address.setOnClickListener(this);
		ll_detail_address.setOnClickListener(this);
		cart_sum_money.setText("￥"+sum);
		
	};
    private int measurebuttonheight(){
    	ll_buttom.measure(0, 0);
    	return ll_buttom.getMeasuredHeight();
    }
	private int[] measurelisviewheigt() {
		int[] heights = new int[2];
		View view = View.inflate(this, R.layout.cart_profuct_list_view, null);
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_detial);
		view.measure(0, 0);
		heights[0] = view.getMeasuredHeight();
		heights[1] = rl.getMeasuredHeight();
		//Toast.makeText(this, heights[0] + "," + heights[1], 0).show();
		return heights;
	}

	private void loadUserAddress() {
		NetAsync task_getAddress = new NetAsync(D.API_CPORD_GETADDRESS, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<UserAddress[]>() {
				}.getType();
				UserAddress[] addressList = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(UserAddress.class);
				Model.save(addressList);
				return addressList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}
		};
		task_getAddress.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		Toast.makeText(this, errMsg, 0).show();

	}
    
	Tuanbi tuanbi;
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_CPORD_GETADDRESS:
			UserAddress[] addressList = (UserAddress[]) data;
			showaddressmsg(addressList);
			break;
		case D.API_SPECIAL_CART_ORDER_POST:
			String ordno = (String) data;
			Intent intent =new Intent(this, Paymoney.class);
			intent.putExtra("cpje", calulatemoney());
			intent.putExtra("ordno", ordno);
			startActivity(intent);
			break;
		case D.ORDER_TUANBI_MONEY:
			tuanbi = (Tuanbi) data;
			if(tuanbi.getOrdtbmoney()!=null){
				
			tuanbimoney = Float.parseFloat(tuanbi.getOrdtbmoney()) / 100;
			tv_tuanbi_pay.setText("可用" + tuanbi.getOrdtbmoney() + "团币" + "，抵￥ "
					+ tuanbimoney);
			}
			calulatemoney();
		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub

	}

	private void showaddressmsg(UserAddress[] addressList) {
		UserAddress defaddress = null;
		for (UserAddress userAddress : addressList) {
			String isdef = userAddress.getIsdef();
			if (TextUtils.equals(isdef, "1")) {
				defaddress = userAddress;
				break;
			} else {
				break;
			}
		}
		if (defaddress == null) {
			ll_seleted_address.setVisibility(View.VISIBLE);
			ll_detail_address.setVisibility(View.GONE);
		//setwithoutAddressScrollHeight();
			
		} else {
			ll_seleted_address.setVisibility(View.GONE);
			ll_detail_address.setVisibility(View.VISIBLE);
			tv_show_address.setText(defaddress.getAddress());
			tv_show_name_and_tell.setText(defaddress.getTruename() + " "
					+ defaddress.getTel());
			addrid = defaddress.getUaid();
			//Toast.makeText(this, "addrid:"+addrid, 0).show();
		}

	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_seleted_address:
		case R.id.ll_detail_address:
			intent = new Intent(getBaseContext(),
					SelectedAddressActivity.class);
			intent.putExtra("istemai", true);
			intent.putExtra("request", 2);
			startActivityForResult(intent, 2);
			break;
		case R.id.bt_post_order:
			orderRequest();
			break;
		case R.id.ll_youhuiquan:
			intent = new Intent(this, DiscountCoupon.class);
			intent.putExtra("shopid", buildshopid());
			intent.putExtra("ordje", buildallprice());
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
	}
	private String buildshopid(){
		StringBuilder builder=new StringBuilder();
		builder.append("0 ,");
		for(ShopCart  shopcart:shopCartList ){
			builder.append(shopcart.getShopid()+",");
		}
		
		return builder.substring(0, builder.length()-1);
	}
	private String buildallprice(){
		StringBuilder builder=new StringBuilder();
		builder.append(sum+",");
		for(ShopCart shopcart :shopCartList){
			builder.append(shopcart.getJe()+",");
		}
		return builder.substring(0, builder.length()-1);
	}
	private void orderRequest() {
		

		/*
		 * Cursor getdefmsg = DBUtilities.getdefmsg();
		 * if(getdefmsg.moveToFirst()){ addrid =
		 * getdefmsg.getString(getdefmsg.getColumnIndex("_id")); }
		 */if (TextUtils.isEmpty(addrid)) {
			Toast.makeText(this, "您尚未填写收货地址!", 1).show();
			return;
		}

		dialog = CustomProgressDialog.createDialog(this);
		dialog.show();

		SharedPreferences pre = getSharedPreferences("zhongtuan_preference",
				Context.MODE_PRIVATE);
		final String lato = pre.getString("lat", "");
		final String lngo = pre.getString("lgn", "");
		NetAsync orderTask = new NetAsync(D.API_SPECIAL_CART_ORDER_POST, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> item = JsonUtilities.parseModelByType(
						elData, type);
				String ordno = item.get("ordno");
				return ordno;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("addrid", addrid));// addrid/地址ID
				params.add(new BasicNameValuePair("lngo", lngo));// lngo/经度
				params.add(new BasicNameValuePair("lato", lato));// lato/纬度
				params.add(new BasicNameValuePair("ctid",builderctid() ));// 购物车id；
				params.add(new BasicNameValuePair("ctmess","" ));// 购物车id；
				params.add(new BasicNameValuePair("sendm","" ));// 购物车id；
				params.add(new BasicNameValuePair("fapiao","" ));// 购物车id；
				if(tmqno!=null){
				params.add(new BasicNameValuePair("tmqno", tmqno));// 订单券号
				}
				if(tuanbi!=null){
					params.add(new BasicNameValuePair("tbmoney", tuanbi.getOrdtbmoney()+""));// 团币数量
				}

			}
		};
		orderTask.execute();
	}
   private String builderctid(){
	   StringBuilder ctids=new StringBuilder();
	   for(ShopCart shopcart :shopCartList){
		   ctids.append(shopcart.getCtid());
		   ctids.append(",");
	   }
	   System.out.println( ctids.substring(0, ctids.length()-2).toString());
	   return ctids.substring(0, ctids.length()-1).toString();
   }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 2:
			String name = (String) data.getCharSequenceExtra("name");
			String tel = (String) data.getCharSequenceExtra("tel");
			String address = (String) data.getCharSequenceExtra("address");
			ll_seleted_address.setVisibility(View.GONE);
			ll_detail_address.setVisibility(View.VISIBLE);
			tv_show_address.setText(address);
			tv_show_name_and_tell.setText(name + " " + tel);
			Cursor cursor = DBUtilities.getitemaddressid(name, address, tel);
			if (cursor.moveToFirst()) {
				addrid = cursor.getString(cursor.getColumnIndex("_id"));
			}
			//sethaveAddresscrollHeight();
			//Toast.makeText(this, "addrid:"+addrid, 0).show();
			break;
		case 3:
			//Toast.makeText(this, "onactivityresult()", 0).show();
			tmqno = data.getStringExtra("tmqno");
			quanmoney = data.getStringExtra("quanmoney");
			tv_youhuimoney.setText("￥" + quanmoney);
			calulatemoney();
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			tuanbipay();
		} else {
			tv_tuanbi_pay.setText("可用0团币" + "，抵￥ 0");
			tuanbimoney=0f;
			calulatemoney();
		}

	}
	private void tuanbipay() {
		NetAsync orderTask = new NetAsync(D.ORDER_TUANBI_MONEY, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Tuanbi>() {
				}.getType();
				Tuanbi tuanbi = JsonUtilities.parseModelByType(elData, type);
				return tuanbi;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", buildtmid()));// addrid/地址ID

			}
		};
		orderTask.execute();
	}
	private String buildtmid(){
		StringBuilder builder=new StringBuilder();
		for(ShopCart  shopcart:shopCartList ){
			builder.append(shopcart.getMid()+",");
		}
		
		return builder.substring(0, builder.length()-1);
		
	}
	 private String calulatemoney(){
	    	float quanmoney1;
	    	if(quanmoney==null){
	    		quanmoney1=0f;
	    	}else{
	    		quanmoney1=Float.parseFloat(quanmoney);
	    	}
	    	cart_sum_money.setText("￥"+(Float.parseFloat(sum)-quanmoney1-tuanbimoney)+"");
	    	return (Float.parseFloat(sum)-quanmoney1-tuanbimoney)+"";
	    }
}
