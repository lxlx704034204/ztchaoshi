package com.iteambuysale.zhongtuan.actor.near;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.model.Category;

public class CategoryActor extends SuperActor {

	View mCurrentView;
	Context mContext;
	Activity mActivity;
	public CategoryActor(Context context, View v) {
		super(context);
		setCurrentView(v);
		mContext = context;
		mActivity = (Activity)context;
		mCurrentView = v;
	}

	public CategoryActor(Context context) {
		super(context);
	}
	
	public void initViews(Category cate,LayoutInflater inflater){
		
		DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
		int density = (int)mContext.getResources().getDisplayMetrics().density;

		List<Category> list =  cate.getSubCategorys();
		for (Category c : list){
			View item_cate = inflater.inflate(R.layout.x_item_cate, $ll("category_content"), false);
			setCurrentView(item_cate);
			$tv("tv_category_name").setText(c.getName());
			$rl("rl_Categories_tags").setVisibility(View.GONE);
			item_cate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggle($rl("rl_Categories_tags"));
					toggle($tv("line"));
				}
			});
			
			setCurrentView(mCurrentView);
			$ll("category_content").addView(item_cate);
			setCurrentView(item_cate);
			
			List<Category> scList = c.getSubCategorys();
			int right = 0;
			int length = widthPixels-40*density;
			int row = 0;
			for (Category sc :scList){
				View tag_cate = inflater.inflate(R.layout.x_block_cate_tag, $rl("rl_Categories_tags"), false);
				setCurrentView(tag_cate);
				TextPaint paint = $tv("tv_category_tag").getPaint();
				float textWidth = paint.measureText(sc.getName());	
				float right_out = right + textWidth+8*density;
				if (right_out > length){
					right = 0;
					row +=1;
				}

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams($tv("tv_category_tag").getLayoutParams());
				lp.setMargins(density*8 + right, density*(4+25*row), 0, 0);
				$tv("tv_category_tag").setLayoutParams(lp);
				right += textWidth+8*density;
				$tv("tv_category_tag").setText(sc.getName());
				setCurrentView(item_cate);
				$rl("rl_Categories_tags").addView(tag_cate);
			}
		}
	}
	
	private void toggle(View view){
		int visible = view.getVisibility();
		if(visible == View.VISIBLE){
			view.setVisibility(View.GONE);
		}else{
			view.setVisibility(View.VISIBLE);
		}
	}
}
