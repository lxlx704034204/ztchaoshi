package com.iteambuysale.zhongtuan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	private int downy;

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean inter = false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downy = (int) ev.getRawY();
		case MotionEvent.ACTION_MOVE:
			int movey=(int) ev.getRawY();
			if (movey - downy > 20) {
                
				 inter=true;
			}
			break;
		case MotionEvent.ACTION_UP:
			int upy = (int) ev.getRawY();
			if (upy - downy > 60) {
				inter = true;
			}
			break;
		default:
			break;
		}
		if (inter) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

}
