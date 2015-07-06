package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ShopMsg;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class SpecialShopLeft extends Fragment implements OnClickListener, NetAsyncListener {
	private ImageView shopimage;
	private TextView shopname;
	private TextView collections;
	private TextView address;
	private Cursor cr;
	private TextView contect;
	private Object tell;
	private String shopId;
	private String image=null;
	private String name=null;
	private String collection=null;
	private String province=null;
	private String city=null;
	private String area=null;
	private TextView tv_left_menu;
	private TextView tv_left_menu_call;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu, null);
		shopimage = (ImageView) view.findViewById(R.id.left_menu_shopimage);
		shopname = (TextView) view.findViewById(R.id.left_menu_shopname);
		collections = (TextView) view.findViewById(R.id.left_menu_collect);
		address = (TextView) view.findViewById(R.id.tv_left_menu_shop_address);
		contect = (TextView) view.findViewById(R.id.tv_left_menu_contect);
		tv_left_menu = (TextView) view.findViewById(R.id.tv_left_menu_call);
		TextView tv_left_call=(TextView) view.findViewById(R.id.tv_left_call);
		tv_left_call.setOnClickListener(this);
		contect.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		shopId = ((SpecialShop)getActivity()).getcenter();
		cr = DBUtilities.getShopmsg(shopId);
		
		if (cr.moveToFirst()){
			image = getcr("_picurl");
			name = getcr("_shopname");
			collection = getcr("_collects");
			province = getcr("_province");
			city = getcr("_city");
			area = getcr("_carea");
			tell=getcr("_tel");
			
		}else{
			loadshopProduct();
		}
		cr.close();
		setdata();
		
		
	}

	public void setdata() {
		ImageUtilities.loadBitMap(image, shopimage);
		shopname.setText(name);
		SQLiteDatabase pcdDB = ZhongTuanApp.getInstance().getPcdDB();
		collections.setText("收藏数："+collection);
		if (TextUtils.equals(province, "1000")&&province!=null) {
			pcdDB.rawQuery("select ProName from T_province where _id = ? ", new String []{province});
		}
		if (TextUtils.equals(city, "1000")&&city!=null){
			pcdDB.rawQuery("select CityName from T_City where _id= ?", new String []{city});
		}
		if (area!=null) {
			pcdDB.rawQuery("select ZoneName from T_Zone where _id =?", new String []{area});
		}
		if (TextUtils.equals(province, "1000")&&TextUtils.equals(city, "1000")) {
			address.setText("所在地：地址信息暂未确定");
		}else {
			address.setText("所在地："+province+" "+city+" "+area);
		}
		
		tv_left_menu.setText("联系电话："+tell);
	}
	private String getcr(String str){
		return cr.getString(cr.getColumnIndex(str));
	}

	@Override
	public void onClick(View v) {
		if (tell!=null) 
		
		
		{
			Intent telIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ tell));
			startActivity(telIntent);
		}else{
			Toast.makeText(getActivity(), "商家暂时没有预留电话号码", 0).show();
		}
		
		
	}
	public void loadshopProduct() {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_GETSHOP, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ShopMsg>() {
				}.getType();
				ShopMsg shopmsg = JsonUtilities.parseModelByType(elData, type);
				Model.save(new ShopMsg[] { shopmsg });
				return shopmsg;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("shopid", shopId));
			}
		};
		task_loadTemai.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_SPECIAL_GETSHOP:
			cr = DBUtilities.getShopmsg(shopId);
			if (cr.moveToFirst()){
				image = getcr("_picurl");
				name = getcr("_shopname");
				collection = getcr("_collects");
				province = getcr("_province");
				city = getcr("_city");
				area = getcr("_carea");
				tell=getcr("_tel");
				((SpecialShop)getActivity()).getCenterFragment().setShopName(cr);
			}
			setdata();
			break;
         
		default:
			break;
		}
		
	}

	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub
		
	}
}
