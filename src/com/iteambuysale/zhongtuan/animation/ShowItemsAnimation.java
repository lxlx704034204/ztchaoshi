package com.iteambuysale.zhongtuan.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ShowItemsAnimation extends Animation {
	
	View view;
	int finalHeight;
	
	public ShowItemsAnimation(View view,int finalHeight){
		this.view = view;
		this.finalHeight = finalHeight;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		view.getLayoutParams().height = (int)(interpolatedTime*finalHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return super.willChangeBounds();
	}
	

}
