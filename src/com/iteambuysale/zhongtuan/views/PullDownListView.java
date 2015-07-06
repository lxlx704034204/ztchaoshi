package com.iteambuysale.zhongtuan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.iteambuysale.zhongtuan.R;


public class PullDownListView extends ListView {
	View mHeader;
	View mFooter;
	int mHeaderHeight;
	int mFooterHeight;
	boolean loadOnece = false;
	
	public PullDownListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);

	}

	public PullDownListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}


	public PullDownListView(Context context) {
		super(context);
		initView(context);

	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout headerLayout = new LinearLayout(context);
		LinearLayout footerLayout = new LinearLayout(context);
		headerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		footerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		mHeader = inflater.inflate(R.layout.x_block_list_header, headerLayout,true);
		mFooter = inflater.inflate(R.layout.x_block_list_footer, footerLayout,true);
		mHeaderHeight = mHeader.getHeight();
		mFooterHeight = mFooter.getHeight();
		addHeaderView(mHeader);
		addFooterView(mFooter);
//		updateFooter(0);
//		updateHeader(0);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnece){
			updateFooter(0);
			updateHeader(0);
			loadOnece = true;
		}
	}
	
	private void updateHeader(int h){
		mHeader.setPadding(0, -1*mHeaderHeight+h, 0, 0);
		invalidate();
	}
	
	private void updateFooter(int h){
		mFooter.setPadding(0, 0, 0, -1*mFooterHeight+h);
		invalidate();
	}
}
