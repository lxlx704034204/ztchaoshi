package com.iteambuysale.zhongtuan.activity.me.address;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

/**
 * @author Administrator
 * 
 */
@SuppressLint("UseSparseArrays")
public class AddressManageActivity extends BaseActivity implements
		OnItemSelectedListener, OnClickListener, NetAsyncListener {
	private TextView tittle;
	private Button back, save;
	private Spinner provinceSp, citySp, districtSp, timeSp;
	private EditText nameEdt, telEdt, addressEdt;
	private String provinceId, cityId, districtId, truename, tel, time,
			address;
	private SimpleCursorAdapter proAdapter, cityAdapter, zoneAdapter;
	private CustomProgressDialog mDialog;
	private Cursor proCursor, cityCursor, zoneCursor;
	private int flag;
	private String id;
	private CheckBox cb_isdef;
	private SQLiteDatabase db2;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (proCursor != null) {
			proCursor.close();
		}
		if (cityCursor != null) {
			cityCursor.close();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_manage);
		findViews();
		initListener();
		initData();

	}

	/**
	 * 找到当前所有控件
	 */
	private void findViews() {
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.setting);
		provinceSp = (Spinner) findViewById(R.id.sp_pro);
		citySp = (Spinner) findViewById(R.id.sp_city);
		districtSp = (Spinner) findViewById(R.id.sp_district);
		nameEdt = (EditText) findViewById(R.id.et_name);
		telEdt = (EditText) findViewById(R.id.et_tel);
		timeSp = (Spinner) findViewById(R.id.sp_time);
		addressEdt = (EditText) findViewById(R.id.et_address_detail);
		cb_isdef = (CheckBox) findViewById(R.id.cb_isdef);
	}

	/**
	 * 设置控件点击事件
	 */
	private void initListener() {
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		timeSp.setOnItemSelectedListener(this);
		provinceSp.setOnItemSelectedListener(this);
		citySp.setOnItemSelectedListener(this);
		districtSp.setOnItemSelectedListener(this);

	}

	/**
	 * 初始化数据库，view的值，adapter
	 */
	private void initData() {
		db2 = ZhongTuanApp.getInstance().getPcdDB();

		back.setBackgroundResource(R.drawable.header_back);
		tittle.setText("地址管理");
		save.setText("保存");
		ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
				R.layout.spinner_view, new String[] { "所有时间", "周六周日及节假日可收货",
						"工作日可收货" });
		timeSp.setAdapter(timeAdapter);
		setProvince(provinceSp, "T_Province", "ProName");
		Intent intent = getIntent();
		time = intent.getStringExtra("time");
		flag = intent.getFlags();
		if (flag == 1) {
			id = intent.getStringExtra("id");
			nameEdt.setText(intent.getStringExtra("name"));
			telEdt.setText(intent.getStringExtra("tel"));
			addressEdt.setText(intent.getStringExtra("addressDetail"));
			timeSp.setSelection(Integer.parseInt(intent.getStringExtra("time")));
			provinceSp.setSelection(Integer.parseInt(intent
					.getStringExtra("province")) - 1);
			// -------------------------------------
			String isdef = intent.getStringExtra("isdef");
			if (TextUtils.equals(isdef, "0")) {
				cb_isdef.setChecked(false);
			} else if (TextUtils.equals(isdef, "1")) {
				cb_isdef.setChecked(true);
			}
			save.setText("修改");
		}

	}

	/**
	 * 设置省
	 * 
	 * @param sp
	 * @param tableName
	 */
	private void setProvince(Spinner sp, String tableName, String column) {
		Cursor cursor = db2
				.query(tableName, null, null, null, null, null, null);
		proAdapter = new SimpleCursorAdapter(this, R.layout.spinner_view,
				cursor, new String[] { column }, new int[] { R.id.sp_text }, 0);
		sp.setAdapter(proAdapter);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long id) {
		switch (arg0.getId()) {
		case R.id.sp_pro:
			provinceId = id + "";
			cityCursor = db2.query("T_City", null, "ProId=?",
					new String[] { provinceId }, null, null, null);
			cityAdapter = new SimpleCursorAdapter(view.getContext(),
					R.layout.spinner_view, cityCursor,
					new String[] { "CityName" }, new int[] { R.id.sp_text }, 0);
			citySp.setAdapter(cityAdapter);
			if (flag == 1) {
				String city = getIntent().getStringExtra("city");
				cityCursor.moveToFirst();
				for (int i = 0; i < cityCursor.getCount(); i++) {
					if (!cityCursor.getString(
							cityCursor.getColumnIndex("CityName")).equals(city)) {
						cityCursor.moveToNext();
						continue;
					}
					citySp.setSelection(i);
					break;
				}
			}
			break;

		case R.id.sp_city:
			cityId = id + "";
			zoneCursor = db2.query("T_Zone", null, "CityId=?",
					new String[] { cityId }, null, null, null);
			zoneAdapter = new SimpleCursorAdapter(view.getContext(),
					R.layout.spinner_view, zoneCursor,
					new String[] { "ZoneName" }, new int[] { R.id.sp_text }, 0);
			districtSp.setAdapter(zoneAdapter);
			String district = getIntent().getStringExtra("district");
			zoneCursor.moveToFirst();
			for (int i = 0; i < zoneCursor.getCount(); i++) {
				if (!zoneCursor
						.getString(zoneCursor.getColumnIndex("ZoneName"))
						.equals(district)) {
					zoneCursor.moveToNext();
					continue;
				}
				districtSp.setSelection(i);
				break;
			}
			break;
		case R.id.sp_district:
			districtId = id + "";
			// 之前未设置区的防止奔溃
			if (districtId.equals("0")) {
				return;
			}
			district = cityAdapter.getItem(arg2).toString();
			break;

		case R.id.sp_time:
			time = arg2 + "";
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.setting:
			save(flag);
			break;
		}

	}

	private void save(final int flag) {
		truename = nameEdt.getText().toString();
		tel = telEdt.getText().toString();
		if (tel.length() != 11) {
			Toast.makeText(this, "电话号码长度不对！", Toast.LENGTH_SHORT).show();
			return;
		}
		address = addressEdt.getText().toString();
		final String isdef = cb_isdef.isChecked() ? "1" : "0";

		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync addTask = new NetAsync(flag == 0 ? D.API_CPORD_NEWADDRESS
				: D.API_CPORD_UPDATEADDRESSS, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				if (flag == 1) {
					params.add(new BasicNameValuePair("uaid", id));
				}
				params.add(new BasicNameValuePair("truename", truename));
				params.add(new BasicNameValuePair("tel", tel));
				params.add(new BasicNameValuePair("address", address));
				params.add(new BasicNameValuePair("province", provinceId));
				params.add(new BasicNameValuePair("city", cityId));
				params.add(new BasicNameValuePair("carea", districtId));
				params.add(new BasicNameValuePair("zipcode", ""));
				params.add(new BasicNameValuePair("sendid", time));
				params.add(new BasicNameValuePair("isdef", isdef));

			}
		};
		addTask.execute();

	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mDialog.dismiss();
		switch (reqUrl) {
		case D.API_CPORD_NEWADDRESS:
			Toast.makeText(this, "添加新地址成功！", Toast.LENGTH_SHORT).show();
			break;
		case D.API_CPORD_UPDATEADDRESSS:
			Toast.makeText(this, "修改地址成功！", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		finish();

	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		Toast.makeText(this, "超时！", Toast.LENGTH_SHORT).show();
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
