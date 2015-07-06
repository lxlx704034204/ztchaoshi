package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ProductStyleLsAdapter extends SimpleCursorAdapter{
   
	private Context context;

/*	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view=View.inflate(context, R.layout.shop_show_style_list,null);
		return view;
	}*/

	@Override
	public void setViewImage(ImageView view, String arg1) {
		super.setViewImage(view, arg1);
		switch (view.getId()) {
		case R.id.iv_shop_style:
			
			ImageUtilities.loadBitMap(arg1, view);
			break;

		default:
			break;
		}
	}

	@Override
	public void setViewText(TextView v, String text) {
		super.setViewText(v, text);
		switch (v.getId()) {
		
		case R.id.tv_shop_style_price:
			v.setText("￥"+text);
			break;
		case R.id.tv_shop_style_num:
			v.setText("销量"+text);
			break;
  
		default:
			break;
		}
	}

	public ProductStyleLsAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context=context;
	}
   
}
