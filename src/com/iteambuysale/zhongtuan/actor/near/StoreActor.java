package com.iteambuysale.zhongtuan.actor.near;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.near.StoreListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Product;
import com.iteambuysale.zhongtuan.model.Store;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public class StoreActor extends SuperActor {
	private StoreListener mListener;
	private String storeId;

	public StoreActor(Context context) {
		super(context);
		mListener = (StoreListener) context;
	}

	public void initViewsWithStoreId(String id) {
		storeId = id;
		Store store = Model.load(new Store(), id);
		initTitleBar(D.BAR_SHOW_LEFT, store.getShopname());
		$tv("title").setText(store.getShopname());
		$tv("name").setText(store.getShopname());
		$tv("priceAvg").setText(store.getPerfee());
		$tv("address").setText(store.getAddress());
		$tv("phone").setText("电话："+store.getTel());
		$tv("des").setText(D.DEFAULT_DESCRIPTION);
//		$iv("pic").setImageBitmap(ImageUtilities.loadBitMap( store.getPicurl(), $iv("pic"), this));
		ImageUtilities.loadBitMap(store.getPicurl(), $iv("pic"));
	}
	
	/**
	 * 加载产品列表
	 */
	public void loadProducts() {
		NetAsync task_getproducts = new NetAsync(D.API_CPMX_GETCPMXBYSHOP,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Product[]>(){}.getType();
				Product[] productList = JsonUtilities.parseModelByType(elData, type);
				Model.save(productList);
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_STORE_ID, storeId));				
			}
		};
		task_getproducts.execute();
	}
	

	/*=================================== Helpers ===============================*/
	/**
	 * 获取phone按钮中的电话
	 * @return
	 */
	public String getPhoneNum() {
		String str = $btn("phone").getText().toString();
		return StringUtilities.getPhoneNumFromString(str);
	}

}
