package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class EvaluationAdapter extends SimpleCursorAdapter {
	Context mContext;
	int layout;
	Cursor c;
	ArrayList<Cursor> cursorList = new ArrayList<Cursor>();
	private int height=0;
	//int position=0;
	//int [] heightarray=new int [4];
	// boolean isbegin=false;

	public EvaluationAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
		this.c = c;
		this.layout = layout;
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(mContext)
				.inflate(layout, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) view.findViewById(R.id.cusname);
		holder.time = (TextView) view.findViewById(R.id.time);
		holder.evaluation = (TextView) view.findViewById(R.id.pingjia);
		holder.bar = (RatingBar) view.findViewById(R.id.ratingbar1);
		holder.gridview = (GridView) view.findViewById(R.id.gridview);
		view.setTag(holder);
		//System.out.println("position:"+cursor.getPosition());
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		ViewHolder holder = (ViewHolder) view.getTag();
		initViews(c, holder,view);
	}

	private void initViews(Cursor c2, ViewHolder holder,View view) {
		float sum = Float.parseFloat(c2.getString(c2.getColumnIndex("_dfen")));
		String name = c2.getString(c2.getColumnIndex("_username"));
		String time = c2.getString(c2.getColumnIndex("_dateandtime"));
		String evaluation = c2.getString(c2.getColumnIndex("_recmemo"));
		String recpic = c2.getString(c2.getColumnIndex("_recpic"));

		holder.bar.setRating(sum);
		holder.name.setText(name.substring(0, 3) + "****" + name.substring(9));
		holder.time.setText(time.substring(0, 10));
		holder.evaluation.setText(evaluation);

		String[] pics = recpic.split("\\|");
		if (!recpic.equals("")) {
			holder.gridview.setVisibility(View.VISIBLE);
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < pics.length; i++) {
				HashMap<String, String> item = new HashMap<String, String>();
				item.put("pic", pics[i]);
				data.add(item);
			}
			PicAdapter adapter = new PicAdapter(mContext, data, R.layout.pic,
					new String[] { "pic" }, new int[] { R.id.img });
			holder.gridview.setAdapter(adapter);
			
			int count=adapter.getCount();
			int total=0;
			if(count%3!=0){
				count=(int)count/3+1;
			}
			else{
				count=count/3;
			}
			 for (int i = 0; i < count; i++) {   
		            View listItem = adapter.getView(i, null, holder.gridview);  
		            listItem.measure(0, 0);   
		            total += listItem.getMeasuredHeight();   
		        }   
			 LayoutParams params=holder.gridview.getLayoutParams(); 
			 int densty=ZhongTuanApp.getInstance().getDensity();
		        params.height = (int) (total + (densty * count));   
		        holder.gridview.setLayoutParams(params);  
		}
		else{
			holder.gridview.setVisibility(View.GONE);
		}
		/*if (this.position<c2.getPosition()&&isbegin) {
			view.measure(0, 0);
			height+=view.getMeasuredHeight();
			position++;
		}*/
		view.measure(0, 0);
		//System.out.println("c2.getPosition():"+c2.getPosition());
		//heightarray[c2.getPosition()]=view.getMeasuredHeight();
	}
    /*public int  getviewHeight(){
    	int h=0;
    	for (int i=0;i<heightarray.length;i++){
    		h+=heightarray[i];
    	}
    	return h;
    }*/
   /* public void setbeginmeasure(boolean isbegin){
    	this.isbegin=isbegin;
    }*/
	public static class ViewHolder {
		TextView name, time, evaluation;
		RatingBar bar;
		GridView gridview;
	}

	public class PicAdapter extends SimpleAdapter {
		ArrayList<HashMap<String, String>> data;
		public PicAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.data=(ArrayList<HashMap<String, String>>) data;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(mContext).inflate(R.layout.pic, parent, false);
				viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
				convertView.setTag(viewHolder);				
			}
			else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			ImageUtilities.loadBitMap(data.get(position).get("pic"), viewHolder.img);
			return convertView;
		}
		public class ViewHolder{
			ImageView img;
		}
	}

}
