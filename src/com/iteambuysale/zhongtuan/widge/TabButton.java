package com.iteambuysale.zhongtuan.widge;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.iteambuysale.zhongtuan.R;


public class TabButton extends RadioButton {

	public TabButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setChecked(boolean checked) {
		if (checked) {
			this.setTextColor(this.getResources().getColor(R.color.red));
		} else {
			this.setTextColor(this.getResources().getColor(R.color.black_code));
		}
		super.setChecked(checked);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
