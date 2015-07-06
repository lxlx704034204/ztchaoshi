package com.iteambuysale.zhongtuan.views;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.near.CategoryListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ProductCategory;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class CategorysView extends LinearLayout implements
		OnCheckedChangeListener, OnItemClickListener, NetAsyncListener, OnClickListener,OnKeyListener {
	RadioGroup group;
	RadioButton c1, c2, c3, c4, c5, selectedC;
	PopupWindow popupWindow, popupWindow1;
	ListView pLv, cLv, areaLv,sortLv,sumLv;
	Context mContext;
	LayoutInflater inflater;
	SQLiteDatabase db;
	Cursor pCursor, cCursor;
	TextView blank1,blank2;
	CategoryListener mListener;
	String pId,cId;
	TextView pHeader,cHeader;

	public CategorysView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CategorysView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		db = ZhongTuanApp.getInstance().getRDB();
		loadCategory();
		initPopupWindow();
		initRadioGroup();
		initListener();

	}


	private void initListener() {
		group.setOnCheckedChangeListener(this);
		pLv.setOnItemClickListener(this);
		cLv.setOnItemClickListener(this);
		sortLv.setOnItemClickListener(this);
		sumLv.setOnItemClickListener(this);
		pLv.setOnKeyListener(this);
		cLv.setOnKeyListener(this);
		sortLv.setOnKeyListener(this);
		sumLv.setOnKeyListener(this);
		blank1.setOnClickListener(this);
		blank2.setOnClickListener(this);
		
		pHeader.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
			c1.setText("全部分类");
			c5.setChecked(true);
			mListener.loadBusinessByTag("", "");
			popupWindow.dismiss();
			
			}
		});
		cHeader.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				mListener.loadBusinessByTag(pId, "");
				popupWindow.dismiss();
				
			}
		});
		
	}

	private void loadCategory() {
		NetAsync task1 = new NetAsync(D.API_PRODUCT_CATEGORY, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ProductCategory[]>() {
				}.getType();
				ProductCategory[] productCategory = JsonUtilities
						.parseModelByType(elData, type);
				Model.save(productCategory);
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		task1.execute();

	}

	private void initRadioGroup() {
		group = (RadioGroup) findViewById(R.id.radiogroup);
		c1 = (RadioButton) findViewById(R.id.c1);
		c2 = (RadioButton) findViewById(R.id.c2);
		c3 = (RadioButton) findViewById(R.id.c3);
		c4 = (RadioButton) findViewById(R.id.c4);
		c5 = (RadioButton) findViewById(R.id.c5);
		selectedC = c5;

	}

	private void initPopupWindow() {
		inflater = (LayoutInflater) mContext
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.categoryview, this);
		View view = inflater.inflate(R.layout.popupwindow, this, false);
		View sortView = inflater.inflate(R.layout.lv_singel_cate, this, false);
		View sumView = inflater.inflate(R.layout.lv_sum, this, false);
		pHeader=(TextView) inflater.inflate(R.layout.textview, null, false);
		cHeader=(TextView) inflater.inflate(R.layout.textview, null, false);
		
		popupWindow = new PopupWindow(this);
		popupWindow.setContentView(view);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);

		popupWindow1 = new PopupWindow(this);
		popupWindow1.setContentView(sortView);
		popupWindow1.setOutsideTouchable(true);
		popupWindow1.setFocusable(true);
		popupWindow1.update();
		popupWindow1.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow1.setWidth(LayoutParams.MATCH_PARENT);

		pLv = (ListView) view.findViewById(R.id.lv1);
		cLv = (ListView) view.findViewById(R.id.lv2);
		sortLv = (ListView) sortView.findViewById(R.id.lv_cate);
		sumLv=(ListView) sumView.findViewById(R.id.lv_sum);
		blank1=(TextView) view.findViewById(R.id.blank);
		blank2=(TextView) sortView.findViewById(R.id.blank1);
		
		pLv.addHeaderView(pHeader);
		cLv.addHeaderView(cHeader);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkId) {
		arg0.requestFocus();
		switch (checkId) {
		case R.id.c1:
			selectedC = c1;
			loadPCate();
			popupWindow.update();
			popupWindow.showAsDropDown(c1);		
			break;

		case R.id.c2:
			Toast.makeText(mContext, "暂未开通，敬请期待！", Toast.LENGTH_SHORT).show();
			c5.setChecked(true);
			break;
			
		case R.id.c3:
			selectedC = c3;
			ArrayAdapter<String> sortAdapter=new ArrayAdapter<>(mContext, R.layout.textview, new String[]{"智能排序","离我最近","人气最高","评价最高","价格最低","价格最高","最新发布","免预约优先"});
			sortLv.setAdapter(sortAdapter);
			popupWindow1.update();
			popupWindow1.showAsDropDown(c1);
			break;
			
		case R.id.c4:
			selectedC = c4;
			ArrayAdapter<String> sumAdapter=new ArrayAdapter<>(mContext, R.layout.textview, new String[]{"不限人数","单人餐","双人餐","3—4人餐","5—10人餐","10人以上","其他"});
			sumLv.setAdapter(sumAdapter);
			popupWindow1.update();
			popupWindow1.showAsDropDown(c1);
			break;
		case R.id.c5:
			
		}


	}

	private void loadPCate() {
		pCursor = db.query("PRODUCTCCATEGORY_LIST", null, "_cup=?",
				new String[] { "0" }, null, null, null);
		pCursor.moveToFirst();
		SimpleCursorAdapter pAdapter = new SimpleCursorAdapter(mContext,
				R.layout.textview, pCursor, new String[] { "_lbname" },
				new int[] { R.id.tv }, 0);
		pLv.setAdapter(pAdapter);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String selectedStr=((TextView)view).getText().toString();
		switch (arg0.getId()) {
		case R.id.lv1:
			c1.setText(selectedStr);
			if (hasChild(id)) {
				pId=id+"";
			} 
			else {
				pCursor.moveToPosition(position);
				//没有孩子列表
				mListener.loadBusinessByTag(id+"","");
				popupWindow.dismiss();
				
			}
			break;

		case R.id.lv2:
			c1.setText(selectedStr);
			cId=id+"";
			mListener.loadBusinessByTag(pId, cId);
			cCursor.moveToPosition(position-1);
			popupWindow.dismiss();
			break;

		case R.id.lv_cate:		
			c3.setText(selectedStr);
			popupWindow1.dismiss();
			Toast.makeText(mContext, "暂不可用！", Toast.LENGTH_SHORT).show();
			
			break;
        case R.id.lv_sum:	
        	c4.setText(selectedStr);
			popupWindow1.dismiss();
			Toast.makeText(mContext, "暂不可用！", Toast.LENGTH_SHORT).show();
			break;

		}
		c5.setChecked(true);
	}



	private boolean hasChild(long id) {
		cCursor = db.query("PRODUCTCCATEGORY_LIST", null, "_cup=?",
				new String[] { id + "" }, null, null, null);
		if (cCursor.getCount() != 0) {
			SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(mContext,
					R.layout.textview, cCursor, new String[] { "_lbname" },
					new int[] { R.id.tv }, 0);
			cLv.setAdapter(cAdapter);
			
			return true;
		}
		return false;
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		Toast.makeText(mContext, "加载分类失败！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {

	}

	@Override
	public void onTokenTimeout() {
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.blank:
			popupWindow.dismiss();
			break;

		case R.id.blank1:
			popupWindow1.dismiss();
			break;
		case R.id.tv:
			break;
		}
		c5.setChecked(true);
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent keyEvent) {
		if(keyEvent.getAction()==KeyEvent.ACTION_UP&&arg1==KeyEvent.KEYCODE_BACK){
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
			}
			if(popupWindow1.isShowing()){
			popupWindow1.dismiss();
			}

		}
		c5.setChecked(true);
		return false;
	}
	public void setOnCategoryListener(CategoryListener listener){
		mListener=listener;
	}
	public void setSelectedTittle(String text){
		c1.setText(text);
	}

}
