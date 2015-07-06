package com.iteambuysale.zhongtuan.activity.near;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;

public class CitySelecteActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	ListView listView;
	AutoCompleteTextView searchText;
	ArrayList<Map<String, String>> list;
	ArrayList<String> autolist;
	TextView localCityView, tittle;
	Button imageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		findViews();
		initData();

	}
	/**
	 * 初始化界面数据
	 * 
	 * 2015-1-24
	 * lforxeverc
	 */
	private void initData() {
		tittle.setText("选择城市");
		localCityView.setText(ZhongTuanApp.getInstance().getCityName());
		list = DBUtilities.getList();
		autolist = DBUtilities.getAutoList();
		ArrayAdapter<String> autoAdater = new ArrayAdapter<String>(this,
				R.layout.city_text, R.id.text1, autolist);
		searchText.setAdapter(autoAdater);
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.city_item, new String[] { "cityName" },
				new int[] { R.id.city_name });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		searchText.setOnItemClickListener(this);
		imageButton.setOnClickListener(this);

	}

	/**
	 * 找到控件
	 */
	private void findViews() {
		imageButton = (Button) findViewById(R.id.back);
		imageButton.setBackgroundResource(R.drawable.header_back);
		localCityView = (TextView) findViewById(R.id.city_local);
		searchText = (AutoCompleteTextView) findViewById(R.id.city_search_edittext);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		listView = (ListView) findViewById(R.id.city_list);

	}
	
	/**
	 * 点击城市触发事件
	 * @param arg0
	 * 2015-1-24
	 * lforxeverc
	 */
	public void onCityClick(View arg0) {
		TextView cityText = (TextView) findViewById(arg0.getId());
		String cityName = cityText.getText().toString();
		localCityView.setText(cityName);
		ZhongTuanApp zhongtuan = ZhongTuanApp.getInstance();
		zhongtuan.setCityName(cityName);
		zhongtuan.setLocation();
		Intent intent = new Intent(CitySelecteActivity.this, HomeActivity.class);
		intent.putExtra("tag", D.OPT_NEAR);
		intent.putExtra("city",cityName);
		setResult(1,intent);
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
	public void onItemClick(AdapterView<?> v, View view, int arg2, long id) {
		// 顶部搜索框点击事件
		
		if(view.getId()==R.id.text1){
			String cityName = v.getItemAtPosition(arg2).toString();
			localCityView.setText(cityName);
			ZhongTuanApp zhongtuan = ZhongTuanApp.getInstance();
			zhongtuan.setCityName(cityName);
			zhongtuan.setLocation();
			Intent intent = new Intent(CitySelecteActivity.this,
					HomeActivity.class);
			intent.putExtra("tag", D.OPT_NEAR);
			intent.putExtra("city",cityName);
			setResult(1, intent);
			finish();
		}
		// 底部城市列表
		if(v.getId()==R.id.city_list){
			TextView cityText = (TextView) view.findViewById(R.id.city_name);
			String cityName1 = cityText.getText().toString();
			localCityView.setText(cityName1);
			ZhongTuanApp zhongtuan1 = ZhongTuanApp.getInstance();
			zhongtuan1.setCityName(cityName1);
			zhongtuan1.setLocation();
			Intent intent1 = new Intent(CitySelecteActivity.this,
					HomeActivity.class);
			intent1.putExtra("tag", D.OPT_NEAR);
			intent1.putExtra("city",cityName1);
			setResult(1, intent1);
			finish();
		}
		

	}
	
	@Override
	public void onClick(View arg0) {
		finish();
	}


}
