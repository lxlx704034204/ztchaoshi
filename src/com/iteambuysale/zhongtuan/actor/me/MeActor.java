package com.iteambuysale.zhongtuan.actor.me;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.MeListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class MeActor extends SuperActor {
	Context mContext;
	MeListener mListener;

	public MeActor(Context context,View v,MeListener listener) {
		super(context);
		setCurrentView(v);
		mContext = context;
		mListener = listener;
	}

	/* ====================== logic process ==================== */
	/**
	 * 初始化页面
	 */
	public void initView() {
		initTitleBar(D.BAR_SHOW_RIGHT, "我的中团");
		String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
		User user = Model.load(new User(), uid);
		$tv("tv_me_name").setText(user.getNickname());
	/*	if(!TextUtils.isEmpty(user.getTbmoney())){
			
			$tv("tv_me_rest").setText(user.getTbmoney());
		}else{
			$tv("tv_me_rest").setText("0");
		}*/
		$btn("setting").setOnClickListener(mListener);
		ImageUtilities.loadBitMap(user.getAvatar(), $iv("iv_avatar"));
		$btn("setting").setBackgroundResource(R.drawable.img_me_setting);
	}



	/* ======================= helpers ========================== */
	
}
