package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.model.Feedback;

public class FeedbackAdapter extends SimpleAdapter {
	Context mContext;
	ArrayList<Feedback> mData;
	public FeedbackAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		
	}
	public FeedbackAdapter(Context context,ArrayList<Feedback> data0) {
		super(context, null, R.layout.feedback_item, null, null);
		mContext=context;
		mData=data0;
		Collections.reverse(mData);		
	}
	@Override
	public int getCount() {		
		return mData.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=convertView;
		ViewHolder viewHolder=null;
		if(view==null){
			viewHolder=new ViewHolder();
			view=LayoutInflater.from(mContext).inflate(R.layout.feedback_item, parent, false);
			viewHolder.title=(TextView) view.findViewById(R.id.title);
			viewHolder.fb=(TextView) view.findViewById(R.id.fb);
			viewHolder.time=(TextView) view.findViewById(R.id.time);
			view.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder) view.getTag();
		}
		Feedback fb=mData.get(position);
		String tittle=fb.getTitle();
		if(tittle.length()==0||tittle==null){
			tittle="反馈意见"+(position+1);
		}
		viewHolder.title.setText(tittle);
		viewHolder.fb.setText(fb.getFeedback());
		viewHolder.time.setText(fb.getDateandtime());
		
		return view;
	}
	
	public class ViewHolder{
		TextView title,fb,time;
	}
	



}
