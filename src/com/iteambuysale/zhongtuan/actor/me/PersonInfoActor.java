package com.iteambuysale.zhongtuan.actor.me;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.PersonInfoListener;
import com.iteambuysale.zhongtuan.model.DateTime;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class PersonInfoActor extends SuperActor{

	Context mContext;
	PersonInfoListener mListener;

	public PersonInfoActor(Context context) {
		super(context);
		mContext = context;
		mListener = (PersonInfoListener)context;
	}

	/* ====================== logic process ==================== */
	public void initViews() {
		initTitleBar(D.BAR_SHOW_LEFT, "我的信息");
		initViewWithCurrentUser();
	}

	/**
	 * 切换到修改信息状态
	 */
	public void changeStateEdit() {
		ToggleVisiable(new String[] { "edit", "post", "sex", "rg_sex", "email", "input_email", "signate", "input_signate" });
		$tv("birthday_tag").setText("点击修改生日");
		$tv("birthday").setClickable(true);
	}

	/**
	 * 切换到显示信息状态
	 */
	public void changeStateDisplay() {
		initViewWithCurrentUser();
		ToggleVisiable(new String[]{ "edit", "post", "sex", "rg_sex", "email", "input_email", "signate", "input_signate"});
		$tv("birthday_tag").setText("出生日期");
		$tv("birthday").setClickable(false);
	}
	
	/**
	 * 提交修改结果
	 */
	public void postEdit(){
		NetAsync task_postEdit = new NetAsync(D.API_USER_EDITUSER,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				User user= JsonUtilities.parseModelByType(elData, User.class);
				user.save();
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_NICKNAME, getNickname()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_SEX, getSex()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_BIRTHDAY, getBirthday()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_EMAIL, getEmail()));
				params.add(new BasicNameValuePair(D.ARG_EDITUSER_SIGNATE, getSignate()));
			}
		};
		task_postEdit.execute();
	}

	/**
	 *  获取日期控件需要显示的值
	 * @return
	 */
	public DateTime getBirthDateTime(){
		String birthday = getBirthday();
		if (!"".equals(birthday)){
			return new DateTime(birthday);
		}
		return new DateTime();
	}
	
	/**
	 * 设置日期为用户选择的日期
	 * @param date
	 */
	public void setBirthday(String date){
		$tv("birthday").setText(date);
	}

	/* ======================= helpers ========================== */

	/**
	 * 使用当前用户信息初始化view
	 */
	private void initViewWithCurrentUser(){
		String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
		User user = Model.load(new User(), uid);
		
		$tv("nickname").setText(user.getNickname());
		ImageUtilities.loadBitMap(user.getAvatar(), $iv("avatar"));
		$tv("uid").setText(user.getMobile());
		$tv("birthday").setText(user.getBirthday());
		$tv("email").setText(user.getEmail());
		$et("input_email").setText(user.getEmail());
		$tv("signate").setText(user.getSignate());
		$et("input_signate").setText(user.getSignate());
		if(TextUtils.isEmpty(user.getSex())){
			ZhongTuanApp.getInstance().logout(mContext,true);
			((Activity)mContext).finish();
			return ;
		}
		switch (Integer.valueOf(user.getSex())) {
		case D.MALE:
			$tv("sex").setText("男");
			$rb("man").setChecked(true);
			break;
		case D.FEMALE:
			$tv("sex").setText("女");
			$rb("woman").setChecked(true);
			break;
		default:
			$tv("sex").setText("未知");
			$rb("man").setChecked(true);
			break;
		}
	}
	
	private String getSex(){
		if ($rb("man").isChecked()){
			return String.valueOf(D.MALE);
		}else{
			return String.valueOf(D.FEMALE);
		}
	}
	
	private String getEmail(){
		return $et("input_email").getText().toString();
	}

	private String getBirthday(){
		return $tv("birthday").getText().toString();
	}
	
	private String getNickname(){
		return $tv("nickname").getText().toString();
	}
	
	private String getSignate(){
		return $et("input_signate").getText().toString();
	}

	public void updateAvata() {
		// 删除旧的头像
		User user = Model.load(new User(), ZhongTuanApp.getInstance().getAppSettings().uid);
		ImageUtilities.clearImageCache(user.getAvatar(), $iv("avatar"));
		//ImageUtilities.loadBitMap("avatar.png", $iv("avatar"),"avatar.png");//以前的版本
		
	}
}
