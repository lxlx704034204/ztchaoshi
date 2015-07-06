package com.iteambuysale.zhongtuan.activity.me;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class MeEvalutionBefore_l extends BaseActivity implements OnClickListener, NetAsyncListener {

	private int[] to;
	private String[] from;
	private ListView listview;
	private final int ENSURE_PACKAGE=9;//确认收货
	private final int APPLY_REFUND=7;//		申请退货
	private int fromstatus;
	private CustomProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.wait_pay_l);
		super.onCreate(savedInstanceState);
		initview();
		initdata();
	}
	private void initview(){
		ImageView iv_finish=(ImageView) findViewById(R.id.iv_finish);
		TextView  tv_center_title=(TextView) findViewById(R.id.tv_center_title);
		tv_center_title.setText("评价晒单");
		listview = (ListView)findViewById(R.id.lv_order_wait_pay);
		iv_finish.setOnClickListener(this);
	}
	private void initdata(){
		CustomProgressDialog.createDialog(this);
		to = new int[] { R.id.iv_img_one, R.id.tv_product_name,
				R.id.tv_product_price, R.id.tv_buynums,R.id.tv_me_eval };
		from = new String[] { "_cppic", "_cpmc", "_oje", "_osl","_ordnox" };
		String orderno = getIntent().getStringExtra("orderno");
		fromstatus = getIntent().getIntExtra("Fromstatus", 0);
		Cursor cursor = DBUtilities.getcpmx(orderno);
		listview.setAdapter(new MyCursorAdapter(this, R.layout.order_detial_adapter_l, cursor, from, to));
		
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
	protected void ensurePackage(final String orderno) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				this);
		dialog.setTitle("提示");
		dialog.setMessage("是否确认收货？");
		dialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
                      ensureGetGoods(orderno);
					}
				});
		dialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
						arg0.dismiss();
					}
				});
		dialog.create().show();
		
	}
	private   class MyCursorAdapter extends SimpleCursorAdapter {

		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);

		}
          
		@Override
		public void setViewText(TextView v, final String text) {
			switch (v.getId()) {
			case R.id.tv_product_price:
				v.setText("￥" + text);
				break;
			case R.id.tv_buynums:
				v.setText("X" + text);
				break;
			case R.id.tv_me_eval:
				if(fromstatus==ENSURE_PACKAGE){
					v.setText("确认收货");
				}else if(fromstatus==APPLY_REFUND){
					v.setText("申请退款");
				}
			    v.setVisibility(View.VISIBLE);
			    v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ensurePackage(text);
						
					}
				});
				break;
			default:
				break;
			}
		}
		
		@Override
		public void setViewImage(ImageView v, String value) {
			ImageUtilities.loadBitMap(value, v);
		}

	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub
		
	}
}
