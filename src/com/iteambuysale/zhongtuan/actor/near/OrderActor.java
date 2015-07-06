package com.iteambuysale.zhongtuan.actor.near;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.near.OrderListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderCreate;
import com.iteambuysale.zhongtuan.model.Product;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class OrderActor extends SuperActor {
	private Context mContext;
	private OrderListener mListener;
	private String mPid;

	public OrderActor(Context context) {
		super(context);
		mContext = context;
		mListener = (OrderListener) context;
	}

	public void initViewWithProductId(String pid) {
		mPid = pid;
		Product p = Model.load(new Product(), pid);
		initTitleBar(D.BAR_SHOW_LEFT, "订单");
		$tv("name").setText(p.getCpmc());
		$tv("price").setText("￥" + p.getDj2());
		$tv("exPrice").setText("￥" + p.getDj2());
		$tv("totalPrice").setText("￥" + p.getDj2());
//		$iv("pic").setImageBitmap(ImageUtilities.loadBitMap(p.getPicurl(), $iv("pic"), this));
		ImageUtilities.loadBitMap(p.getPicurl(), $iv("pic"));
		upDateRecieveInfo(DBUtilities.getDefaultAddress());
		$rb("zhifubao").performClick();
	}

	/**
	 * 创建订单
	 */
	public void CreateOrder() {
		if (null == getUaid()) {
			mListener.onResultError(D.API_CPORD_CREATEORDER, "收货地址不能为空");
			return;
		}
		NetAsync task_post = new NetAsync(D.API_CPORD_CREATEORDER, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String,JsonElement>>(){}.getType();
				Map<String,JsonElement> mResult = JsonUtilities.parseModelByType(elData, type);
				JsonElement jCredential = mResult.get("credential");
				return jCredential.toString();
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				 params.add(new BasicNameValuePair(D.ARG_ORDER_UAID, getUaid()));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_PAYM, getPayMethod()));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_SENDM, "ups"));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_FAPIAO, "teambuy"));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_LNGO, ZhongTuanApp.getInstance().getLngo()));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_LATO, ZhongTuanApp.getInstance().getLato()));
				 Product p = Model.load(new Product(), mPid);
				 ArrayList<Product> products = new ArrayList<Product>();
				 products.add(p);
				 OrderCreate orderCreate = new OrderCreate(products);
				 params.add(new BasicNameValuePair(D.ARG_ORDER_SHOP, orderCreate.getOrderParamShop()));
				 params.add(new BasicNameValuePair(D.ARG_ORDER_CPMX, orderCreate.getOrderParamCpmx()));
			}
		};
		task_post.execute();
	}

	/**
	 * 加载用户历史地址
	 */
	public void loadUserAddress() {
		NetAsync task_getAddress = new NetAsync(D.API_CPORD_GETADDRESS, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<UserAddress[]>() {
				}.getType();
				UserAddress[] addressList = JsonUtilities.parseModelByType(elData, type);
				if (0 == addressList.length) {
					Model.delete(UserAddress.class);
				}
				Model.save(addressList);
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}
		};
		task_getAddress.execute();
	}

	/**
	 * 更新收货信息
	 * 
	 * @param address
	 */
	public void upDateRecieveInfo(UserAddress address) {
		if (null != address) {
			$rl("name_phone").setTag(address.getUaid());
			$tv("namePhone").setText(address.getTruename() + " " + address.getTel());
			$tv("address").setText(address.getAddress());
		}
	}

	/**
	 * 设置支付方式
	 * 
	 * @param method
	 */
	public void choosePayMethod(String method) {
		$rg("payChoose").setTag(method);
	}
	

	/* ======================================= Helpers ============================ */
	/**
	 * 获取uaid
	 * @return
	 */
	private String getUaid(){
		return (String)$rl("name_phone").getTag();
	}
	
	/**
	 * 获取支付方式
	 * @return
	 */
	private String getPayMethod(){
		return (String)$rg("payChoose").getTag();
	}

	public void notifyDownLoadPaymentServices() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("下载");
		builder.setMessage("缺少银联客户端，是否现在下载？");
		builder.setPositiveButton("立刻下载", mListener);
		builder.setNegativeButton("等等，我考虑一下", mListener);
		builder.create().show();
	}
}
