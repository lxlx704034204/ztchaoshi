package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;

public class AddressListAdapter extends SimpleCursorAdapter {
	SQLiteDatabase db;
	public AddressListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);	
		db = ZhongTuanApp.getInstance().getPcdDB();
		
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		super.bindView(arg0, arg1, arg2);
	}
    @Override
    public void setViewImage(ImageView iv, String text) {
    	super.setViewImage(iv, text);
    	if (TextUtils.equals(text, "1")) {
    		iv.setImageResource(R.drawable.defult_address_img);
		}
    	
    	
    }
	@Override
	public void setViewText(TextView v, String text) {
		super.setViewText(v, text);
		switch (v.getId()) {
		case R.id.tv_address_pro:
			Cursor proCursor=db.query("T_Province", null, "_id=?", new String[]{text}, null, null,null);
			proCursor.moveToFirst();
			v.setText(proCursor.getString(proCursor.getColumnIndex("ProName"))+"-");
			v.setTag(text);
			break;
		case R.id.tv_address_city:
			Cursor cityCursor=db.query("T_City", null, "_id=?", new String[]{text}, null, null,null);
			cityCursor.moveToFirst();
			v.setText(cityCursor.getString(cityCursor.getColumnIndex("CityName"))+"-");
			
			break;
			
		case R.id.tv_address_district:
			Cursor districtCursor=db.query("T_Zone", null, "_id = ?", new String[]{text}, null, null,null);
			if(districtCursor.moveToFirst()){
			v.setText(districtCursor.getString(districtCursor.getColumnIndex("ZoneName")));
			}
			else{
				v.setText("");
			}
			break;
		case R.id.tv_time:
			if(text.equals("0")){
				v.setText("所有时间");
				v.setTag(0);
			}
			else if(text.equals("1")){
				v.setText("周六周日及节假日可收货");
				v.setTag(1);
			}
			else if(text.equals("2")){
				v.setText("工作日可收货");
				v.setTag(2);
			}
			else{
				v.setText("所有时间");
				v.setTag(0);
			}
			break;

		default:
			break;
		}
	}

}
