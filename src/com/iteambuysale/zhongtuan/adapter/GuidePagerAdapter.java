package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import com.iteambuysale.zhongtuan.activity.specialsale.ProductImagePopWindow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;


public class GuidePagerAdapter extends PagerAdapter {
	private ArrayList<?extends View> viewList;
	private Context context;
	private ProductImagePopWindow pop;
	private PopupWindow popupWindow;
	public GuidePagerAdapter(ArrayList<?extends View> viewList){
		this.viewList = viewList;
	}
	public GuidePagerAdapter(ArrayList<?extends View> viewList,Context context){
		this.viewList = viewList;
		this.context=context;
		pop = new ProductImagePopWindow(context);
		popupWindow = pop.createpopwindow();
	}
	  public void closePopwindow(){
		  popupWindow.dismiss();
	      }
	  public boolean ispopopen(){
		  return popupWindow.isShowing();
	  }
	@Override
	public void destroyItem(View view, int position, Object obj) {
		if(viewList.size()>position&&position>=0){
			
			((ViewPager) view).removeView(viewList.get(position));
		}else{
			((ViewPager) view).removeAllViews();
		}
	}

	@Override
	public Object instantiateItem(View container, final int position) {
	//	((ViewPager) container).addView(viewList.get(position), position);
		((ViewPager) container).addView(viewList.get(position%viewList.size()), 0);            //addView(viewList.get(position), position);
		 final View view=viewList.get(position%viewList.size());
		 if(context!=null){
		 view.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				ArrayList<Drawable> list=new ArrayList<Drawable>();
				pop.showpopwindow();
				for(View view:viewList){
					Drawable drawable = ((ImageView)view).getDrawable();
					list.add(drawable);
				}
				pop.setlist(list);
				//Toast.makeText(context, "点了第"+position+"页", 1).show();
			}
		}); }
		 return view;
	}

   public void setparentPopVIew(View parent){
	   pop.setparents(parent);
   }
	@Override
	public int getCount() {
		if(viewList!=null){
			return viewList.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
								
	
}
