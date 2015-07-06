package com.iteambuysale.zhongtuan.actor.login;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.Html;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.AppSetting;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.login.LoginListener;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.utilities.VerificationUtilities;

public class LoginActor extends SuperActor{
	private LoginListener listener;

	public LoginActor(Context context) {
		super(context);
		listener = (LoginListener)context;
	}
	public void initView() {
		LogUtilities.Log(D.DEBUG, "initView()",D.DEBUG_DEBUG);
		$tv("forget_password").setText(Html.fromHtml("<a>忘记密码？</a>"));
		String phoneNumber=ZhongTuanApp.getInstance().getAppSettings().phoneNumber;
		$et("phone").setText(phoneNumber);
	}

	public void Login() {

		if (!VerificationUtilities.validateReversePhone(getReversText($et("phone")))) { // 校验手机号
			listener.onValidateFailed(D.ERROR_MSG_PHONE_NOTMATCH);
			return;
		}
		if (!VerificationUtilities.validatePassword(getText($et("password")))) { // 校验密码
			listener.onValidateFailed(D.ERROR_MSG_PASW_NOTMATCH);
			return;
		}
		
		NetAsync task_login = new NetAsync(D.API_USER_LOGIN, listener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				User user = JsonUtilities.parseModelByType(elData, User.class);
				user.save();
				AppSetting setting = ZhongTuanApp.getInstance().getAppSettings();
				setting.uid = user.getUid();
				setting.ackToken = user.getAcctoken();
				setting.phoneNumber = user.getMobile();
				ZhongTuanApp.getInstance().persistSave();
				return user;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_LOGIN_PHONE, getReversText($et("phone"))));
				params.add(new BasicNameValuePair(D.ARG_LOGIN_PASW, getMd5Text($et("password"))));
			}
		};
		task_login.execute();
	}

}
