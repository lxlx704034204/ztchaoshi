package com.iteambuysale.zhongtuan.fragment.home;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.me.MeEvlution;
import com.iteambuysale.zhongtuan.activity.me.PersonInfoActivity;
import com.iteambuysale.zhongtuan.activity.me.ZTQActivity;
import com.iteambuysale.zhongtuan.activity.me.address.SelectedAddressActivity;
import com.iteambuysale.zhongtuan.activity.me.collection.CollectionListActivity;
import com.iteambuysale.zhongtuan.activity.me.setting.FeedbackActivity;
import com.iteambuysale.zhongtuan.activity.me.setting.SettingActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.UnpayActivity;
import com.iteambuysale.zhongtuan.activity.me.waitgoods.WaitForGoods;
import com.iteambuysale.zhongtuan.actor.me.MeActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.me.MeListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.iteambuysale.zhongtuan.views.MyScrollView;

public class MeFragment extends Fragment implements OnClickListener,
		MeListener, NetAsyncListener, OnTouchListener {
	private static final int REQUEST_PHOTO = 100;
	Button collectionBtn, payedBtn, ungetBtn, addressBtn,feedbackBtn;
	RelativeLayout unpayRl, infoRl;
	TextView tv_me_rest_ztq, tv_me_collection_sum;
	MeActor mActor;
	CustomProgressDialog mProgressDialog;
	RelativeLayout ztqRl;
	private RelativeLayout rl_me_refresh;
	private LinearLayout ll_me_fragment;
	private int height;
	private int inforlHeight;
	private boolean isfirstdown = true;
	private int downy;
	private int movey;
	private Button my_evlution;
	private TextView tv_alert_num;
	private SQLiteDatabase db;
	private TextView tv_me_rest;

	public static final MeFragment newInstance(String tag) {
		MeFragment fragment = new MeFragment();
		return fragment;
	}

	public MeFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/**
		 * 查找view
		 */
		View meView = inflater.inflate(R.layout.f_me_main, container, false);
		ztqRl = (RelativeLayout) meView.findViewById(R.id.ztqrl);
		infoRl = (RelativeLayout) meView.findViewById(R.id.rl_info);
		tv_me_rest_ztq = (TextView) meView.findViewById(R.id.tv_me_rest_ztq);
		tv_me_collection_sum = (TextView) meView
				.findViewById(R.id.tv_me_collection_sum);
		unpayRl = (RelativeLayout) meView.findViewById(R.id.unpayBtn);
		payedBtn = (Button) meView.findViewById(R.id.payedBtn);
		ungetBtn = (Button) meView.findViewById(R.id.ungetBtn);
		collectionBtn = (Button) meView.findViewById(R.id.tv_me_collection);
		addressBtn = (Button) meView.findViewById(R.id.addressBtn);
		feedbackBtn= (Button) meView.findViewById(R.id.feedbackBtn);
		Button havegettn = (Button) meView.findViewById(R.id.havegetBtn);
		tv_alert_num = (TextView) meView.findViewById(R.id.tv_alert_num);
		tv_alert_num.setVisibility(View.INVISIBLE);
		rl_me_refresh = (RelativeLayout) meView
				.findViewById(R.id.rl_me_refresh);
		ll_me_fragment = (LinearLayout) meView
				.findViewById(R.id.ll_me_fragment);
		my_evlution = (Button) meView.findViewById(R.id.my_evlution);
		MyScrollView sv_toch = (MyScrollView) meView.findViewById(R.id.sv_touch);
		//tv_me_rest = (TextView) meView.findViewById(R.id.tv_me_rest);
		//LinearLayout ll_me_main=(LinearLayout) meView.findViewById(R.id.ll_me_main);
		/**
		 * 初始化监听器
		 */
		unpayRl.setOnClickListener(this);
		infoRl.setOnClickListener(this);
		payedBtn.setOnClickListener(this);
		ungetBtn.setOnClickListener(this);
		addressBtn.setOnClickListener(this);
		ztqRl.setOnClickListener(this);
		collectionBtn.setOnClickListener(this);
		feedbackBtn.setOnClickListener(this);
		havegettn.setOnClickListener(this);
		sv_toch.setOnTouchListener(this);
		my_evlution.setOnClickListener(this);
	    //ll_me_fragment.setOnTouchListener(this);
		//ll_me_main.setOnTouchListener(this);
		/**
		 * 构造actor并显示
		 */
		mActor = new MeActor(getActivity(), meView, this);
		mActor.initView();
		return meView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rl_me_refresh.measure(0, 0);
		height = rl_me_refresh.getMeasuredHeight();
		ll_me_fragment.setPadding(0, -height, 0, 0);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		mActor.initView();
		loadSum();
		loadsendgoodmsg(D.API_MY_GETTMORDER);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ztqrl:
			Intent ztqIntent = new Intent(getActivity(), ZTQActivity.class);
			startActivity(ztqIntent);
			break;
		case R.id.setting:
			Intent intent_edit = new Intent(getActivity(),
					SettingActivity.class);
			startActivity(intent_edit);
			break;
		case R.id.unpayBtn:
			Intent intent_unpay = new Intent(getActivity(), UnpayActivity.class);
			intent_unpay.putExtra("activityTag", 0);
			startActivity(intent_unpay);
			break;
		case R.id.payedBtn:
			Intent intent_payed = new Intent(getActivity(), UnpayActivity.class);
			intent_payed.putExtra("activityTag", 1);
			startActivity(intent_payed);
			break;
		case R.id.addressBtn:
			Intent intent_address = new Intent(getActivity(),
					SelectedAddressActivity.class);
			intent_address.putExtra("isfromme", true);
			startActivity(intent_address);
			break;
		case R.id.rl_info:
			Intent intent_info = new Intent(getActivity(),
					PersonInfoActivity.class);
			startActivity(intent_info);
			break;
		case R.id.tv_me_collection:// tv_me_collection
			Intent intent_coll = new Intent(getActivity(),
					CollectionListActivity.class);
			startActivity(intent_coll);
			break;
		case R.id.ungetBtn:
			Intent intent_un = new Intent(getActivity(), WaitForGoods.class);
			intent_un.putExtra("iswaitgood", true);// 是否是代收货
			startActivity(intent_un);
			break;
		case R.id.havegetBtn:
			Intent intent_have = new Intent(getActivity(), WaitForGoods.class);
			intent_have.putExtra("iswaitgood", false);
			startActivity(intent_have);
			break;
		case R.id.feedbackBtn:
			Intent intent_fb = new Intent(getActivity(), FeedbackActivity.class);	
			startActivity(intent_fb);
			break;
		case R.id.my_evlution:
			Intent intent_ev=new Intent(getActivity(),MeEvlution.class );
			startActivity(intent_ev);
			break;
		}
