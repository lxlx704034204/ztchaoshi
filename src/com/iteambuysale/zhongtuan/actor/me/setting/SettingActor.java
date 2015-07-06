package com.iteambuysale.zhongtuan.actor.me.setting;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.SettingListener;
import com.iteambuysale.zhongtuan.model.Version;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;


public class SettingActor extends SuperActor {

	private SettingListener mListener;
	
	public SettingActor(Context context) {
		super(context);
		mListener = (SettingListener)context;
	}
	
	public void initView(){
		initTitleBar(D.BAR_SHOW_LEFT, "设置");
	}
	
	public void checkVersion(){
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

}
