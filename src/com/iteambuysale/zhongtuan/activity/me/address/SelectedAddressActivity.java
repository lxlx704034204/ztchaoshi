package com.iteambuysale.zhongtuan.activity.me.address;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.shopcart.ShopCartPostOrder;
import com.iteambuysale.zhongtuan.activity.specialsale.SpecialSaleBuyAtOnce;
import com.iteambuysale.zhongtuan.adapter.AddressListAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SelectedAddressActivity extends BaseActivity implements
		NetAsyncListener {
	Button addBtn, back;
	ListView addressLv;
	TextView tittle;
	CustomProgressDialog mDialog;
	String[] from = { "_truename", "_tel", "_province", "_city", "_carea",
			"_address", "_sendid" ,"_isdef"};
	int[] to = { R.id.tv_name, R.id.tv_tel, R.id.tv_address_pro,
			R.id.tv_address_city, R.id.tv_address_district,
			R.id.tv_address_detail, R.id.tv_time,R.id.iv_isdefult_address };
	private boolean isfromme;
	private Cursor cursor;
	private SQLiteDatabase db;
	private AddressListAdapter adapter;
	private int requestcode;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (cursor!=null) {
			cursor.close();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_address);
		loadUserAddress();
		isfromme = getIntent().getBooleanExtra("isfromme", false);
		requestcode=getIntent().getIntExtra("request", -1);
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		addBtn = (Button) findViewById(R.id.addBtn);
		back = (Button) findViewById(R.id.back);
		addressLv = (ListView) findViewById(R.id.lv_address);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		addressLv = (ListView) findViewById(R.id.lv_address);
		back.setBackgroundResource(R.drawable.header_back);
		tittle.setText("选择收货地址");
		db = ZhongTuanApp.getInstance().getRDB();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SelectedAddressActivity.this,
						AddressManageActivity.class);
				intent.setFlags(0);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		loadUserAddress();
	}
	public void loadUserAddress() {
		NetAsync task_getAddress = new NetAsync(D.API_CPORD_GETADDRESS, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<UserAddress[]>() {
				}.getType();
				UserAddress[] addressList = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(UserAddress.class);
				Model.save(addressList);
				return null;
			}
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}
		};
		task_getAddress.execute();
	}

	public void changeAddressmessages(final String isdef, View view, long id) {
		Intent intent = new Intent(SelectedAddressActivity.this,
				AddressManageActivity.class);
		String uid = id + "";
		String name = ((TextView) view.findViewById(R.id.tv_name)).getText()
				.toString();
		String tel = ((TextView) view.findViewById(R.id.tv_tel)).getText()
				.toString();
		//省份后缀多一个-分割，要去除分隔符
		String province = ((TextView) view.findViewById(R.id.tv_address_pro))
				.getTag().toString();
		String city = ((TextView) view.findViewById(R.id.tv_address_city))
				.getText().toString().split("-")[0];
		String district=((TextView) view.findViewById(R.id.tv_address_district))
				.getText().toString();
		String addressDetail = ((TextView) view
				.findViewById(R.id.tv_address_detail)).getText().toString();
		String time=null ;
		try {
			time= ((TextView) view.findViewById(R.id.tv_time)).getTag()
					.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent.putExtra("id", uid);
		intent.putExtra("name", name);
		intent.putExtra("tel", tel);
		intent.putExtra("province", province);
		intent.putExtra("city", city);
		intent.putExtra("district", district);
		intent.putExtra("addressDetail", addressDetail);
		intent.putExtra("time", time);
		intent.putExtra("isdef", isdef);
		intent.addFlags(1);
		startActivity(intent);
	}

	public void deleteAddressmsg(final String id) {
		NetAsync delete = new NetAsync(D.API_SPECIAL_DELUADDR, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return id;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("uaid", id));

			}
		};
		delete.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		Toast.makeText(getApplication(), errMsg,Toast.LENGTH_SHORT).show();
		mDialog.dismiss();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_CPORD_GETADDRESS:
			mDialog.dismiss();
			setListView();
			break;
		case D.API_SPECIAL_DELUADDR:
			db.execSQL("delete from ADDRESS_LIST where _id=?",
					new String[] { (String) data });
			cursor = db.query("ADDRESS_LIST", null, null, null, null, null,
					"_isdef desc");
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
			break;

		}

	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
		finish();
	}

	public void setListView() {

		cursor = db.query("ADDRESS_LIST", null, null, null, null, null,
				"_id desc");
		cursor.moveToFirst();
		adapter = new AddressListAdapter(this, R.layout.listitem_address,
				cursor, from, to, 0);
		addressLv.setAdapter(adapter);
		addressLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				cursor = db.query("ADDRESS_LIST", null, new String("_id=?"),
						new String[] { id + "" }, null, null, "_id desc");
				cursor.moveToFirst();
				final String isdef = cursor.getString(cursor
						.getColumnIndex("_isdef"));
				// 弹出单选框
				if (isfromme) {
					showDeleteaddress(id + "");
				} else {
					showdialog(isdef, view, id);
				}
				// changeAddressmessages(isdef, view, id);
				return true;
			}
		});
		addressLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long id) {
				if (isfromme) {
					cursor = db.query("ADDRESS_LIST", null,
							new String("_id=?"), new String[] { id + "" },
							null, null, "_id desc");
					cursor.moveToFirst();
					final String isdef = cursor.getString(cursor
							.getColumnIndex("_isdef"));
					changeAddressmessages(isdef, view, id);
				} else {
					selectAddress(view);
				}
			}

			private void selectAddress(View view) {
				String name = ((TextView) view.findViewById(R.id.tv_name))
						.getText().toString();
				String tel = ((TextView) view.findViewById(R.id.tv_tel))
						.getText().toString();
				String addressDetail = ((TextView) view
						.findViewById(R.id.tv_address_detail)).getText()
						.toString();
				
				
				Intent intent;
				if(requestcode==1){
					intent = new Intent(getApplicationContext(),
							SpecialSaleBuyAtOnce.class);
				}else if(requestcode==2){
					intent = new Intent(getApplicationContext(),
							ShopCartPostOrder.class);
				}else{
					return ;
				}
				intent.putExtra("name", name);
				intent.putExtra("tel", tel);
				intent.putExtra("address", addressDetail);
				setResult(2, intent);
				finish();
			}
		});
	}

	private void showdialog(final String isdef, final View view, final long id) {
		new AlertDialog.Builder(this)
				.setTitle("选项")
				.setItems(new String[] { "修改", "删除" },
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									changeAddressmessages(isdef, view, id);
									break;
								case 1:
									deleteAddressmsg(id + "");
								default:
									break;
								}
								dialog.dismiss();
							}
						}).show();

	}

	private void showDeleteaddress(final String id) {
		new AlertDialog.Builder(this)
				.setIcon(R.drawable.tm_dialog_delete)
				.setTitle("你确定要删除?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						deleteAddressmsg(id);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}

				}).show();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
 
}
