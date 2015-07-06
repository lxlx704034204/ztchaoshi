package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class CollectionAdapter extends SimpleCursorAdapter {
	Context mContext;

	ViewHolder viewHolder;

	private String uflb;

	public CollectionAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;

	}

	//
	// @Override
	// public void bindView(View arg0, Context arg1, Cursor arg2) {
	//
	// }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_collection, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.picView = (ImageView) view
					.findViewById(R.id.img_product);
			viewHolder.img_product_tag = (ImageView) view
					.findViewById(R.id.img_product_tag);
			viewHolder.title = (TextView) view
					.findViewById(R.id.tv_product_tittle);
			viewHolder.decription = (TextView) view
					.findViewById(R.id.webview_product_detail1);
			viewHolder.price = (TextView) view
					.findViewById(R.id.tv_product_price);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Cursor c = getCursor();
		c.moveToPosition(position);
		String title, picUrl, description, price;
		title = c.getString(c.getColumnIndex("_title"));
		picUrl = c.getString(c.getColumnIndex("_picurl"));
		description = c.getString(c.getColumnIndex("_memo"));
		price = c.getString(c.getColumnIndex("_dj"));
		uflb = c.getString(c.getColumnIndex("_uflb"));
		if (TextUtils.equals(uflb, "cpmx-tm")) {
			viewHolder.img_product_tag.setVisibility(View.GONE);
		} else{
			viewHolder.img_product_tag.setVisibility(View.VISIBLE);
		}
 
		viewHolder.title.setText(title);
		viewHolder.decription.setText(description);
		viewHolder.price.setText(price + "元");
		ImageUtilities.loadBitMap(picUrl, viewHolder.picView);
		return view;
	}

	// @Override
	// public void setViewText(TextView v, String text) {
	// switch (v.getId()) {
	// case R.id.tv_product_tittle:
	// v.setText(text);
	// break;
	// case R.id.webview_product_detail:
	// v.setText(text);
	// break;
	// case R.id.tv_product_price:
	// v.setText(text+"元");
	// break;
	//
	// default:
	// break;
	// }
	//

	public static class ViewHolder {
		ImageView picView, img_product_tag;
		TextView title, decription, price;
	}

}
