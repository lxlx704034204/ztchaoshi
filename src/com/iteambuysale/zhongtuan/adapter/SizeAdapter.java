package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.model.ProductSize;

public class SizeAdapter  extends BaseAdapter {

	private ArrayList<ProductSize> sizelist;
	private Context context;
	private View [] viewlist;
	private String chima;
	private int beforeposition=0;

	public SizeAdapter(ArrayList<ProductSize> sizelist,Context context) {
		this.sizelist=sizelist;
		this.context=context;
		viewlist =new View[sizelist.size()];
		chima=sizelist.get(0).getChima();
	}

	@Override
	public int getCount() {
		return sizelist.size();
	}

	@Override
	public Object getItem(int position) {
		return sizelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view=View.inflate(context, R.layout.size_num_button, null);
		TextView tv=(TextView) view.findViewById(R.id.tv_size_show);
		tv.setText(sizelist.get(position).getChima());
		tv.setEnabled(false);
	    if(TextUtils.equals(sizelist.get(position).getKcsl(), "0")){
	    	view.setVisibility(View.GONE);
	    }else{
	    	view.setVisibility(View.VISIBLE);
	    }
		 if(0==position){
			 //tv.setEnabled(true);
		 }else{
			 tv.setEnabled(false);
		 }//默认取消
	
		return view;
	}
	
}
