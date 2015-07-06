package com.iteambuysale.zhongtuan.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextWithBorder extends EditText {

	public EditTextWithBorder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditTextWithBorder(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditTextWithBorder(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		if (this.isFocused()){
			paint.setColor(Color.parseColor("#cccccc"));
		}else{
			paint.setColor(Color.parseColor("#cccccc"));
		}
		canvas.drawRoundRect(new RectF(2+this.getScrollX(), 2+this.getScrollY(), this.getWidth()-3, this.getHeight()-3), 3, 3, paint);
		super.onDraw(canvas);
	}

}
