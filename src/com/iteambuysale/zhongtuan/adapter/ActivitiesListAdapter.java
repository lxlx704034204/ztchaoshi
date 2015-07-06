package com.iteambuysale.zhongtuan.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.listener.ActivitiesListener;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ActivitiesListAdapter extends SimpleCursorAdapter {
	Context context;
	ActivitiesListener listener;
	ImageView imageView;
	TextView tittle;
	TextView sum;
	ImageButton btn1,btn2,btn3,btn4;
	ListView mListView;

	public ActivitiesListAdapter(Context _context, Cursor c, String[] from, int[] to,ActivitiesListener _listener) {
		super(_context, R.layout.x_list_activities, c, from, to, 0);
		context = _context;
		listener = _listener;
	}
	@Override
	public void setViewImage(ImageView iv,final String url) {
		DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
		LayoutParams lp = iv.getLayoutParams();
		lp.height = 305*widthPixels/640;
		iv.setLayoutParams(lp);
//		iv.setTag(url);
//		iv.setImageBitmap(ImageUtilities.loadBitMap(url,iv,new ImageUpdateListener() {
//			@Override
//			public void onLoadImageSuccess(Bitmap bitmap) {
//				if(null != mListView){
//					ImageView tagImageView = (ImageView)mListView.findViewWithTag(url);
//					if(null != tagImageView)tagImageView.setImageBitmap(bitmap);
//				}
//			}
//		}));
		ImageUtilities.loadBitMap(url, iv);
	}

	@Override
	public void setViewText(TextView v, String text) {
		super.setViewText(v, text);
		int vid = v.getId();
		switch (vid) {
		case R.id.tv_event_time:
			v.setText("活动时间："+text);
			break;
		case R.id.tv_event_phone:
			v.setText("咨询电话："+text);
			break;
		case R.id.tv_event_qq:
			v.setText("QQ客服："+text);
			break;
		case R.id.tv_event_address:
			v.setText("活动地点："+text);
			break;	
		default:
			break;
		}
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		mListView = (ListView)parent;
		return super.getView(position, view, parent);
	}
	
}