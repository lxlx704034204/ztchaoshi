package com.iteambuysale.zhongtuan.actor.login;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.CountDownAsync;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.login.RegisterListener;
import com.iteambuysale.zhongtuan.model.DateTime;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.utilities.VerificationUtilities;

public class RegisterActor extends SuperActor{
	private RegisterListener listener;
	private CountDownAsync task_count;

	public RegisterActor(Context context) {
		super(context);
		listener = (RegisterListener)context;
	}
	
	/**
	 * 初始化页面
	 */
	public void initViews() {
		initTitleBar(D.BAR_SHOW_LEFT, "注册");
		$et("mobyzm").addTextChangedListener(listener);
	}

	
	
	/**
	 * 跳转到填补信息页面
	 */
	public void nextPage() {
		ToggleVisiable(new String[]{"first","second"});
		$tv("tv_header_tittle").setText("设置密码");
	}
	
	/**
	 * 返回注册首页
	 */
	public void toFirstPage() {
		ToggleVisiable(new String[]{"first","second"});
		$tv("tv_header_tittle").setText("注册");
	}


	/*========================================= 注册 ===================================*/
	/**
	 * 激活注册按钮
	 */
	public void activeRegist(){
		$btn("register").setClickable(true);
		$btn("register").setBackgroundResource(R.drawable.btn_new_on);
	}
	
	/**
	 * 冻结注册按钮
	 */
	public void turnOffRegist(){
		$btn("register").setClickable(false);
		$btn("register").setBackgroundResource(R.drawable.btn_new_off);
	}
	/**
	 * 检查帐号是否可以注册
	 */
	public void checkRegistAble() {
		/* 参数校验 */
		if (!VerificationUtilities.validateReversePhone(getReversText($et("phone")))) {
			listener.onValidateFailed(D.ERROR_MSG_PHONE_NOTMATCH);
			return;
		}
		/*if (!VerificationUtilities.validatePassword(getText($et("password")))) {
			listener.onValidateFailed(D.ERROR_MSG_PASW_NOTMATCH);
			return;
		}
		if (!VerificationUtilities.validatePassword(getText($et("password")), getText($et("repeat_password")))) {
			listener.onValidateFailed(D.ERROR_MSG_PASW_NOT_EQUAL);
			return;
		}*/

		NetAsync task_checkName = new NetAsync(D.API_USER_CHECKUSERNAME,listener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_LOGIN_PHONE, getReversText($et("phone"))));
			}
		};
		task_checkName.execute();
	}
	
	/**
	 * 注册
	 */
	public void regist() {		
		stopCountDown();
		if (!VerificationUtilities.validatePassword(getText($et("password")))) {
			listener.onValidateFailed(D.ERROR_MSG_PASW_NOTMATCH);
			return;
		}
		if (!VerificationUtilities.validatePassword(getText($et("password")), getText($et("repeat_password")))) {
			listener.onValidateFailed(D.ERROR_MSG_PASW_NOT_EQUAL);
			return;
		}
		NetAsync task_register = new NetAsync(D.API_USER_REGISTER,listener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				User user = JsonUtilities.parseModelByType(elData, User.class);
				ZhongTuanApp.getInstance().getAppSettings().uid = user.getUid();
				ZhongTuanApp.getInstance().getAppSettings().ackToken = user.getAcctoken();
				ZhongTuanApp.getInstance().persistSave();
				user.save();
				return user;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_REGISTER_PHONE, getReversText($et("phone"))));
				params.add(new BasicNameValuePair(D.ARG_REGISTER_PASW, getMd5Text($et("password"))));
				//params.add(new BasicNameValuePair(D.ARG_REGISTER_NICKNAME, getText($et("nickname"))));
				//params.add(new BasicNameValuePair(D.ARG_REGISTER_SEX, getSelectorSex()));
				//params.add(new BasicNameValuePair(D.ARG_REGISTER_BIRTHDAY, getText($tv("birthday"))));
				params.add(new BasicNameValuePair(D.ARG_REGISTER_MOBY_YZM, getText($et("mobyzm"))));				
			}
		};
		task_register.execute();	
	}
	

	/*=============================== 日期选择 ==============================*/
	/**
	 * 获取日期选择器需要的时间，如果没有，则返回当前日期
	 * @return
	 */
	public DateTime getDateTime() {
		String date = getText($tv("birthday"));
		return StringUtilities.getDateTimefromString(date);
	}

	/**
	 * 设置生日年月日
	 * @param date
	 */
	public void showDateTime(DateTime date) {
		$tv("birthday").setText(date.toString());
	}
	
	/*=============================== 获取验证码 ================================*/
	/**
	 * 获取验证码
	 */
	public void getYzm(){
		NetAsync task_getMobyzm = new NetAsync(D.API_USER_SENDYZM,listener) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("mobile", getReversText($et("phone"))));
			}
		};
		task_getMobyzm.execute();
	}
	
	/**
	 * 激活验证码按钮
	 */
	public void activeYzmButton() {
		$btn("get_mobyzm").setClickable(true);
		//$btn("get_mobyzm").setBackgroundResource(R.drawable.btn_new_on);
		$btn("get_mobyzm").setText("重新获取");
	}

	/**
	 * 刷新验证码倒数秒数
	 * @param second
	 */
	public void flashYzmButton(String second){
		$btn("get_mobyzm").setText(second);
	}

	/**
	 * 冻结发送验证码按钮
	 */
	public void turnOffYzmButton() {
		$btn("get_mobyzm").setClickable(false);
		//$btn("get_mobyzm").setBackgroundResource(R.drawable.btn_new_off);
		//$btn("get_mobyzm").setText("获取验证码");
	}
	
	/**
	 * 开始倒计时
	 */
	public void beginCountDown() {
		// 开始倒计时
		task_count = new CountDownAsync(120, listener);
		task_count.startCountDown();
	}
	
	/**
	 * 停止倒计时
	 */
	public void stopCountDown(){
		// 如果还在倒计时，停止倒计时
		if (null != task_count) {
			task_count.cancel();
		}		
		activeYzmButton();
	}
	
	/*==================================== helpers ============================*/
	/**
	 * 获取用户性别
	 * @return
	 */
	private String getSelectorSex() {
		if ($rb("male").isChecked()) {
			return String.valueOf(D.MALE);
		} else if ($rb("female").isChecked()) {
			return String.valueOf(D.FEMALE);
		} else {
			return String.valueOf(D.UNKNOW);
		}
	}

	public void veryYzm() {
		NetAsync task_veryMobyzm = new NetAsync(D.VERY_YZM,listener) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("mobile", getReversText($et("phone"))));
				params.add(new BasicNameValuePair("yzm", getText($et("mobyzm"))));
			}
		};
		task_veryMobyzm.execute();
		
	}



}
