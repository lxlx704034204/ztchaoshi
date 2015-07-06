package com.iteambuysale.zhongtuan.activity.specialsale;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.GuidePagerAdapter;
import com.iteambuysale.zhongtuan.views.ZoomableImageView;

public class ProductImagePopWindow {
	private  Context context;
	private PopupWindow pop;
	private View popView;
	private View parents;
	private ArrayList<ImageView> viewlist;
	private ViewPager vp_popwindow;
	private ViewTreeObserver viewTreeObserver;
	private ZoomableImageView view;

	private static Activity activity;
	public ProductImagePopWindow(Context context){
		this.context=context;
	}
       public PopupWindow createpopwindow(){
    	   popView = View.inflate(context, R.layout.listview_pop,null);
    	   ZhongTuanApp instance = ZhongTuanApp.getInstance();
    	   pop = new PopupWindow(popView, instance.getScreenWidths(), instance.getScreenHeights(),true);
    	   // pop = new PopupWindow(popView, 1080, 1920,true);
    	   pop.setOutsideTouchable(true);
    	   pop.setFocusable(false);
           //设置popwindow如果点击外面区域，便关闭。
    	   pop.setAnimationStyle(R.style.PopupAnimation);
    	   vp_popwindow = (ViewPager) popView.findViewById(R.id.vp_popwindow);
    	
    	   return pop;
       }
       public void showpopwindow(){
    	   pop.showAtLocation(parents, Gravity.CENTER, 0, 0);
    	   pop.update();
       }
       public void setparents(View parents){
    	   this.parents=parents;
       }
	public void setlist(ArrayList<Drawable> list) {
		viewlist=new ArrayList<ImageView>();
		for (Drawable d:list) {
			view = new ZoomableImageView(context);
			view.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			view.setImageDrawable(d);
			viewlist.add(view);
			
		}
	   vp_popwindow.setAdapter(new GuidePagerAdapter(viewlist));
	   
	}
}
