package com.iteambuysale.zhongtuan.activity.me.collection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.base.Basepager;
import com.base.CollectionListpager;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.CollectionAdapter;
import com.iteambuysale.zhongtuan.adapter.EvluteAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Collection;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class CollectionListActivity extends BaseActivity implements
	    OnCheckedChangeListener,
		OnClickListener, OnPageChangeListener {
	private static final String TUANGOU = "cpmx-cp";
	private static final String TEMAI = "cpmx-tm";
	//RefreshListView listview;
	RadioGroup radiogroup;
	RadioButton tgRb, tmRb;
	Button back;
	TextView tittle;
	//CustomProgressDialog mDialog;
	SQLiteDatabase db;
	CollectionAdapter adapter;
	private String nowStatus = "cpmx-tm";
	
	Boolean firstTime = true; // 第一次加载团购收藏
	// Boolean tmFirstTime = true; // 第一次加载特卖收藏
	Boolean isLoadMore = false; // 加载更多时状态为true，下拉刷新时状态为false
	int page = 0;
	Cursor cursor;
	private ImageView underline;
	private ViewPager vp_collection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_collection);
		initView();
		initData();
		tmRb.performClick();
	}

	private int getScreenWidth() {
		return getWindowManager().getDefaultDisplay().getWidth();
	}

	

	ArrayList<Basepager > vplist;
	private CollectionListpager temaipager;
	private CollectionListpager tuangoupager;
	/**
	 * 初始化页面和监听事件
	 * 
	 * 2015-1-14 lforxeverc
	 */
	private int Screenwidth;
	private void initData() {
		db = ZhongTuanApp.getInstance().getRDB();
		back.setBackgroundResource(R.drawable.header_back);
		tittle.setText("收藏");
		//setListView();
		
		setUndLineWidth();
		back.setOnClickListener(this);
		vplist = new ArrayList<Basepager>();
		temaipager = new CollectionListpager(this,true);
		tuangoupager = new CollectionListpager(this,false);
		vplist.add(temaipager);
		vplist.add(tuangoupager);
		EvluteAdapter vpAdapter = new EvluteAdapter(this, vplist);
		vp_collection.setAdapter(vpAdapter);
		vp_collection.setOnPageChangeListener(this);
		//loadCollections();
		vp_collection.setCurrentItem(0);
		//getUnderlineParams();

	}

	/**
	 * 设置下滑线的宽度；
	 */
	private void setUndLineWidth() {
		LayoutParams params = underline.getLayoutParams();
		Screenwidth=getScreenWidth();
		params.width=Screenwidth/2;
		
	}

	/**
	 * 找到对应控件和创建对话框
	 * 
	 * 2015-1-14 lforxeverc
	 */
	private void initView() {
		back = (Button) findViewById(R.id.back);
		tgRb = (RadioButton) findViewById(R.id.rb_tuangou);
		tmRb = (RadioButton) findViewById(R.id.rb_temai);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup_a);
		radiogroup.setOnCheckedChangeListener(this);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		vp_collection = (ViewPager) findViewById(R.id.vp_collect);
		underline = (ImageView) findViewById(R.id.underline);
		

	}

	
	

	/**
	 * 顶部团购特卖RadioGroup的点击事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// 将当前页面设置为第0页

		switch (checkedId) {
		case R.id.rb_tuangou:
			nowStatus = TUANGOU;
			// 如果是第一次加载，起一个任务加载，然后返回
			Model.delete(Collection.class);
			tuangoupager.loadCollections();
			vp_collection.setCurrentItem(1);
			tuangoupager.setListView();
			return;

		case R.id.rb_temai:
			nowStatus = TEMAI;
			// 如果是第一次加载，起一个任务加载，然后返回
			// if (firstTime) {
			temaipager.loadCollections();
			vp_collection.setCurrentItem(0);
			temaipager.setListView();
			return;
			/*
			 * }else{ setListView(); }
			 */

			// break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		ImageUtilities.removeBitmaps();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 从商品页面返回后重新查询数据库数据
		if (adapter != null) {
			updatelist();
		}
	}

	private void updatelist() {
		if (nowStatus==TEMAI) {
			temaipager.setListView();
		}else{
			tuangoupager.setListView();
		}
		
	}

	@Override
	protected void onDestroy() {
		if (cursor != null) {
			cursor.close();
		}
		super.onDestroy();
	}

	/*// 下拉刷新
	@Override
	public void OnPullDownRefresh() {
		Model.delete(Collection.class);
		page = 0;
		loadCollections();
	}

	// 上拉加载更多
	@Override
	public void onLoadingMore() {
		isLoadMore = true;
		loadCollections();

	}*/

	@Override
	public void onClick(View arg0) {
		finish();
	}

	
	

	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(arg1>0){
			underline.layout(arg2/2, underline.getTop(),
					arg2/2+underline.getWidth(), underline.getBottom());
			//underline.setmargin
			System.out.println("左："+underline.getLeft()+",右"+underline.getRight());
		}else if(arg1==0){
			System.out.println("arg1:"+arg1+",左："+underline.getLeft()+",右"+underline.getRight());
			underline.setVisibility(View.VISIBLE);
		}
		
	}
	/*public void getUnderlineParams(){
		new Thread(){
			public void run() {
				while(true){
					SystemClock.sleep(300);
					System.out.println("左："+underline.getLeft()+"右"+underline.getRight());
				}
			};
		}.start();
	}*/
	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			nowStatus=TEMAI;
			temaipager.setListView();
			temaipager.loadCollections();
			tmRb.setChecked(true);
			setunderlineparams(0);
			break;
		case 1:
			nowStatus=TUANGOU;
			tuangoupager.setListView();
			tuangoupager.loadCollections();
			tgRb.setChecked(true);
			setunderlineparams(Screenwidth/2);
			//System.out.println("团购");
			break;

		default:
			break;
		}
		
	}

	public void setunderlineparams(int x){
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) underline.getLayoutParams();
		params.leftMargin=x;
	}
}
