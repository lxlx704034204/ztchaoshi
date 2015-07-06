package com.iteambuysale.zhongtuan.actor;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.near.BusinessListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Store;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;

public class BusinessActor extends SuperActor {
	private BusinessListener mListener;

	public BusinessActor(Context context,BusinessListener listener) {
		super(context);
		mListener = listener;
	}

	public void initViews() {
		$lv("storeList").setOnItemClickListener(mListener);
	}

	/**
	 * 加载商铺列表
	 */
	public void loadStores() {	
		
		NetAsync task_loadShop = new NetAsync(D.API_SHOP_GETSHOP,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				DBUtilities.deleteStoreList();
				
				Type type = new TypeToken<Store[]>(){}.getType();
				Store[] stores = JsonUtilities.parseModelByType(elData, type);
				Model.save(stores);
				return stores;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_GETSHOPS_PAGE, "0"));	
				ZhongTuanApp app = ZhongTuanApp.getInstance();
				while (!"".equals(app.getLngo()) && !"".equals(app.getLato()) && !"".equals(app.getCityId())) {
					LogUtilities.Log(D.LOCATION_DEBUG, "exit while",D.DEBUG_DEBUG);
					LogUtilities.Log(D.LOCATION_DEBUG,"lngo:"+app.getLngo()+"\tlato:"+app.getLato()+"\tCityId:"+app.getCityId()+"\tCityCode:"+app.getCityCode(),D.DEBUG_DEBUG);
					break;
				}
				params.add(new BasicNameValuePair(D.ARG_GETSHOPS_CITYID, ZhongTuanApp.getInstance().getCityId()));
				params.add(new BasicNameValuePair(D.ARG_GETSHOPS_LNGO, ZhongTuanApp.getInstance().getLngo()));
				params.add(new BasicNameValuePair(D.ARG_GETSHOPS_LATO, ZhongTuanApp.getInstance().getLato()));
			}
		};
		task_loadShop.execute();
	}

	// 根据商品id加载商铺详情数据
	public void loadStoreById(final long id) {
		
		NetAsync task_loadStore = new NetAsync(D.API_SHOP_GETASHOP,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Store>(){}.getType();
				Store store = JsonUtilities.parseModelByType(elData, type);
				store.save();
				return store.getSid();
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_STORE_ID, String.valueOf(id)));				
			}
		};
		task_loadStore.execute();

	}

}
