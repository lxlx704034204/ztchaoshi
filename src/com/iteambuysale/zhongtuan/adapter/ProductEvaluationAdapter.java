package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.model.TeMaiEvaluation;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;



public class ProductEvaluationAdapter extends BaseAdapter {
 ArrayList<TeMaiEvaluation> eva=new ArrayList<TeMaiEvaluation>();
private Context context;

public  ProductEvaluationAdapter(Cursor c,Context context){
	this.context=context;
	while (c.moveToNext()) {
		TeMaiEvaluation temai=new TeMaiEvaluation();
		String sum = c.getString(c.getColumnIndex("_dfen"));
		String name = c.getString(c.getColumnIndex("_username"));
		String time = c.getString(c.getColumnIndex("_dateandtime"));
		String evaluation = c.getString(c.getColumnIndex("_recmemo"));
		String recpic = c.getString(c.getColumnIndex("_recpic"));
		temai.setDfen(sum);
		temai.setDateandtime(time);
		temai.setUsername(name);
		temai.setRecmemo(evaluation);
		temai.setRecpic(recpic);
		eva.add(temai);
	}
    }
	@Override
	public int getCount() {
		return eva.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return eva.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if(convertView==null){
		    convertView =View.inflate(context, R.layout.listitem_pj, null);
			holder = new ViewHolder();
			holder.name = (com.iteambuysale.zhongtuan.views.MyTextView) convertView.findViewById(R.id.cusname);
			holder.time = (com.iteambuysale.zhongtuan.views.MyTextView) convertView.findViewById(R.id.time);
			holder.evaluation = (com.iteambuysale.zhongtuan.views.MyTextView) convertView.findViewById(R.id.pingjia);
			holder.bar = (RatingBar) convertView.findViewById(R.id.ratingbar1);
			holder.gridview = (GridView) convertView.findViewById(R.id.gridview);
			convertView.setTag(holder);		
		}else{
			holder =(ViewHolder) convertView.getTag();
		}
		holder.bar.setRating(Float.parseFloat(eva.get(position).getDfen()));
		holder.name.setText(eva.get(position).getUsername().substring(0, 3) + "****" + eva.get(position).getUsername().substring(9));
		holder.time.setText(eva.get(position).getDateandtime().substring(0, 10));
		holder.evaluation.setText(eva.get(position).getRecmemo());
		if (!TextUtils.equals(eva.get(position).getRecpic(), "")) {
			String[] pics = eva.get(position).getRecpic().split("\\|");
			holder.gridview.setAdapter(new Myadapter(pics));
			holder.gridview.setVisibility(View.VISIBLE);
			
		}else{
			holder.gridview.setVisibility(View.GONE);
		}   
		   //  convertView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return convertView;
	}
	public static class ViewHolder {
		com.iteambuysale.zhongtuan.views.MyTextView name, time, evaluation;
		RatingBar bar;
		GridView gridview;
	}
	 private class Myadapter extends BaseAdapter{
	        
			private String[] pics;
			
			public Myadapter(String[] pics) {
				this.pics=pics;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return pics.length;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return pics[position];
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView img;
				if (convertView==null) {
					convertView=View.inflate(context, R.layout.pic, null);
					img = (ImageView) convertView.findViewById(R.id.img);
					convertView.setTag(img);
				}else{
					 img=(ImageView)convertView.getTag();
				}
				ImageUtilities.loadBitMap(pics[position], img);
				
				return convertView;
			}

			
	    	 
	     }
	
}
