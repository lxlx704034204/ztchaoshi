package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.ShopProductAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.fragment.home.SaleFragmentVersion2;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SpecialShopCenter extends Fragment implements OnClickListener,
		OnTouchListener, OnScrollListener, NetAsyncListener {
	public String shopId;
	private CustomProgressDialog mProgressDialog;
	private ShopProductAdapter adapter;
	private Cursor cr;
	private LinearLayout ll_shop;
	private Drawable llDrawable;
	private ListView lv_newproduct;
	private View header;
	private int locationy;
	private boolean isdown = true;
	private View footer;
	private Cursor cur;
	private String product[];
	private ShopProductAdapter adapter2;
	private ImageView[] ivs;
	private TextView[] tvSell;
	private ImageView iv_return;
	private int[] locations = new int[2];
	private LayoutParams params;
	private TextView tv_col_num;
	private TextView attention;
	private boolean ispayattention=false;
	private int fheight;
	private ListView lv_sale_good;
	private TextView tv_shopname;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopshow, null);
		shopId = getActivity().getIntent().getStringExtra("shopId");
		lv_newproduct = (ListView) view.findViewById(R.id.lv_newproduct);
		iv_return = (ImageView) view.findViewById(R.id.iv_shop_return);
		TextView tv_shop_search = (TextView) view
				.findViewById(R.id.tv_shop_search);
		tv_shop_search.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		cr = DBUtilities.getShopProductList(shopId);//TODO 新宝贝
		adapter = new ShopProductAdapter(getActivity(), cr);
		header = inflater.inflate(R.layout.shop_prodcut_list_header, null);
		// header.findViewById(R.id.tv_shopnamess);
		initheaderview();
		lv_newproduct.addHeaderView(header);
		ll_shop = (LinearLayout) view.findViewById(R.id.ll_shop);
		llDrawable = ll_shop.getBackground();
		llDrawable.setAlpha(0);
		lv_newproduct.setOnTouchListener(this);
		lv_newproduct.setOnScrollListener(this);
		footer = inflater.inflate(R.layout.shop_product_footer, null);
		lv_newproduct.addFooterView(footer);
		mProgressDialog = CustomProgressDialog.createDialog(getActivity());
		lv_newproduct.setAdapter(adapter);//TODO
		mProgressDialog.show();
		loadshopProduct();
		cur = DBUtilities.getpopularproductlist(shopId);//热卖的宝贝
		cur.getCount();
		lv_sale_good = (ListView) footer
				.findViewById(R.id.lv_sale_good);
		View footerView = inflater.inflate(R.layout.shop_product_list_footer,
				null);
		footerView.measure(0, 0);
		fheight = footerView.getMeasuredHeight();
		lv_sale_good.addFooterView(footerView);
		adapter2 = new ShopProductAdapter(getActivity(), cur);
		setSaleGoodListheight();
		initfooterview(footerView);
		lv_sale_good.setLayoutParams(params);
		lv_sale_good.setAdapter(adapter2);
		ispayattention();
		return view;
	}

	public void setSaleGoodListheight() {
		params = lv_sale_good.getLayoutParams();
		// params.height =
		// (cur.getCount()%2==0?cur.getCount()/2:cur.getCount()/2+1) *
		// SaleFragment.measureAdapterviewheight(getActivity()) + den * 310;
		params.height = (cur.getCount() % 2 == 0 ? cur.getCount() / 2 : cur
				.getCount() / 2 + 1)
				* SaleFragmentVersion2.measureAdapterviewheight(getActivity())
				+ fheight;
	}

	/**
	 * 查看是否关注了此商店；
	 */
	private void ispayattention() {
		NetAsync foucusAsyn=new NetAsync(D.API_CHECK_COLLECT,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("uflb", "shop-tm"));
				params.add(new BasicNameValuePair("lbid", shopId));
				
			}
		};
		foucusAsyn.execute();
	}

	private void initheaderview() {
		tv_shopname = (TextView) header
				.findViewById(R.id.tv_shopnamess);
		tv_col_num = (TextView) header
				.findViewById(R.id.tv_shop_collection_num);
		attention = (TextView) header
				.findViewById(R.id.tv_shop_payattenion);
		attention.setOnClickListener(this);
		tv_shopname.setOnClickListener(this);
		Cursor shopmsg = DBUtilities.getShopmsg(shopId);
		if (shopmsg.moveToFirst()) {
			setShopName(shopmsg);
		}
		shopmsg.close();
	}

	public void setShopName(Cursor shopmsg) {
		String shopname = shopmsg.getString(shopmsg
				.getColumnIndex("_shopname"));
		String collectonnum = shopmsg.getString(shopmsg
				.getColumnIndex("_collects"));
		if (shopname != null) {
			tv_shopname.setText(shopname);
		}
		tv_col_num.setText(collectonnum + "");
	}

	private void initfooterview(View footerView) {
		ImageView iv_num1 = (ImageView) footerView.findViewById(R.id.iv_num1);
		iv_num1.setOnClickListener(this);
		ImageView iv_num2 = (ImageView) footerView.findViewById(R.id.iv_num2);
		iv_num2.setOnClickListener(this);
		ImageView iv_num3 = (ImageView) footerView.findViewById(R.id.iv_num3);
		iv_num3.setOnClickListener(this);
		RelativeLayout rl_num1 = (RelativeLayout) footerView
				.findViewById(R.id.rl_num1);
		RelativeLayout rl_num2 = (RelativeLayout) footerView
				.findViewById(R.id.rl_num2);
		RelativeLayout rl_num3 = (RelativeLayout) footerView
				.findViewById(R.id.rl_num3);
		TextView tv_sell_num1 = (TextView) footerView
				.findViewById(R.id.tv_sell_num1);
		TextView tv_sell_num2 = (TextView) footerView
				.findViewById(R.id.tv_sell_num2);
		TextView tv_sell_num3 = (TextView) footerView
				.findViewById(R.id.tv_sell_num3);
		TextView tv_price = (TextView) footerView
				.findViewById(R.id.tv_price_num1);
		rl_num1.getBackground().setAlpha(100);
		rl_num2.getBackground().setAlpha(100);
		rl_num3.getBackground().setAlpha(100);
		ivs = new ImageView[] { iv_num1, iv_num2, iv_num3 };
		tvSell = new TextView[] { tv_sell_num1, tv_sell_num2, tv_sell_num3,
				tv_price };
		setsalegood();
	}

	private void setsalegood() {
		cur.moveToFirst();
		cur.moveToPrevious();
		product = new String[3];
		for (int i = 0; i < 3; i++) {
			if (cur.moveToNext()) {
				tvSell[i].setText("已售 "
						+ cur.getString(cur.getColumnIndex("_sells")));
				product[i] = cur.getString(cur.getColumnIndex("_id"));
				ImageUtilities.loadBitMap(
						cur.getString(cur.getColumnIndex("_picurl")), ivs[i]);
				if (i == 0) {
					tvSell[3].setText("￥"
							+ cur.getString(cur.getColumnIndex("_tmdj")));
				}
			}
		}
	}

	// 加载特卖商品列表
	public void loadshopProduct() {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_SELL, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2[]>() {
				}.getType();
				TemaiVerson2[] temaiList = JsonUtilities.parseModelByType(
						elData, type);
				Model.save(temaiList);
				return temaiList;
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
         
		//Toast.makeText(getActivity(), errMsg, 1).show();
		if(mProgressDialog!=null&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_SPECIAL_SELL:
			mProgressDialog.dismiss();
			cr = DBUtilities.getShopProductList(shopId);
			adapter.crtoObj(cr);
			adapter.notifyDataSetChanged();
			cur = DBUtilities.getpopularproductlist(shopId);
		    setSaleGoodListheight();
			adapter2.crtoObj(cur);
			adapter2.notifyDataSetChanged();
			setsalegood();
			break;
		case D.API_CHECK_COLLECT:
			ispayattention=true;
			attention.setBackgroundResource(R.drawable.weiguanzhu);
			attention.setText("取消");
		default:
			break;
		}
	
	}

	@Override
	public void onTokenTimeout() {
		if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		ZhongTuanApp.getInstance().logout(getActivity(),false);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			header.getLocationOnScreen(locations);
			if (isdown) {
				locationy = locations[1];
				isdown = false;
			}
			
/*			 int alpha = locationy - locations[1]; if (alpha >= 255) {
			  llDrawable.setAlpha(255); } else if (alpha < +0) {
			  llDrawable.setAlpha(0);
			  
			  } else { llDrawable.setAlpha(alpha); }*/
			 
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_num1:
			if (product[0] != null) {
				Intent intent1 = new Intent(getActivity(), ProductActivity.class);
				intent1.putExtra("productId", product[0]);
				startActivity(intent1);
			}else{
				Toast.makeText(getActivity(), "此商品暂时不存在", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_num2:
			if (product[1] != null) {
				Intent intent2 = new Intent(getActivity(), ProductActivity.class);
				intent2.putExtra("productId", product[1]);
				startActivity(intent2);
			}else{
				Toast.makeText(getActivity(), "此商品暂时不存在", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_num3:
			if (product[2] != null) {
				Intent intent3 = new Intent(getActivity(), ProductActivity.class);
				intent3.putExtra("productId", product[2]);
				startActivity(intent3);
			}else{
				Toast.makeText(getActivity(), "此商品暂时不存在", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_shop_return:
			if (((SpecialShop) getActivity()).isclickfleft()) {
				((SpecialShop) getActivity()).showleft();
			} else {
				iv_return.setAlpha(80);
				getActivity().finish();
			}
			break;
		case R.id.tv_shop_search:
			Intent intent4 = new Intent(getActivity(), SearchToList.class);
			intent4.putExtra("shopId", shopId);
			startActivity(intent4);
			break;
		case R.id.tv_shopnamess:
			((SpecialShop) getActivity()).showleft();
			break;
		case R.id.tv_shop_payattenion:

			//Toast.makeText(getActivity(), "关注成功", 0).show();
			if (!ispayattention) {
				addToCollection(D.API_ADD_COLLECT);
				ispayattention=true;
				attention.setBackgroundResource(R.drawable.weiguanzhu);
				attention.setText("取消");
			}else{
				addToCollection(D.API_CANCEL_COLLECT);
				ispayattention=false;
				attention.setBackgroundResource(R.drawable.guanzhu);
				attention.setText("关注");
			}
		default:
			break;
		}
	}

	/**
	 * 收藏店铺
	 */
	private void addToCollection(final String path) {
		final SharedPreferences pre = getActivity().getSharedPreferences(
				"zhongtuan_preference", Context.MODE_PRIVATE);
		NetAsync collectionAsync=new NetAsync(path,this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("uflb", "shop-tm"));
				params.add(new BasicNameValuePair("lbid", shopId));
				params.add(new BasicNameValuePair("lngo", pre.getString("lgn",
						"")));
				params.add(new BasicNameValuePair("lato", pre.getString("lat",
						"")));
				
			}
		};
		collectionAsync.execute();
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		header.getLocationOnScreen(locations);
		int alpha = locationy - locations[1];
		if (alpha >= 255) {
			llDrawable.setAlpha(255);
		} else if (alpha < 0) {
			llDrawable.setAlpha(0);

		} else {
			llDrawable.setAlpha(alpha);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (cr != null) {
			cr.close();
		}
		if (cur != null) {
			cur.close();
		}
	}
}