//		ZhongTuanApp.getInstance().pushActivity(getActivity());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_PHOTO) {
			if (resultCode == Activity.RESULT_OK)
				Toast.makeText(getActivity(), "image save success!",
						Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 加载顶部中团券和待支付的数目 2015-1-15 lforxeverc
	 */
	public void loadSum() {
		NetAsync loadSumTask = new NetAsync(D.API_ME_USER_MSG, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		loadSumTask.execute();

	}
	private void loadsendgoodmsg(String url) {

		//mProgressDialog.show();
		NetAsync task_loadorder = new NetAsync(url, this) {
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}

			/* 处理返回数据 */
			@Override
			public Object processDataInBackground(JsonElement elData) {
				OrderTM.delete(OrderTM.class);
				OrderDetailsTM.delete(OrderDetailsTM.class);
				Type type = new TypeToken<Map<String, OrderTM>>() {
				}.getType();
				Map<String, OrderTM> orderMap = JsonUtilities.parseModelByType(
						elData, type);
				SQLiteDatabase db = ZhongTuanApp.getInstance().getWDB();
				db.beginTransaction();
				for (OrderTM o : orderMap.values()) {
					o.save(db);
					OrderDetailsTM[] odList = o.getCpmx();
					Model.save(odList, db);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				return null;
			}
		};
		task_loadorder.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_MY_GETTMORDER:
			db = ZhongTuanApp.getInstance().getRDB();
			 Cursor cursor = db.query("ORDER_TM_LIST", null,
					"_ordzt =? or _ordzt =?", new String[] { "1", "4" },
					null, null, "_id desc");
			 if(cursor!=null&&cursor.getCount()!=0){
				 tv_alert_num.setText(cursor.getCount()+"");
				 tv_alert_num.setVisibility(View.VISIBLE);
			 }
			break;
		case D.API_ME_USER_MSG:
			// 解析json并显示
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> map = JsonUtilities.parseModelByType(
					(JsonElement) data, type);
			tv_me_rest_ztq.setText(map.get("quan"));
			
			mActor.$tv("tv_me_rest").setText(map.get("tbmoney"));
            
			/**
			 ** 之前顶部是收藏，后改为未支付
			 */
			tv_me_collection_sum.setText(map.get("nopay"));
			break;

		default:
			break;
		}
		

	}

	private int getlocationy(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[1];
	}

	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(getActivity(),false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO
		switch (event.getAction()) {
		/*case MotionEvent.ACTION_DOWN:
			break;*/
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_MOVE:
			if (isfirstdown) {
				inforlHeight = getlocationy(infoRl);
				downy = (int) event.getRawY();
				isfirstdown = false;
			}
			movey = (int) event.getRawY();
			if (movey - downy <= height && movey - downy >= 0) {
				int dis = -height + getscrolldis((movey - downy),height);
				ll_me_fragment.setPadding(0, dis, 0, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			isfirstdown = true;
			ll_me_fragment.setPadding(0, -height, 0, 0);
			break;
		default:
			break;
		}
		return false;
	}
	public static int getscrolldis(int dis,int layoutheight){
		if(dis>layoutheight){
			return layoutheight;
		}else{
			int y=(int) (layoutheight*Math.sin(((double)dis/(2*layoutheight))));
			return y;
		}
	}
}
