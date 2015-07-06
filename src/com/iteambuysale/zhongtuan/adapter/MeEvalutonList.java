package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
//String [] from=new String[]{"_cpmc","_dfen","_recmemo","_recpic","_dateandtime"};
//int [] to=new int[]{R.id.tv_pro_name,R.id.rb_ratingbar,R.id.tv_evalution_detial,R.id.gv_e_gridview};
public class MeEvalutonList extends SimpleCursorAdapter {

	public MeEvalutonList(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
	}

	public MeEvalutonList(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//super.bindView(view, context, cursor);
		ViewHolder holder=(ViewHolder) view.getTag();
		holder.tv_pro_name.setText(getCStr(cursor, "_cpmc"));
		holder.tv_evalution_detial.setText(getCStr(cursor, "_recmemo"));
		holder.tv_evlute_time.setText(getCStr(cursor, "_dateandtime"));
		holder.rb_ratingbar.setRating(Float.parseFloat(getCStr(cursor, "_dfen")));
		String recpic =getCStr(cursor, "_recpic");
		recpic.length();
		if (recpic!=null&&!recpic.equals("")) {
			String[] pics = getCStr(cursor, "_recpic").split("\\|");
			holder.gv_e_gridview.setAdapter(new Myadapter(pics));
			holder.gv_e_gridview.setVisibility(View.VISIBLE);
			
		}else{
			holder.gv_e_gridview.setVisibility(View.GONE);
		}
	}
     @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
    	 View view =View.inflate(context, R.layout.listitem_evalution, null);
    	 ViewHolder holder=new ViewHolder();
    	 holder.tv_pro_name= (TextView) view.findViewById(R.id.tv_pro_name);
    	 holder.rb_ratingbar=(RatingBar) view.findViewById(R.id.rb_ratingbar);
    	 holder.tv_evalution_detial=(TextView) view.findViewById(R.id.tv_evalution_detial);
    	 holder.gv_e_gridview=(GridView) view.findViewById(R.id.gv_e_gridview);
    	 holder.tv_evlute_time=(TextView) view.findViewById(R.id.tv_evlute_time);
    	 view.setTag(holder);
    	return view;
    }
     private class  ViewHolder{
    	 TextView tv_pro_name;
    	 RatingBar rb_ratingbar;
    	 TextView tv_evalution_detial;
    	 GridView gv_e_gridview;
    	 TextView tv_evlute_time;
     }
     private String getCStr(Cursor c,String string){
    	return  c.getString(c.getColumnIndex(string));
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
				convertView=View.inflate(mContext, R.layout.pic, null);
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
