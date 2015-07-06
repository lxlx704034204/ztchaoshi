package com.iteambuysale.zhongtuan.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HideItemsAnimation extends Animation {
	
	View view;
	
	public HideItemsAnimation(View view){
		this.view = view;
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int originHeight = view.getLayoutParams().height;
		view.getLayoutParams().height = (int)(originHeight-originHeight*interpolatedTime);
	}

	@Override
	public boolean willChangeBounds() {
		return super.willChangeBounds();
	}
	

}
