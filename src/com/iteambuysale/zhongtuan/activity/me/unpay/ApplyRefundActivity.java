package com.iteambuysale.zhongtuan.activity.me.unpay;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.ZTQSCAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class ApplyRefundActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, NetAsyncListener {
	private TextView tittle, refundTv, cashTv;
	CheckBox c1, c2, c3, c4, c5, c6, c7, c8;
	Button back, applyBtn;
	EditText suggest;
	String ordno;//ordid;
	float price = 0, per_price = 0;
	ListView listView;
	SQLiteDatabase db;
	ArrayList<String> qidList = new ArrayList<String>();
	ArrayList<String> msgList = new ArrayList<String>();
	String reasonMsg, qidMsg;
	CustomProgressDialog mDialog;
	private int currentstate;
	private String sum;
	private String productName;
	private TextView refund_detail;
	private Cursor cursor;
	private String onox;

	@Override
	protected void onDestroy() {
		cursor.close();
		super.onDestroy();
		ImageUtilities.removeBitmaps();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_refund);
		Intent inetent = getIntent();
		currentstate = inetent.getIntExtra("currentstate", -1);//0为团购，1为特卖
		ordno = inetent.getStringExtra("ordno");
		onox =inetent.getStringExtra("onox");
		sum = inetent.getStringExtra("sum");
		productName = inetent.getStringExtra("productName");
		String cost=inetent.getStringExtra("cost");
		if(cost==null){
			cost="0.0";
		}
		per_price = Float.parseFloat(cost);
		//ordid = inetent.getStringExtra("id");// "ORDER_LIST"中的_id
		listView = (ListView) findViewById(R.id.lv_ztq);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		refundTv = (TextView) findViewById(R.id.refund_money);
		cashTv = (TextView) findViewById(R.id.cash);
		back = (Button) findViewById(R.id.back);
		suggest = (EditText) findViewById(R.id.et_suggest);
		refund_detail = (TextView) findViewById(R.id.refund_detail);
		applyBtn = (Button) findViewById(R.id.applybtn);
		c1 = (CheckBox) findViewById(R.id.check1);
		c2 = (CheckBox) findViewById(R.id.check2);
		c3 = (CheckBox) findViewById(R.id.check3);
		c4 = (CheckBox) findViewById(R.id.check4);
		c5 = (CheckBox) findViewById(R.id.check5);
		c6 = (CheckBox) findViewById(R.id.check6);
		c7 = (CheckBox) findViewById(R.id.check7);
		c8 = (CheckBox) findViewById(R.id.check8);
		suggest = (EditText) findViewById(R.id.et_suggest);

		tittle.setText("申请退款");
		float sumprice = per_price * Integer.parseInt(sum);
		refundTv.setText(sumprice + "");
		cashTv.setText(sumprice + "");
		refund_detail.setText("退款内容：\n" + productName);
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(this);
		applyBtn.setOnClickListener(this);
		c1.setOnCheckedChangeListener(this);
		c2.setOnCheckedChangeListener(this);
		c3.setOnCheckedChangeListener(this);
		c4.setOnCheckedChangeListener(this);
		c5.setOnCheckedChangeListener(this);
		c6.setOnCheckedChangeListener(this);
		c7.setOnCheckedChangeListener(this);
		c8.setOnCheckedChangeListener(this);

		db = ZhongTuanApp.getInstance().getRDB();
		cursor = db.query("ZTQ_LIST", null, "_ordno = ?",
				new String[] { ordno }, null, null, null);
		ZTQSCAdapter adapter = new ZTQSCAdapter(this, R.layout.listitem_ztq,
				cursor, new String[] { "_qno" }, new int[] { R.id.tv_ztq }, 0);
		listView.setAdapter(adapter);
		setListViewHeight(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				if (qidList.contains(id + "")) {
					checkBox.setChecked(false);
					qidList.remove("" + id);
					price = (float) Math.round((price - per_price) * 100) / 100;
					refundTv.setText(price + "");
					cashTv.setText(price + "");
				} else {
					checkBox.setChecked(true);
					qidList.add("" + id);
					price = (float) Math.round((price + per_price) * 100) / 100;
					refundTv.setText(price + "");
					cashTv.setText(price + "");
				}

			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		case R.id.applybtn:
			//if (currentstate == 1) {
				SpecialSaleOrderback(D.API_SPECIAL_ORDREFUND);
		//	} else {
				//packetMsgAndApply(D.API_CPORD_ORDER_REFUND);
			//}
           
			break;
		}

	}
   
	private void SpecialSaleOrderback(String url) {

		reasonMsg = new String();

		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync refundTask = new NetAsync(url, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno", ordno));
				params.add(new BasicNameValuePair("mess", reasonMsg));
				params.add(new BasicNameValuePair("onox", onox));

			}
		};
		refundTask.execute();

	}

	/*private void SpecialSaleOrderback(String url) {
		reasonMsg = new String();
		qidMsg = new String();
		addMsgFromEdt();
		if (msgList.isEmpty() || qidList.isEmpty()) {
			Toast.makeText(this, "亲，您还没填写退款原因！", Toast.LENGTH_SHORT).show();
			return;
		}
		for (String temp : msgList) {
			reasonMsg = reasonMsg + temp + "|";
		}
		for (String temp : qidList) {
			qidMsg = qidMsg + temp + ",";
		}
		reasonMsg = reasonMsg.substring(0, reasonMsg.length() - 1);
		qidMsg = qidMsg.substring(0, qidMsg.length() - 1);
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync refundTask = new NetAsync(url, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordno", ordno));
				params.add(new BasicNameValuePair("mess", reasonMsg));
				params.add(new BasicNameValuePair("onox", onox));

			}
		};
		refundTask.execute();

	}*/

	private void addMsgFromEdt() {
		String edtMsg = suggest.getText().toString();
		if (!edtMsg.equals("")) {
			msgList.add(edtMsg);
		}
	}

	public void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);
	}

	@Override
	public void onCheckedChanged(CompoundButton checkbox, boolean check) {
		switch (checkbox.getId()) {
		case R.id.check1:
			if (check) {
				msgList.add(getResources().getString(R.string.c1));
			} else {
				msgList.remove(getResources().getString(R.string.c1));
			}
			break;
		case R.id.check2:
			if (check) {
				msgList.add(getResources().getString(R.string.c2));
			} else {
				msgList.remove(getResources().getString(R.string.c2));
			}
			break;
		case R.id.check3:
			if (check) {
				msgList.add(getResources().getString(R.string.c3));
			} else {
				msgList.remove(getResources().getString(R.string.c3));
			}

			break;
		case R.id.check4:
			if (check) {
				msgList.add(getResources().getString(R.string.c4));
			} else {
				msgList.remove(getResources().getString(R.string.c4));
			}
			break;
		case R.id.check5:
			if (check) {
				msgList.add(getResources().getString(R.string.c5));
			} else {
				msgList.remove(getResources().getString(R.string.c5));
			}
			break;
		case R.id.check6:
			if (check) {
				msgList.add(getResources().getString(R.string.c6));
			} else {
				msgList.remove(getResources().getString(R.string.c6));
			}
			break;
		case R.id.check7:
			if (check) {
				msgList.add(getResources().getString(R.string.c7));
			} else {
				msgList.remove(getResources().getString(R.string.c7));
			}
			break;
		case R.id.check8:
			if (check) {
				msgList.add(getResources().getString(R.string.c8));
			} else {
				msgList.remove(getResources().getString(R.string.c8));
			}
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
		mDialog.dismiss();
		Intent intent = new Intent(ApplyRefundActivity.this,
				ApplySuccessActivity.class);
		intent.putExtra("ordno", ordno);
		intent.putExtra("qid", qidMsg);
		intent.putExtra("price", price + "");
		intent.putExtra("currentstate", currentstate);
		startActivity(intent);
		finish();

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
	

}
