package com.iteambuysale.zhongtuan.activity.me.unpay;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.UnpayListAdapter;
import com.iteambuysale.zhongtuan.adapter.UnpayListAdapter.ViewHolder;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTG;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTG;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

/**
 * @author lforxeverc
 * 
 */
public class UnpayActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, NetAsyncListener, OnItemClickListener {
	private Boolean editable = false;
	private Boolean tgFirstTime = true;
	private Boolean tmFirstTime = true;
	private static final int TUANGOU = 0;
	private static final int TEMAI = 1;
	private int tabState = 1; // 0为团购 1为特卖 （从未支付页面过来也是如此）
	private static final int UNPAY = 0;
	private static final int PAYED = 1;
	public static ArrayList<String> ordList;// 记录订单的list（删除多个订单使用）
	RadioGroup radioGroup;
	RadioButton tgRbtn, tmRbtn; // 顶部团购、特卖按钮
	CheckBox checkBox; // 删除状态下左边的选择框
	ListView listView; // 列表
	TextView headerTv; // 顶部标题
	Button backBtn, editBtn, deleteBtn;// 返回按钮 、右上角编辑按钮、未支付订单底部删除按钮
	CustomProgressDialog mProgressDialog;
	UnpayListAdapter adapter; // ListView的adapter
	String[] unpayFrom = { "_fcpmc", "_ordje", "_ordsl", "_fcppic", "_fcpmc" };
	String[] payedFrom = { "_fcpmc", "_ordje", "_ordsl", "_fcppic",
			"_dateandtime", "_ordzt" };
	int activityTag; // 已支付页面为1，待支付为0
	int[] unpayTo = { R.id.tv_tittle, R.id.tv_price, R.id.tv_sum, R.id.pic,
			R.id.tv_pay };
	int[] payedTo = { R.id.tv_tittle, R.id.tv_price, R.id.tv_sum, R.id.pic,
			R.id.tv_time, R.id.tv_pay };
	SQLiteDatabase db;
	Cursor cursor = null;
	int screenWidth, downX, upX; // 屏幕宽度，按下时x轴坐标值，释放时x轴坐标值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unpay);
		// 未支付成功后跳转到这里需要把之前的页面清除
		findViews();
		initViewsAndData();
		initListener();
		if (tabState == 0) {
			tgRbtn.performClick();
		} else {
			tmRbtn.performClick();
		}
	}

	/**
	 * 找到界面控件
	 * 
	 * 2015-1-15 lforxeverc
	 */
	private void findViews() {
		headerTv = (TextView) findViewById(R.id.tv_header_tittle);
		backBtn = (Button) findViewById(R.id.back);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		tgRbtn = (RadioButton) findViewById(R.id.rbtn1);
		tmRbtn = (RadioButton) findViewById(R.id.rbtn2);
		editBtn = (Button) findViewById(R.id.setting);
		deleteBtn = (Button) findViewById(R.id.btn_delete);
		listView = (ListView) findViewById(R.id.lv);
		checkBox = (CheckBox) findViewById(R.id.checkbox);

	}

	/**
	 * 顶部view，对话框等初始化操作
	 * 
	 * 2015-1-15 lforxeverc
	 */
	private void initViewsAndData() {
		screenWidth = ZhongTuanApp.getInstance().getScreenWidthPixels();
		mProgressDialog = CustomProgressDialog.createDialog(this);
		backBtn.setBackgroundResource(R.drawable.header_back);
		ordList = new ArrayList<String>();
		// 获得进入当前入眠的来源（0为我的未支付，1为我的已支付页面，默认为未支付页面）
		activityTag = getIntent().getIntExtra("activityTag", UNPAY);
		// 获得从支付失败后传过来的值，0为团购支付失败，1为特卖支付失败，没有标记默认为团购
		tabState = getIntent().getIntExtra("tgOrtm", TEMAI);
		db = ZhongTuanApp.getInstance().getRDB();
		if (activityTag == UNPAY) {
			headerTv.setText("待支付");
			editBtn.setText("编辑");
			editBtn.setOnClickListener(this);
		} else {
			headerTv.setText("已支付");
			editBtn.setVisibility(View.GONE);

		}
		setListView(activityTag);
	}

	/**
	 * 设置所有View的监听 2015-1-15 lforxeverc
	 */
	private void initListener() {
		backBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				changeTab(arg1);
				return false;
			}
		});

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// 根据当前状态加载数据
		if (tabState == TEMAI) {
			loadOrderList(D.API_MY_GETTMORDER);
		} else {
			loadOrderList(D.API_MY_GETTGORDER);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		// 顶部订单编辑（未支付页面）
		case R.id.setting:
			if (!editable) {
				editable = true;
				adapter.setEditable(true);
				adapter.notifyDataSetChanged();
				editBtn.setText("取消");
				deleteBtn.setVisibility(View.VISIBLE);
				clearCheckState();
			} else {
				editable = false;
				adapter.setEditable(false);
				adapter.notifyDataSetChanged();
				editBtn.setText("编辑");
				deleteBtn.setVisibility(View.GONE);
				ordList.clear();

			}
			break;
		// 删除订单按钮（未支付订单页面有效）
		case R.id.btn_delete:
			String ordnos = getOrdnoById(ordList);
			deleteUnpayOrder(ordnos);
			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.rbtn1:
			tabState = TUANGOU;
			ordList.clear();
			if (tgFirstTime) {
				loadOrderList(D.API_MY_GETTGORDER);
			} else {
				setListView(activityTag);
			}

			break;
		case R.id.rbtn2:
			tabState = TEMAI;
			ordList.clear();			
			if (tmFirstTime) {
				loadOrderList(D.API_MY_GETTMORDER);
			} else {
				setListView(activityTag);
			}
			break;

		}

	}

	/**
	 * 获取团购或者特卖订单信息
	 */
	public void loadOrderList(String url) {
			
		  mProgressDialog.show();
		NetAsync task_loadorder = new NetAsync(url, this) {
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}

			/* 处理返回数据 */
			@Override
			public Object processDataInBackground(JsonElement elData) {
				if (tabState == TUANGOU) {
					OrderTG.delete(OrderTG.class);
					OrderDetailsTG.delete(OrderDetailsTG.class);
					Type type = new TypeToken<Map<String, OrderTG>>() {
					}.getType();
					Map<String, OrderTG> orderMap = JsonUtilities
							.parseModelByType(elData, type);
					// SQLiteDatabase db = ZhongTuanApp.getInstance().getWDB();
					db.beginTransaction();
					for (OrderTG o : orderMap.values()) {
						o.save(db);
						OrderDetailsTG[] odList = o.getCpmx();
						Model.save(odList, db);
					}
					db.setTransactionSuccessful();
					db.endTransaction();
				}
				if (tabState == TEMAI) {
					OrderTM.delete(OrderTM.class);
					OrderDetailsTM.delete(OrderDetailsTM.class);
					Type type = new TypeToken<Map<String, OrderTM>>() {
					}.getType();
					Map<String, OrderTM> orderMap = JsonUtilities
							.parseModelByType(elData, type);
					// SQLiteDatabase db = ZhongTuanApp.getInstance().getWDB();
					db.beginTransaction();
					for (OrderTM o : orderMap.values()) {
						o.save(db);
						OrderDetailsTM[] odList = o.getCpmx();
						Model.save(odList, db);
					}
					db.setTransactionSuccessful();
					db.endTransaction();
				}
				return null;
			}
		};
		task_loadorder.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		listView.setVisibility(View.GONE);
		mProgressDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		listView.setVisibility(View.VISIBLE);
		mProgressDialog.dismiss();
		switch (reqUrl) {
		case D.API_MY_GETTGORDER:
			
			setListView(activityTag);
			tgFirstTime = false;
			break;
		case D.API_MY_GETTMORDER:
			setListView(activityTag);
			tmFirstTime = false;
			break;
		case D.API_DELETE_ORDERS:
			loadOrderList(D.API_MY_GETTGORDER);
			editBtn.performClick();
			break;
		case D.API_SPECIAL_ORDRSCANCEL:
			loadOrderList(D.API_MY_GETTMORDER);
			editBtn.performClick();
		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
		finish();

	}

	/**
	 * 根据activityTag设置列表内容tag==0未支付列表 activityTag==1已支付列表
	 * 
	 * @param activityTag
	 *            2015-1-14 lforxeverc
	 */
	private void setListView(int activityTag) {
		listView.setVisibility(View.INVISIBLE);
		if (cursor != null)
			cursor.close();
		// db = ZhongTuanApp.getInstance().getRDB();
		switch (activityTag) {
		case UNPAY:
			if (tabState == TUANGOU) {
				cursor = db.query("ORDER_TG_LIST", null, "_ordzt = ?",
						new String[] { "0" }, null, null, "_id desc");
			} else {
				cursor = db.query("ORDER_TM_LIST", null, "_ordzt = ?",
						new String[] { "0" }, null, null, "_id desc");
			}
			if (cursor.getCount() > 0)
				listView.setVisibility(View.VISIBLE);
			adapter = new UnpayListAdapter(this, R.layout.listitem_unpay,
					cursor, unpayFrom, unpayTo, 0, false, activityTag, tabState);
			if (editable) {
				adapter.setEditable(true);
			}
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			

			break;
		case PAYED:
			if (tabState == TUANGOU) {
				cursor = db.query("ORDER_TG_LIST", null, "_ordzt > ?",
						new String[] { "0" }, null, null, "_id desc");
			} else {
				cursor = db.query("ORDER_TM_LIST", null, "_ordzt > ?",
						new String[] { "0" }, null, null, "_id desc");
			}
			if (cursor.getCount() > 0)
				listView.setVisibility(View.VISIBLE);
			adapter = new UnpayListAdapter(this, R.layout.listitem_payed,
					cursor, payedFrom, payedTo, 0, false, activityTag, tabState);
			if (editable) {
				adapter.setEditable(true);
			}
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			break;

		}
		if (cursor.getCount() == 0)
			listView.setVisibility(View.INVISIBLE);
		listView.setVisibility(View.VISIBLE);

	}

	public void clearCheckState() {
		for (int i = 0; i < listView.getCount(); i++) {
			if (listView.getChildAt(i) == null) {
				View view = LayoutInflater.from(this).inflate(
						R.layout.listitem_unpay, null);
				((CheckBox) (view.findViewById(R.id.checkbox)))
						.setChecked(false);
			} else {
				((CheckBox) (listView.getChildAt(i).findViewById(R.id.checkbox)))
						.setChecked(false);
			}
		}
	}

	/**
	 * 从"ORDER_TG_LIST或"ORDER_TM_LIST"数据表查询订单号并拼装成一个字符串
	 * 
	 * @param id
	 * @return 2015-1-15 lforxeverc
	 */
	public String getOrdnoById(ArrayList<String> idList) {
		Cursor cursor;
		if (idList.isEmpty()) {
			Toast.makeText(this, "亲，没有订单被选中", Toast.LENGTH_SHORT).show();
			return "-1";
		}
		StringBuilder ordno = new StringBuilder();
		String[] idStr = new String[] {};
		StringBuilder whereStr = new StringBuilder();
		whereStr.append("_id in (");
		int len=idList.size();
		for (int i = 0; i < len; i++) {
			if (i != len - 1) {
				whereStr.append("?,");
			} else {
				whereStr.append("?)");
			}
		}
		idStr = idList.toArray(idStr);
		if (tabState == TUANGOU) {
			cursor = db.query("ORDER_TG_LIST", null, whereStr.toString(),
					idStr, null, null, null);
		} else {
			cursor = db.query("ORDER_TM_LIST", null, whereStr.toString(),
					idStr, null, null, null);
		}
		while (cursor.moveToNext()) {
			if (cursor.isLast()) {
				ordno.append(cursor.getString(cursor.getColumnIndex("_ordno")));
			} else {
				ordno.append(cursor.getString(cursor.getColumnIndex("_ordno"))
						+ ",");
			}
		}
		cursor.close();
		return ordno.toString();

	}

	/**
	 * 删除订单号
	 * 
	 * @param ordernos
	 *            2015-1-15 lforxeverc
	 */
	public void deleteUnpayOrder(final String ordernos) {
		if (ordernos.equals("-1")) {
			return;
		}
//		mDeleteDialog = CustomProgressDialog.createDialog(this);
//		mDeleteDialog.show();
		mProgressDialog.show();
		String url;
		if (tabState == TEMAI) {
			url = D.API_SPECIAL_ORDRSCANCEL;
		} else {
			url = D.API_DELETE_ORDERS;
		}
		NetAsync deleteTask = new NetAsync(url, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ordnos", ordernos));
			}
		};
		deleteTask.execute();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long id) {

		if (!editable) {

			String state = ((TextView) view.findViewById(R.id.tv_pay))
					.getText().toString();
			Intent intent = new Intent(UnpayActivity.this,
					OrderDetailActivity.class);
			intent.putExtra("id", id + "");// "ORDER_TG_LIST"或者"ORDER_TM_LIST"的"_id"
			intent.putExtra("activityTag", activityTag); // 当前是从那个页面进来的,待支付页面为“0”，已支付页面为”1“
			intent.putExtra("payState", state); // 订单状态
			intent.putExtra("TGorTM", tabState); // 团购或者特卖标签
			startActivity(intent);
			

		} else {
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			CheckBox checkBox = viewHolder.getCheckbox();
			if (checkBox.isChecked()) {
				ordList.remove(id + "");
				checkBox.setChecked(false);

			} else {
				ordList.add(id + "");
				checkBox.setChecked(true);

			}
		}

	}
	/**
	 * 根据手势来切换顶部特卖和团购按钮
	 * @param event
	 * 2015-1-28
	 * lforxeverc
	 */
	private void changeTab(MotionEvent event) {
		// 按下时记录x轴坐标
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = (int) event.getX();
		}
		// 释放时记录x轴坐标
		if (event.getAction() == MotionEvent.ACTION_UP) {
			upX = (int) event.getX();
		}
		// 如果当前状态时团购并且向左滑动的距离大于屏幕的三分之一则响应切换到特卖界面
		if(event.getAction()==MotionEvent.ACTION_UP){
		if (tabState == 0 && upX - downX> screenWidth / 3) {
			tmRbtn.performClick();
		}
		// 如果当前状态时特卖并且向右滑动的距离大于屏幕的三分之一则响应切换到团购界面
		if (tabState == 1 &&  downX - upX > screenWidth / 3) {
			tgRbtn.performClick();
		}
		}

	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
		ImageUtilities.removeBitmaps();
	}

}
