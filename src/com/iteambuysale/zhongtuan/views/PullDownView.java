package com.iteambuysale.zhongtuan.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.PullDownListener;

@SuppressLint("ClickableViewAccessibility")
public class PullDownView extends LinearLayout implements OnTouchListener, OnScrollListener {

	View mHeader;
	View mFooter;
	ListView mListView;
	int mStart; // 点击屏幕的初始Y轴位置
	int mStepPoint; // 每次移动的位置
	int mHeadHeight; // 头部高度
	int mFooterHeight; // 脚部高度
	boolean pullAble = true; // 是否可下拉
	boolean load_once = false; // 是否已经初始化
	String mState; // 下拉状态
	PullDownListener mListener;

	public PullDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullDownView(Context context) {
		super(context);
		initView(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !load_once) {
			mHeadHeight = mHeader.getHeight();
			mFooterHeight = mFooter.getHeight();
			updateHeader(0);
			updateFooter(0);
			initListView();
			setOnTouchListener(this);

			load_once = true;
		}
	}
	
	public void setPullDownListener(PullDownListener listener){
		mListener = listener;
	}

	/**
	 * 调整其中的listView
	 */
	private void initListView() {
		// 调整listView的位置
		mListView = (ListView) getChildAt(2);
		removeView(mListView);
		addView(mListView, 1);
		mListView.setOnScrollListener(this);
	}

	/**
	 * 初始化View，引入header和footer
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout headerLayout = new LinearLayout(context);
		LinearLayout footerLayout = new LinearLayout(context);
		headerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		footerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mHeader = inflater.inflate(R.layout.x_block_list_header, headerLayout, true);
		mFooter = inflater.inflate(R.layout.x_block_list_footer, footerLayout, true);
		addView(mHeader);
		addView(mFooter);
	}

	/**
	 * 显示Header
	 * 
	 * @param mDistance2
	 */
	private void updateHeader(int h) {
		MarginLayoutParams lp = (MarginLayoutParams) mHeader.getLayoutParams();
		lp.topMargin = -mHeadHeight + h;
		mHeader.setLayoutParams(lp);
	}
	
	private void updateHeader(int h,String text){
		updateHeader(h);
		TextView tv = (TextView)mHeader.findViewById(R.id.text);
		tv.setText(text);
	}

	/**
	 * 显示footer
	 * 
	 * @param h
	 */
	private void updateFooter(int h) {
		MarginLayoutParams lp = (MarginLayoutParams) mFooter.getLayoutParams();
		lp.bottomMargin = -mFooterHeight + h;
		mFooter.setLayoutParams(lp);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!pullAble) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return action_Down(event);
		case MotionEvent.ACTION_MOVE:
			return action_move(event);
		case MotionEvent.ACTION_UP:
			return action_up(v, event);
		}
		return false;
	}

	/**
	 * 松手事件
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean action_up(View v, MotionEvent event) {
		switch (mState) {
		case D.HALF_PULL:
			updateHeader(0);
			return false;
		case D.FULL_PULL:
			updateHeader(mHeadHeight);
			pullAble = false;
			mListener.onFullPullingDown();
			return false;
		case D.NOMAL:
			return false;
		}
		return false;

	}

	/**
	 * 点击事件
	 * @param event
	 * @return
	 */
	private boolean action_Down(MotionEvent event) {
		return true;
	}

	/**
	 * 移动事件
	 * @param event
	 * @return
	 */
	private boolean action_move(MotionEvent event) {
		int dis = (int) event.getRawY() - mStart; // 距离初始点下位置的距离
		if (dis > 0 && dis < mHeadHeight ) { // 下拉，但是头部没有出来
			mState = D.HALF_PULL;
			updateHeader(dis,"下拉刷新");
		}
		if (dis < 0) { // 用户把header放回原来的位置
			mState = D.NOMAL;
			updateHeader(0);
		}
		if (dis > mHeadHeight) { // 完全拉了出来
			mState = D.FULL_PULL;
			updateHeader(dis,"松手加载更多");
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!pullAble) {
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStart = (int) ev.getRawY();
			mStepPoint = mStart;
			break;
		case MotionEvent.ACTION_MOVE:
			int dis = (int) ev.getRawY() - mStart;
			if ( dis > 0) { // 列表处于可下拉状态，且用户在下拉或者非下拉状态但header未归位
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 恢复列表到初始状态
	 */
	public void initListStatus() {
		mState = D.NOMAL;
		pullAble = true;
		updateHeader(0);
		updateFooter(0);
	}

	/* ================================== on Scroll Listener ====================================== */

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getFirstVisiblePosition() == 0
				&& view.getChildAt(0).getTop() == 0 && !D.FULL_PULL.equals(mState)) {
			pullAble = true;
		} else {
			pullAble = false;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (visibleItemCount+firstVisibleItem==totalItemCount){
		}
	}

}
