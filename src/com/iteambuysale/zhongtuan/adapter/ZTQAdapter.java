package com.iteambuysale.zhongtuan.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ZTQAdapter extends SimpleAdapter  {
	Context context;
	List<HashMap<String,String>> data;
	@SuppressWarnings("unchecked")
	public ZTQAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.context=context;
		this.data=(List<HashMap<String, String>>) data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.ztq_item, parent, false);
			viewHolder=new ViewHolder();
			viewHolder.picIv=(ImageView) convertView.findViewById(R.id.pic);
			viewHolder.ztqNoTv=(TextView) convertView.findViewById(R.id.tv_ztq);
			viewHolder.titleTv=(TextView) convertView.findViewById(R.id.tv_ztqname);
			viewHolder.priceTv=(TextView) convertView.findViewById(R.id.tv_price);
			viewHolder.timeTv=(TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		HashMap<String,String> item=data.get(position);
		ImageUtilities.loadBitMap(item.get("picurl"),viewHolder.picIv);
		viewHolder.ztqNoTv.setText(item.get("qno"));
		String name=item.get("productName");
		viewHolder.titleTv.setText(name);
		viewHolder.priceTv.setText(item.get("price"));
		viewHolder.timeTv.setText(item.get("edate"));
		
		return convertView;
	}
	
	public static class ViewHolder{
		ImageView picIv;
		TextView ztqNoTv,titleTv,priceTv,timeTv;
	}

	


}
