package com.iteambuysale.zhongtuan.activity.me.unpay;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.MeEvalutionBefore_l;
import com.iteambuysale.zhongtuan.activity.specialsale.Paymoney;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class OrderDetailActivity_l extends BaseActivity implements OnClickListener, NetAsyncListener {
	private ListView lv_order_detial;
	private TextView tv_order_status;
	private TextView tv_name_tell;
	private TextView tv_address;
	private TextView tv_paytype;
	private TextView tv_pay_time;
	private int[] to;
	private String[] from;
	private TextView tv_order_num;
	private ImageView iv_finish;
	private String orderno;
	private String ordje;
	private CustomProgressDialog mProgressDialog;
	private int orderstatus;
	private final int UN_PAY=0;
	private final int PAYED=1;
	private final int WAIT_EVAL=2;
	private final int SALE_AFTER=3;
	/*=========================================*/
	private final int GO_EVALUTE=6;
	private final int CHECKED_LOGISTIC=10;
	
	private Button bt_go_pay;
	private Button bt_concel_order;
	private String logistics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detial_l);
		initeview();
		initdata();

	}

	private void initdata() {
		to = new int[] { R.id.iv_img_one, R.id.tv_product_name,
				R.id.tv_product_price, R.id.tv_buynums };
		from = new String[] { "_cppic", "_cpmc", "_oje", "_osl" };
		orderno = getIntent().getStringExtra("orderno");
		ordje = getIntent().getStringExtra("ordje");
		logistics = getIntent().getStringExtra("logistics");
		orderstatus=getIntent().getIntExtra("orderstatus", -1);
		Cursor cursor = DBUtilities.getOrderByoderNumber(orderno);
		Cursor cr = DBUtilities.getcpmx(orderno+"%");
		lv_order_detial.setAdapter(new MyCursorAdapter(this,
				R.layout.order_detial_adapter_l, cr, from, to));
		String ordStatus = "0";
		String address = null;
		String productUser = null;
		String tell = null;
		String paytype = null;
		String paytime = null;
		if (cursor.moveToFirst()) {
			ordStatus = getstr(cursor, "_ordzt");
			productUser = getstr(cursor, "_truename");
			address = getstr(cursor, "_address");
			tell = getstr(cursor, "_tel");
			paytype = getstr(cursor, "_paym");
			paytime = getstr(cursor, "_dateandtime");
		}
		setOrderStatus(ordStatus);
		tv_name_tell.setText(productUser + "    " + tell);
		tv_address.setText(address);
		tv_paytype.setText("支付方式 ：" + getpaytype(paytype));
		tv_pay_time.setText("支付时间： " + paytime);
		tv_order_num.setText(orderno);
        iv_finish.setOnClickListener(this);
        switch (orderstatus) {
		case UN_PAY:
			bt_go_pay.setText("去支付");
			bt_concel_order.setText("取消订单");
			break;
		case PAYED:
			bt_go_pay.setText("申请退款");
			bt_concel_order.setText("查看物流");
			break;
		case WAIT_EVAL:
			bt_go_pay.setText("去评价");
			bt_concel_order.setVisibility(View.GONE);
			break;
		case SALE_AFTER:
			bt_go_pay.setText("申请售后");
			bt_concel_order.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void setOrderStatus(String ordStatus) {
		switch (ordStatus) {
		case "0":
			tv_order_status.setText("待支付");
			break;
		case "1":
			tv_order_status.setText("已支付，代发货");
			break;
		case "2":
			tv_order_status.setText("已收货，待评价");
			break;
		case "3":
			tv_order_status.setText("已评价");
			break;
		case "4":
			tv_order_status.setText("已发货，待收货");
			break;
		case "5":
			tv_order_status.setText("申请退款");
			break;
		case "6":
			tv_order_status.setText("同意退款");
			break;
		case "7":
			tv_order_status.setText("拒绝退款");
			break;
		case "8":
			tv_order_status.setText("亏款成功");
			break;
		case "9":
			tv_order_status.setText("退款已处理");
			break;

		default:
			break;
		}
	}

	private void initeview() {
		lv_order_detial = (ListView) findViewById(R.id.lv_order_detial);
		lv_order_detial.addHeaderView(View.inflate(this,
				R.layout.order_detial_header, null));
		lv_order_detial.addFooterView(View.inflate(this,
				R.layout.order_detial_footer, null));
		tv_order_status = (TextView) findViewById(R.id.tv_order_status);
		tv_name_tell = (TextView) findViewById(R.id.tv_name_tell);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_paytype = (TextView) findViewById(R.id.tv_paytype);
		tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
		tv_order_num = (TextView) findViewById(R.id.tv_order_num);
		iv_finish = (ImageView) findViewById(R.id.iv_finish);
		bt_go_pay = (Button) findViewById(R.id.bt_go_pay);
		bt_concel_order = (Button) findViewById(R.id.bt_concel_order);
		bt_go_pay.setOnClickListener(this);
		bt_concel_order.setOnClickListener(this);
		mProgressDialog=CustomProgressDialog.createDialog(this);
		
		
		
	}

	private String getstr(Cursor c, String name) {
		return c.getString(c.getColumnIndex(name));
	}

	private   class MyCursorAdapter extends SimpleCursorAdapter {

		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);

		}

		@Override
		public void setViewText(TextView v, String text) {
			switch (v.getId()) {
			case R.id.tv_product_price:
				v.setText("￥" + text);
				break;
			case R.id.tv_buynums:
				v.setText("X" + text);
			default:

				// super.setViewText(v, text);
				break;
			}
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			ImageUtilities.loadBitMap(value, v);
		}

	}

	private String getpaytype(String paytype) {
		switch (paytype) {
		case D.PAY_BY_ALI:
			return "支付宝支付";
		case D.PAY_BY_YINLIAN:
			return "支付宝支付";

		case D.PAY_BY_WECHAT:
			return "微信支付";
			
		default:
			return "未支付";
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_finish:
			finish();
			break;
		case R.id.bt_concel_order://左边的按钮
			if (orderstatus==UN_PAY) {
				
				netdeleteOrder(orderno);
			}else if(orderstatus==PAYED){//查看物流
				/*String url = "http://app.teambuy.com.cn/webc/m/tmlog/id/"
						+ logistics;
				Uri uri = Uri.parse(url);
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.setData(uri);
				startActivity(intent);*/
				//loadordermx( orderno);
				Intent intent =new Intent(this,MeEvalutionBefore_l.class);
				intent.putExtra("orderno",orderno);
				intent.putExtra("orderstatus",PAYED);
				intent.putExtra("Fromstatus",CHECKED_LOGISTIC);
				startActivity(intent);
				/*orderno = getIntent().getStringExtra("orderno");
				orderstatus = getIntent().getIntExtra("orderstatus", -1);//intent.putExtra("orderstatus",orderstatus);
				fromstatus = getIntent().getIntExtra("Fromstatus", 0);*/
			}else if(orderstatus==WAIT_EVAL){
				
			}else if(orderstatus==SALE_AFTER){
				Toast.makeText(this, "售后功能暂时未完成", 0).show();
			}
			break;
		case R.id.bt_go_pay://右边的按钮
			if(orderstatus==UN_PAY){
			Intent intent =new Intent(this, Paymoney.class);
			intent.putExtra("cpje", ordje);
			intent.putExtra("ordno", orderno);
			startActivity(intent);
			}else if(orderstatus==PAYED){//申请退款
				Intent intent =new Intent(this, MeEvalutionBefore_l.class);
				intent.putExtra("orderno", orderno);
				intent.putExtra("Fromstatus", 7);
				startActivity(intent);
			}else if(orderstatus==SALE_AFTER){
				Toast.makeText(this, "售后服务暂时未完成", 0).show();
			}else if(orderstatus==WAIT_EVAL){
				Intent intent=new Intent(this ,MeEvalutionBefore_l.class);
				intent.putExtra("orderno", orderno);
				intent.putExtra("Fromstatus", GO_EVALUTE);
				startActivity(intent);
			}
			break;
			
		default:
			break;
		}
		
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

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_SPECIAL_ORDRSCANCEL:
			Toast.makeText(this, "订单已取消", 0).show();
			finish();
			break;
			case D.API_MY_TMORDERMX:
				break;
		default:
			break;
		}
	}

	@Override
	public void onTokenTimeout() {
		if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		
	}
}
