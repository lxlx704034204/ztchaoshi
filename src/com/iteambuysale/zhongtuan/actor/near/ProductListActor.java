package com.iteambuysale.zhongtuan.actor.near;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.near.ProductListListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Product;
import com.iteambuysale.zhongtuan.model.Store;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ProductListActor extends SuperActor {
	private Context mContext;
	private Activity mActivity;
	private ProductListListener mListener;
	private int density;
	private int widthPixels;

	public ProductListActor(Context context) {
		super(context);
		mContext = context;
		mListener = (ProductListListener)context;
		mActivity = (Activity)context;
	}
	
	/**
	 * 根据商铺id初始化view
	 * @param sid
	 */
	public void initViewWithStoreId(String sid){
		initArgs();
		Store store = Model.load(new Store(), sid);
		initTitleBar(D.BAR_SHOW_LEFT, store.getShopname());
		$btn("phone").setText("电话："+store.getTel());
//		$iv("picure").setImageBitmap(ImageUtilities.loadBitMap(store.getPicurl(), $iv("picure"), this));
		ImageUtilities.loadBitMap(store.getPicurl(), $iv("picure"));
		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		Cursor cr = db.query("PRODUCT_LIST", null, "_shopid=?", new String[]{store.getSid()}, null, null, null);
		ArrayList<Product> pList = Model.load(Product.class, cr);
		cr.close();
		for (Product p :pList){
			addItem(pList.indexOf(p), p);
		}
	}
	
	/**
	 * 初始化界面参数
	 */
	private void initArgs(){
		density = (int) mContext.getResources().getDisplayMetrics().density;
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthPixels = dm.widthPixels;
	}
	
	/**
	 *  设置item的位置
	 * @param view
	 * @param itemId
	 */
	private void setPosition(View view, int itemId) {
		int left = (int)((itemId & 1) *( widthPixels/2-24) + 24);
		int top = density * ((itemId >> 1) * 100 + (itemId >> 1) * 4 + 4);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(view.getLayoutParams());
		lp.setMargins(left, top, 0, 0);
		lp.width = (int) (widthPixels/2-30);
		view.setLayoutParams(lp);
	}
	
	/**
	 *  根据item，绑定product，添加到item区域
	 * @param itemId
	 * @param product
	 */
	public void addItem(int itemId, Product product) {
		// TODO:改用view holder 提高性能
		View item_product = LayoutInflater.from(mContext).inflate(R.layout.x_block_product, $rl("productsArea"), false);
		setPosition(item_product, itemId);
		initWithProduct(item_product, product,$v(mContext));
		item_product.setOnClickListener(mListener);
		item_product.setTag(R.id.product_obj, product);
		$rl("productsArea").addView(item_product);
	}
	
	/**
	 * 根据productId初始化view
	 * @param view
	 * @param product
	 * @param parentView
	 */
	private void initWithProduct(View view, Product product,View parentView) {
		setCurrentView(view);
//		$iv("picture").setImageBitmap(ImageUtilities.loadBitMap(product.getPicurl(),$iv("picture"), this));
		ImageUtilities.loadBitMap(product.getPicurl(), $iv("picture"));
		$tv("name").setText(product.getCpmc());
		$tv("price").setText("￥"+product.getDj2());
		$tv("count").setText("销量:"+getCounts(product));
		setCurrentView(parentView);
	}
	

	/*================================ Helpers ============================*/
	/**
	 * 返回正确的“销量+单位”格式
	 * @param p
	 * @return
	 */
	private String getCounts(Product p){
		return p.getSells()+p.getJldw();
	}
}
