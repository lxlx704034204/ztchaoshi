package com.iteambuysale.zhongtuan.actor;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.views.CircleImageView;
import com.iteambuysale.zhongtuan.views.PullDownView;

public class SuperActor implements ListAdapter {
	private Context mContext;
	private Activity mActivity;
	private Class<? extends Model> mCurrentClazz;
	private View mView;

	public SuperActor(Context context) {
		mContext = context;
		mActivity = (Activity) context;
	}

	/**
	 * 设置当前view的上下文环境
	 * @param v
	 */
	public void setCurrentView(View v){
		mView = v;
	}
	
	/**
	 * 设置当前model的上下文环境
	 * @param clazz
	 */
	public void setCurrentModel(Class<? extends Model> clazz) {
		mCurrentClazz = clazz;
	}

	/**
	 * 对标题栏进行初始化
	 * 
	 * @param modTitleBar
	 *            显示模式
	 * @param titleName
	 *            标题名称
	 */
	protected void initTitleBar(int modTitleBar, String titleName) {
		switch (modTitleBar) {
		case D.BAR_SHOW_NONE:
			$btn("back").setVisibility(View.GONE);
			$btn("setting").setVisibility(View.GONE);
			break;
		case D.BAR_SHOW_LEFT:
			if($btn("setting")!=null){
				$btn("setting").setVisibility(View.GONE);
			}
			break;
		case D.BAR_SHOW_RIGHT:
			$btn("back").setVisibility(View.GONE);
			$btn("setting").setVisibility(View.VISIBLE);
		default:
			break;
		}
			$tv("tv_header_tittle").setText(titleName);
	}

	/**
	 * 切换View的显示与隐藏
	 * @param views 需要“显示”/“隐藏”的view的id名
	 */
	public void ToggleVisiable(String[] views) {
		for (String v : views) {
			if (toView(v).getVisibility() == View.VISIBLE) {
				toView(v).setVisibility(View.GONE);
			} else {
				toView(v).setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 获得View的id
	 * @param name
	 * @return
	 */
	public int $(String name) {
		return mContext.getResources().getIdentifier(name, "id", mContext.getPackageName());
	}

	/**
	 * 获得View的id
	 * 
	 * @param v
	 * @return
	 */
	public int _(View v) {
		return v.getId();
	}

	/** 通过名字获取各种类型的实例 */
	public Button $btn(String name) {
		return (Button) toView(name);
	}
	
	public ImageButton $Ibtn(String name) {
		return (ImageButton) toView(name);
	}


	public TextView $tv(String name) {
		return (TextView) toView(name);
	}

	public LinearLayout $ll(String name) {
		return (LinearLayout) toView(name);
	}

	public RelativeLayout $rl(String name) {
		return (RelativeLayout) toView(name);
	}

	public CircleImageView $civ(String name) {
		return (CircleImageView) toView(name);
	}
	
	public ImageView $iv(String name){
		return (ImageView)toView(name);
	}

	public EditText $et(String name) {
		return (EditText) toView(name);
	}

	public RadioButton $rb(String name) {
		return (RadioButton) toView(name);
	}

	public RadioGroup $rg(String name) {
		return (RadioGroup) toView(name);
	}

	public ListView $lv(String name) {
		return (ListView) toView(name);
	}

	public PullDownView $pv(String name) {
		return (PullDownView) toView(name);
	}

	public FrameLayout $fl(String name){
		return (FrameLayout)toView(name);
	}
	
	public WebView $wv(String name){
		return (WebView)toView(name);
	}
	
	public View $v(Context context){
		Activity a = (Activity)context;
		return a.getWindow().getDecorView().findViewById(android.R.id.content);
	}
	
	public Spinner $sp(String name){
		return (Spinner)toView(name);
	}
	
	public CheckBox $cb(String name){
		return (CheckBox)toView(name);
	}
	
	public ViewPager $vp(String name){
		return (ViewPager)toView(name);
	}
	
	/**
	 * 取得数据库对应列名
	 * 
	 * @param name 属性名
	 * @return
	 */
	public String _(String name) {
		return Model.getColumnName(mCurrentClazz, name);
	}
	
	public <T extends TextView> String getText(T v) {
		return v.getText().toString();
	}

	public <T extends TextView> String getReversText(T v) {
		return StringUtilities.getReverseString(getText(v));
	}

	public <T extends TextView> String getMd5Text(T v) {
		return StringUtilities.getMD5(getText(v));
	}

	/* ============================== Helpers ============================= */

	/**
	 * 根据名称，获取对应的view
	 * @param name 属性名称
	 * @return
	 */
	private View toView(String name) {
//		int rid = $(name);
//		if (null !=mView){
//			return mView.findViewById(rid);
//		}
//		return mActivity.findViewById(rid);
		View rootView = mActivity.findViewById(android.R.id.content);
		if(null != mView)rootView = mView; 
		return toView(rootView, name);
	}
	
	private View toView(View v,String name){
		int dotPosition = name.indexOf(".");
		if ( -1 == dotPosition )return v.findViewById($(name));
		// 递归取得view
		String remainName = name.substring(dotPosition+1);
		String viewName = name.substring(0, dotPosition);
		View tmpView = v.findViewById($(viewName));
		return toView(tmpView, remainName);
	}

	/* =============================== list Adapter ========================== */
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
