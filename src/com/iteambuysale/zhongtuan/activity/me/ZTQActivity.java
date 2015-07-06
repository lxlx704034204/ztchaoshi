package com.iteambuysale.zhongtuan.activity.me;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.ZTQAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTG;
import com.iteambuysale.zhongtuan.model.ZTQ;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class ZTQActivity extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener, NetAsyncListener,
		OnTouchListener {
	Cursor ztqCursor, productCursor;
	CustomProgressDialog mDialog;
	RadioGroup radioGroup;
	RadioButton rbtn1, rbtn2;
	ImageView imgView;
	/**
	 * listView的item布局my_ztq.xml
	 */
	ListView listView;
	TextView tittle;
	Button back;
	List<HashMap<String, String>> unuseList, usedList;
	ZTQAdapter adapter1, adapter2;
	int downX, upX, screenWidth, tabStatus;// 按下时的x轴坐标，释放时的x轴坐标，屏幕宽度，当前选中的状态

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_ztq);

		loadZTQ();
		findViews();

		back.setOnClickListener(this);
		listView.setOnTouchListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		screenWidth = ZhongTuanApp.getInstance().getScreenWidthPixels();
		rbtn1.performClick();

	}

	/**
	 * 找到控件 2015-1-15 lforxeverc
	 */
	private void findViews() {
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		back = (Button) findViewById(R.id.back);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		rbtn1 = (RadioButton) findViewById(R.id.rbtn1);
		rbtn2 = (RadioButton) findViewById(R.id.rbtn2);
		listView = (ListView) findViewById(R.id.lv);
		imgView = (ImageView) findViewById(R.id.pic);
		tittle.setText("中团券");
		back.setBackgroundResource(R.drawable.header_back);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.rbtn1:
			listView.setAdapter(adapter1);
			tabStatus = 0;
			break;
		case R.id.rbtn2:
			listView.setAdapter(adapter2);
			tabStatus = 1;
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 加载中团券的异步任务 2015-1-15 lforxeverc
	 */
	public void loadZTQ() {
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		NetAsync loadSumTask = new NetAsync(D.API_ME_ZTQ, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, ZTQ>>() {
				}.getType();
				Map<String, ZTQ> ztqMap = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(ZTQ.class);
				for (String key : ztqMap.keySet()) {
					ZTQ ztq = ztqMap.get(key);
					ztq.save();
					OrderDetailsTG[] details = ztq.getCpmx();
					OrderDetailsTG.save(details);
				}
				return ztqMap;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		loadSumTask.execute();

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mDialog.dismiss();
		saveDataList();
		setAdapter();
	}

	/**
	 * 给未使用和已使用两个listview设置adapter 2015-1-15 lforxeverc
	 */
	private void setAdapter() {
		adapter1 = new ZTQAdapter(
				this,
				unuseList,
				R.layout.ztq_item,
				new String[] { "qno", "productName", "price", "edate", "picurl" },
				new int[] { R.id.tv_ztq, R.id.tv_ztqname, R.id.tv_price,
						R.id.tv_time, R.id.pic });
		adapter2 = new ZTQAdapter(
				this,
				usedList,
				R.layout.ztq_item,
				new String[] { "qno", "productName", "price", "edate", "picurl" },
				new int[] { R.id.tv_ztq, R.id.tv_ztqname, R.id.tv_price,
						R.id.tv_time, R.id.pic });
		listView.setAdapter(adapter1);
		ztqCursor.close();
		productCursor.close();

	}

	/**
	 * 通过查询ZTQ_LIST和PRODUCT_LIST两个数据表把中团券数据存储在unuseList和usedList两个list中
	 * 
	 * 2015-1-15 lforxeverc
	 */
	private void saveDataList() {
		unuseList = new ArrayList<HashMap<String, String>>();
		usedList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		ztqCursor = db.query("ZTQ_LIST", null, null, null, null, null,
				"_id desc");
		productCursor = db.query("ORDER_DETAILS_TG", null, null, null, null, null,
				null);
		while (ztqCursor.moveToNext()) {
			HashMap<String, String> item = new HashMap<String, String>();
			String productName = null;
			String price = null;
			String picurl = null;
			String qzt = ztqCursor.getString(ztqCursor.getColumnIndex("_qzt"));
			String qno = ztqCursor.getString(ztqCursor.getColumnIndex("_qno"));
			String edate = ztqCursor.getString(ztqCursor
					.getColumnIndex("_edate"));
			String cpmid = ztqCursor.getString(ztqCursor
					.getColumnIndex("_cpmid"));
			productCursor.moveToFirst();
			while (!productCursor.isAfterLast()) {
				if (productCursor
						.getString(productCursor.getColumnIndex("_cpmid")).equals(
								cpmid)) {
					productName = productCursor.getString(productCursor
							.getColumnIndex("_cpmc"));
					price = productCursor.getString(productCursor
							.getColumnIndex("_oje"));
					picurl = productCursor.getString(productCursor
							.getColumnIndex("_cppic"));
					break;
				}
				productCursor.moveToNext();
			}

			item.put("productName", productName);
			item.put("price", price);
			item.put("qno", qno);
			item.put("edate", edate);
			item.put("picurl", picurl);
			if (qzt.equals("0")) {
				unuseList.add(item);
			} else {
				usedList.add(item);
			}

		}

	}

	/**
	 * 根据滑动的距离和当前状态来切换页面
	 * 
	 * @param event
	 *            2015-1-28 lforxeverc
	 */
	private void changeTab(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = (int) event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			upX = (int) event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (tabStatus == 0 && downX - upX > screenWidth / 3) {
				rbtn2.performClick();
			}
			if (tabStatus == 1 && upX - downX > screenWidth / 3) {
				rbtn1.performClick();
			}
		}

	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,true);
		finish();
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
	public boolean onTouch(View arg0, MotionEvent arg1) {
		changeTab(arg1);
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageUtilities.removeBitmaps();
	}

}
