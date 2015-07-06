package com.iteambuysale.zhongtuan.fragment.home;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.me.setting.SettingActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.UnpayActivity_l;
import com.iteambuysale.zhongtuan.activity.me.waitgoods.WaitForGoods;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

/**
 * @author 杨楚寅 该版本为刘长源的版本
 * 
 */
public class MeFragment_l extends Fragment implements NetAsyncListener, OnClickListener {
	private TextView tv_quan_num;
	private TextView tv_tbmoney;
	private ImageView iv_avatar;
	private TextView tv_username;

	public static final MeFragment_l newInstance(String tag) {
		MeFragment_l fragment = new MeFragment_l();
		return fragment;
	}

	public MeFragment_l() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.f_me_main_l, null);
		initview(view);
		return view;
	}

	private void initview(View view) {
		ImageView iv_left_title = (ImageView) view
				.findViewById(R.id.iv_left_title);
		iv_left_title.setOnClickListener(this);
		TextView tv_header_title = (TextView) view
				.findViewById(R.id.tv_header_tittle);
		ImageView iv_right_title = (ImageView) view
				.findViewById(R.id.iv_right_title);
		iv_left_title.setImageResource(R.drawable.me_left_setting);
		iv_right_title.setImageResource(R.drawable.me_right_setting);
		tv_header_title.setText("我的");
		tv_quan_num = (TextView) view.findViewById(R.id.tv_quan_num);
		tv_tbmoney = (TextView) view.findViewById(R.id.tv_tbmoney);
		iv_avatar = (ImageView)view.findViewById(R.id.iv_avatar);
		iv_avatar.setOnClickListener(this);
		tv_username = (TextView) view.findViewById(R.id.tv_username);
		TextView tv_unpay=(TextView) view.findViewById(R.id.tv_unpay);
		tv_unpay.setOnClickListener(this);
		TextView tv_unget =(TextView) view.findViewById(R.id.tv_unget);
		tv_unget.setOnClickListener(this);
		TextView tv_wait_evla=(TextView) view.findViewById(R.id.tv_wait_evla);
		tv_wait_evla.setOnClickListener(this);
		TextView tv_sale_after=(TextView) view.findViewById(R.id.tv_sale_after);
		tv_sale_after.setOnClickListener(this);
		LinearLayout ll_my_order=(LinearLayout) view.findViewById(R.id.ll_my_order);
		ll_my_order.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		initdate();
	}

	private void initdate() {
		loadusermsg();
		String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
		User user = Model.load(new User(), uid);
		ImageUtilities.loadBitMap(user.getAvatar(), iv_avatar);
		tv_username.setText(user.getNickname());
		System.out.println("用户名："+user.getNickname());
		

	}

	private void loadusermsg() {
		NetAsync userMsg = new NetAsync(D.API_ME_USER_MSG, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				// TODO Auto-generated method stub
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		userMsg.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_ME_USER_MSG:
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			Map<String, String> map = JsonUtilities.parseModelByType(
					(JsonElement) data, type);
			tv_quan_num.setText(map.get("quan"));
			tv_tbmoney.setText(map.get("tbmoney"));
			break;
			
		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_unpay:
			intent = new Intent(getActivity(), UnpayActivity_l.class);
			intent.putExtra("orderStatus", 0);
			startActivity(intent);
			//Toast.makeText(getActivity(), "待支付", 0).show();
			break;
		case R.id.tv_unget:
			/*intent = new Intent(getActivity(), WaitForGoods.class);
			intent.putExtra("iswaitgood", true);// 是否是代收货
			startActivity(intent);*/
			intent = new Intent(getActivity(), UnpayActivity_l.class);
			intent.putExtra("orderStatus", 1);
			startActivity(intent);
			break;
		case R.id.tv_wait_evla:
			intent = new Intent(getActivity(), UnpayActivity_l.class);
			intent.putExtra("orderStatus", 2);
			startActivity(intent);
			//Toast.makeText(getActivity(), "待评价还未做", 0).show();
			break;
		case R.id.iv_avatar:
			String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
			User user = Model.load(new User(), uid);
			if(user.getMobile()==null){
				ZhongTuanApp.getInstance().logout(getActivity(),true);
				return ;
			}
			((HomeActivity)getActivity()).getHomeActor().changeTab(4);
			/*Intent intent_info = new Intent(getActivity(),
					PersonInfoActivity.class);
			startActivity(intent_info);*/
			break;
		case R.id.iv_left_title:
			Intent intent_edit = new Intent(getActivity(),
					SettingActivity.class);
			startActivity(intent_edit);
			break;
		case R.id.tv_sale_after:
			intent = new Intent(getActivity(), UnpayActivity_l.class);
			intent.putExtra("orderStatus", 3);
			startActivity(intent);
			break;
		case R.id.ll_my_order:
			intent =new Intent(getActivity(),UnpayActivity_l.class);
			intent.putExtra("orderStatus", 4);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	
}
