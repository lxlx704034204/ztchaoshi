package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import com.base.Basepager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class EvluteAdapter extends PagerAdapter {
      private Context context;
	private ArrayList<Basepager> list;



	public EvluteAdapter(Context context,ArrayList<Basepager> list){
    	  this.context=context;
    	  this.list=list;
      }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(list.get(position).getRootview());
	}



	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager)container).addView(list.get(position).getRootview());
		return list.get(position).getRootview();
	}



	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
  
}
