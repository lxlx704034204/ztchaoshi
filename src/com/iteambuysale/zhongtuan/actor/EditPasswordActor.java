package com.iteambuysale.zhongtuan.actor;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.me.waitgoods.WaitForGoods;
import com.iteambuysale.zhongtuan.background.CountDownAsync;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.EditPasswordListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.utilities.VerificationUtilities;

public class EditPasswordActor extends SuperActor {

	private EditPasswordListener mListener;
	String phoneNumber;
	String Tag="1";
	Context mContext;
	public String getTag() {
		return Tag;
	}

	public void setTag(String tag) {
		Tag = tag;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public EditPasswordActor(Context context) {
		super(context);
		mContext=context;
		mListener = (EditPasswordListener) context;
	}

	public void initView() {
		initTitleBar(D.BAR_SHOW_LEFT, "修改密码");
	}

	/**
	 * 获取验证码
	 */
	public void requestYzm() {
		CountDownAsync task_countDown = new CountDownAsync(120, mListener);
		task_countDown.start();
		NetAsync task_getYzm = new NetAsync(D.API_USER_SENDYZM, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				
				
				if (Tag.equals(D.CHANGE_PSW_IN)) {
					User user = Model.load(new User(), ZhongTuanApp.getInstance()
							.getAppSettings().uid);
					if(TextUtils.isEmpty(user.getMobile())){
						ZhongTuanApp.getInstance().logout(mContext,true);
						return ;
					}
					params.add(new BasicNameValuePair(D.ARG_REGISTER_MOBILE,
							StringUtilities.getReverseString(user.getMobile())));
				} 
				else if(Tag.equals(D.CHANGE_PSW_OUT)) {
					
					params.add(new BasicNameValuePair(
							D.ARG_REGISTER_MOBILE,
							StringUtilities.getReverseString(getPhoneNumber())));
				}
			}
		};
		task_getYzm.execute();
	}

	/**
	 * 执行密码修改
	 */
	public void postEdit() {
		if (!VerificationUtilities.validatePassword(getNewpsw())) {
			mListener
					.onResultError(D.API_USER_CHPWD, D.ERROR_MSG_PASW_NOTMATCH);
			return;
		}
		if (!VerificationUtilities.validatePassword(getNewpsw(), getRepsw())) {
			mListener.onResultError(D.API_USER_CHPWD,
					D.ERROR_MSG_PASW_NOT_EQUAL);
			return;
		}
		if (!VerificationUtilities.validateYZM(getYzm())) {
			mListener.onResultError(D.API_USER_CHPWD, D.ERROR_MSG_YZM_NULL);
			return;
		}

		NetAsync task_post = new NetAsync(D.API_USER_CHPWD, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_EDITPSW_USERNAME,
						getRevPhone()));
				params.add(new BasicNameValuePair(D.ARG_EDITPSW_PSW,
						getMD5Psw()));
				params.add(new BasicNameValuePair(D.ARG_EDITPSW_MOBYZM,
						getYzm()));
			}
		};
		task_post.execute();
	}

	/**
	 * 按钮进入倒数状态
	 */
	public void turnOffYzmButton() {
		$btn("getYzm").setClickable(false);
		$btn("getYzm").setBackgroundResource(R.drawable.bg_count_off);
		
	}

	/**
	 * 按钮倒数完成
	 */
	public void activeYzmButton() {
		$btn("getYzm").setClickable(true);
		$btn("getYzm").setBackgroundResource(R.drawable.bg_count_on);
		$btn("getYzm").setText("重新获取");
		
	}

	/**
	 * 刷新按钮秒数
	 * 
	 * @param second
	 */
	public void flashYzmButton(String second) {
		$btn("getYzm").setText(second+"s后再次获取");
		$btn("getYzm").setTextColor(mContext.getResources().getColor(R.color.default_black));
	}

	/*
	 * ========================================= Helpers
	 * ===============================
	 */
	private String getNewpsw() {
		return $et("newPass").getText().toString();
	}

	public String getRepsw() {
		return $et("repPass").getText().toString();
	}

	public String getYzm() {
		return $et("yzm").getText().toString();
	}

	public String getMD5Psw() {
		return StringUtilities.getMD5(getNewpsw());
	}

	private String getRevPhone() {
		if(Tag.equals(D.CHANGE_PSW_IN)){
		User user = Model.load(new User(), ZhongTuanApp.getInstance()
				.getAppSettings().uid);
		return StringUtilities.getReverseString(user.getMobile());
		}
		else{
			return StringUtilities.getReverseString(getPhoneNumber());
		}
	}

}
