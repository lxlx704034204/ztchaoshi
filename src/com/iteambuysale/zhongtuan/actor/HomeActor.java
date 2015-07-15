package com.iteambuysale.zhongtuan.actor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.activity.me.PersonInfoFragment;
import com.iteambuysale.zhongtuan.adapter.SuperFragmentPagerAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.fragment.home.CategoryFragment;
import com.iteambuysale.zhongtuan.fragment.home.MeFragment;
import com.iteambuysale.zhongtuan.fragment.home.MeFragment_l;
import com.iteambuysale.zhongtuan.fragment.home.SaleFragmentVersion2;
import com.iteambuysale.zhongtuan.fragment.home.ShopCartFragment;
import com.iteambuysale.zhongtuan.fragment.home.ShopFragment;
import com.iteambuysale.zhongtuan.listener.HomeListener;
import com.iteambuysale.zhongtuan.model.Version;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class HomeActor extends SuperActor {

	HomeListener mListener;
	RadioButton[] rbArray = new RadioButton[]{$rb("sale"),$rb("near"),$rb("activities"),$rb("me")};
	Context context;
	public HomeActor(Context context) {
		super(context);
		mListener = (HomeListener)context;
	}

	public void initViews(FragmentManager fm) {
//		Drawable drawableTop = ZhongTuanApp.getInstance().getResources().getDrawable(R.drawable.tab_me_notify);
//		$rb("me").setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
//		$rb("near").performClick();
		
		ArrayList<Fragment> fList = new ArrayList<Fragment>();		
		//fList.add(new SaleFragment());
		fList.add(new SaleFragmentVersion2());
	/*	fList.add(new NearByFragment());
		fList.add(new TuangouhuiFragment());*/
		//fList.add(new ShopFragment());
		fList.add(new CategoryFragment());
		fList.add(new ShopCartFragment());
		//fList.add(new MeFragment());
		fList.add(new MeFragment_l());
		fList.add(new PersonInfoFragment());
		$vp("pager").setAdapter(new SuperFragmentPagerAdapter(fm, fList));
		$vp("pager").setOnPageChangeListener(mListener);
		$vp("pager").setOffscreenPageLimit(0);
		$rb("sale").performClick();
	}
	
	/**
	 * 鍒囨崲tab
	 * @param f
	 * @param fm
	 */
	public void changeTab(int position){
		$vp("pager").setCurrentItem(position);
	}

	/**
	 * 璇锋眰鐗堟湰鏇存柊淇℃伅
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-8涓嬪崍6:36:15
	 */
	public void checkVersion() {
		NetAsync task_checkVersion = new NetAsync(D.API_SYS_GETNEWVER,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Version>(){}.getType();
				Version version = JsonUtilities.parseModelByType(elData, type);
				return version;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				
			}
		};
		task_checkVersion.execute();
	}
	
	/**
	 * 鏀瑰彉tab鐨勭姸鎬�
	 * @param position
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-8涓嬪崍6:34:16
	 */
	public void changeTabStatus(int position) {
		if(position<rbArray.length){
			rbArray[position].setChecked(true);
		}
	}
}
