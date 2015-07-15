package com.iteambuysale.zhongtuan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.near.CitySelecteActivity;
import com.iteambuysale.zhongtuan.actor.HomeActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.HomeListener;
import com.iteambuysale.zhongtuan.listener.near.OnLocationClickListener;
import com.iteambuysale.zhongtuan.model.Version;
import com.iteambuysale.zhongtuan.service.UpdateService;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class HomeActivity extends SuperActivity implements HomeListener,OnLocationClickListener{
	
	private static int TAG_NEAR = 1;
	private static int TAG_SALE = 0;
	private static int TAG_ACTIVITIES = 2;
	private static int TAG_ME = 3;
	static HomeActor mActor;
	CustomProgressDialog mProgress;
//	TextView locationText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, true);
		setContentView(R.layout.home_main);
		FragmentManager fm = getSupportFragmentManager();

		//推送
		PushManager.getInstance().initialize(this.getApplicationContext());
		mActor = new HomeActor(this);
		mActor.initViews(fm);
		mActor.checkVersion();
	}
	public HomeActor getHomeActor(){
		return mActor;
	}
	@Override
	public View onCreateView(String name, @NonNull Context context,
			@NonNull AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}
	/*
	 * ======================== button Click Events
	 * ================================
	 */
	public ViewGroup getviewgroup(){
		return (RadioGroup)findViewById(R.id.rg_home);
	}
	/**
	 * 点击附近
	 * 
	 * @param v
	 */
	public void onClickBtnNear(View v) {
		mActor.changeTab(TAG_NEAR);
	}

	/**
	 * 点击特卖
	 * 
	 * @param v
	 */
	public void onClickBtnSale(View v) {
		mActor.changeTab(TAG_SALE);
	}

	/**
	 * 点击活动
	 * 
	 * @param v
	 */
	public void onClickActivities(View v) {
		mActor.changeTab(TAG_ACTIVITIES);
	}

	/**
	 * 点击我的
	 * @param v
	 */
	public void onClickMe(View v) {
		Drawable meTop = getResources().getDrawable(R.drawable.tab_bg_me);
		mActor.$rb("me").setCompoundDrawablesWithIntrinsicBounds(null, meTop,
				null, null);
		mActor.changeTab(TAG_ME);
	}





	/**
	 * SCROLL VIEW
	 */

	@Override
	public void onPageScrollStateChanged(int arg0) {
         
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		mActor.changeTabStatus(position);
	}

	@Override
	public void onPageSelected(int arg0) {
         
	}

	/*
	 * =================================== Status Events
	 * ==============================
	 */

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		alertUpdateMessage((Version) data);
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(this,false);
	}

	/**
	 * 处理版本更新
	 * 
	 * @param version
	 *            TODO Anddward.Liao <Anddward@gmail.com> 20142014-12-8下午5:22:11
	 */
/*	private void alertUpdateMessage(final Version version) {
		if (D.VERSION.equals(version.getAppver())) {
			return;
		}
		Builder alertVersionBuilder = new AlertDialog.Builder(this);
		alertVersionBuilder.setTitle("更新");
		alertVersionBuilder.setMessage("有新的版本" + version.getAppver() + "可以下载!");
		alertVersionBuilder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri download_url = Uri.parse(version.getAppurl());
				intent.setData(download_url);
				startActivity(intent);
			}
		});
		alertVersionBuilder.setNegativeButton("下次再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertVersionBuilder.create().show();
	}*/
	/** 处理版本更新
	 * @param version
	 */
	private void alertUpdateMessage(final Version version){
		if (D.VERSION.equals(version.getAppver())) {
			return;
		}else{
			View view = View.inflate(this, R.layout.version_updata, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final AlertDialog dialog = builder.create();
			dialog.setView(view);
			dialog.show();
			TextView tv_version =(TextView) view.findViewById(R.id.tv_version);
			tv_version.setText("版本更新: "+version.getAppver());
			TextView tv_version_content=(TextView) view.findViewById(R.id.tv_version_content);
			tv_version_content.setText("更新内容:\n"+version.getAppverm());
			TextView tv_next_to_say=(TextView) view.findViewById(R.id.tv_next_to_say);
			TextView tv_update_at_once=(TextView) view.findViewById(R.id.tv_update_at_once);
			tv_update_at_once.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), UpdateService.class);
					//intent.putExtra("url","http://localhost:8000/zapya/teambuyapp-1.1.10c.apk");
					intent.putExtra("url", version.getAppurl());
					dialog.dismiss();
					//System.out.println("url:"+version.getAppurl());
					startService(intent);
					/*Intent intent = new Intent(getApplicationContext(), FileDownLoadService.class);
					intent.putExtra("url", version.getAppurl());
					startService(intent);
					*/
					//finish();
				}
			});
			tv_next_to_say.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    dialog.dismiss();
                   // finish();
				}
			});
			
			
		}
	}
	public void locationclick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, CitySelecteActivity.class);
		startActivityForResult(intent, 2);
	}
	@Override
	protected void onActivityResult(int req, int res, Intent intent) {
		if(req==2&&res==1){
			mActor.$tv("location_name").setText(intent.getStringExtra("city"));			
		}
		else{
			
		}
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
	public void onLocationClick(View view) {
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageUtilities.removeBitmaps();
	}
	/**
	 * 购物车中的修改产品的数量的点击事件
	 * @param v
	 */
	/*public void changeProductNum(View v){
		Toast.makeText(this,v.getId()+"", 0).show();
		 View rootView = v.getRootView();
		 LinearLayout ll_change_num= (LinearLayout)rootView.findViewById(R.id.ll_change_num);
		 TextView tv_pr_introduce_cart= (TextView)rootView.findViewById(R.id.tv_pr_introduce_cart);
		 TextView size_and_color= (TextView)rootView.findViewById(R.id.size_and_color);
		 if(ll_change_num.getVisibility()==View.INVISIBLE){
			ll_change_num.setVisibility(View.VISIBLE);
			tv_pr_introduce_cart.setVisibility(View.INVISIBLE);
			size_and_color.setVisibility(View.INVISIBLE);
			((ImageButton)v).setBackgroundResource(R.drawable.cart_sure_num);
		 }else if(ll_change_num.getVisibility()==View.VISIBLE){
			    ll_change_num.setVisibility(View.INVISIBLE);
				tv_pr_introduce_cart.setVisibility(View.VISIBLE);
				size_and_color.setVisibility(View.VISIBLE);
				((ImageButton)v).setBackgroundResource(R.drawable.cart_change_num);
		 }*/
		 
	//}
	/*public void subNum(View v){
		Toast.makeText(this,v.getId()+"", 0).show();
	}
	public void addNum(View v){
		Toast.makeText(this,v.getId()+"", 0).show();
	}
*/
	
	public static HomeActor getactor(){
		return mActor;
	}
	
}