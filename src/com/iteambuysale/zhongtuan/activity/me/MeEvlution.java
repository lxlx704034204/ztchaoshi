package com.iteambuysale.zhongtuan.activity.me;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.base.Basepager;
import com.base.EvaluteListBasePager;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.near.NearEvaluteListPager;
import com.iteambuysale.zhongtuan.activity.specialsale.TemaiEvluteListpager;
import com.iteambuysale.zhongtuan.adapter.EvaluationAdapter;
import com.iteambuysale.zhongtuan.adapter.EvluteAdapter;
import com.iteambuysale.zhongtuan.adapter.MeEvalutonList;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Evaluation;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TeMaiEvaluation;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MeEvlution extends Activity implements NetAsyncListener,
		OnCheckedChangeListener, OnPageChangeListener {
	private TextView title;
	// private ListView evaluteListView;
	private String phoneNumber;
	private Cursor cursor;
	private MeEvalutonList adapter;
	private RadioGroup radiogroup;
	private RadioButton rbtn1;
	private RadioButton rbtn2;
	private ImageView underline;
	private int[] loactions;
	private boolean istemai = true;
	private int downy;
	private int dis;
	private int width;
	private int screenWidth;
	private boolean idFocus = false;
	private int movey;
	private int down;
	private ViewPager vp_list;
	private TemaiEvluteListpager temaipager;
	private NearEvaluteListPager nearpager;
	private boolean istemailoading;
	private boolean istuangouloading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_evalute_all);
		initView();
		initData();
		screenWidth = getScreenWidth();
	}

	private int getScreenWidth() {
		return getWindowManager().getDefaultDisplay().getWidth();
	}

	private void initData() {
		title.setText("我的评论");
		phoneNumber = ZhongTuanApp.getInstance().getAppSettings().phoneNumber;
		cursor = DBUtilities.getrecmByuid(phoneNumber, istemai);
		String[] from = new String[] { "_cpmc", "_dfen", "_recmemo", "_recpic",
				"_dateandtime" };
		int[] to = new int[] { R.id.tv_pro_name, R.id.rb_ratingbar,
				R.id.tv_evalution_detial, R.id.gv_e_gridview,
				R.id.tv_evlute_time };
		adapter = new MeEvalutonList(this, R.layout.listitem_evalution, cursor,
				from, to);
		// evaluteListView.setAdapter(adapter);
		// evaluteListView.setOnTouchListener(this);
		loadmyEvalution(D.API_GET_MY_TEMAI_EVALUTE);
		radiogroup.setOnCheckedChangeListener(this);
		ArrayList<Basepager> vpEvaluteList = new ArrayList<Basepager>();
		temaipager = new TemaiEvluteListpager(this);
		nearpager = new NearEvaluteListPager(this);
		vpEvaluteList.add(temaipager);
		vpEvaluteList.add(nearpager);
		EvluteAdapter vpAdapter = new EvluteAdapter(this, vpEvaluteList);
		//vp_list.setOnTouchListener(this);
		vp_list.setOnPageChangeListener(this);
		vp_list.setAdapter(vpAdapter);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!idFocus) {
			super.onWindowFocusChanged(hasFocus);
			loactions = new int[2];
			rbtn2.getLocationInWindow(loactions);
			LayoutParams params = underline.getLayoutParams();
			width = loactions[0];
			params.width = loactions[0];
			idFocus = true;

		}

	}

	private void initView() {
		title = (TextView) findViewById(R.id.tv_header_tittle);
		vp_list = (ViewPager) findViewById(R.id.vp_evalute_list);
		// vp_list.addView(new EvaluteListBasePager());
		// evaluteListView = (ListView) findViewById(R.id.my_evalute_List);
		radiogroup = (RadioGroup) findViewById(R.id.rg_radiogroup);
		rbtn1 = (RadioButton) findViewById(R.id.rb_rbtn1_ev);
		rbtn2 = (RadioButton) findViewById(R.id.rb_rbtn2_ev);
		underline = (ImageView) findViewById(R.id.iv_underline_red);
	}

	private void loadmyEvalution(String path) {
		NetAsync myEvaAsyn = new NetAsync(path, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				if (istemai) {
					Type type = new TypeToken<TeMaiEvaluation[]>() {
					}.getType();
					TeMaiEvaluation[] evalution = JsonUtilities
							.parseModelByType(elData, type);
					Model.save(evalution);
					return evalution;
				} else {
					Type type = new TypeToken<Evaluation[]>() {
					}.getType();
					Evaluation[] evalution = JsonUtilities.parseModelByType(
							elData, type);
					Model.save(evalution);
					return evalution;
				}
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				// TODO Auto-generated method stub

			}
		};
		myEvaAsyn.execute();
		if(TextUtils.equals(D.API_GET_MY_TEMAI_EVALUTE, path)){
			istemailoading=false;
		}else{
			istuangouloading=false;
		}

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		//Toast.makeText(this, errMsg, 1).show();
		switch (reqUrl) {
		case D.API_GET_MY_TEMAI_EVALUTE:
			istemailoading=false;
			break;
		case D.API_GET_MY_NEAR_EVALUTE:
			istuangouloading=false;
			break;

		default:
			break;
		}

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		/*
		 * cursor = DBUtilities.getrecmByuid(phoneNumber,istemai);
		 * adapter.changeCursor(cursor); adapter.notifyDataSetChanged();
		 */
		if (istemai) {
			istemailoading=false;
			temaipager.notedata();
		} else {
			istuangouloading=false;
			nearpager.notedata();
		}
	}

	@Override
	public void onTokenTimeout() {
		istemailoading=false;
		istuangouloading=false;
		ZhongTuanApp.getInstance().logout(this,true);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_rbtn1_ev:
			istemai = true;
			//underline.startAnimation(setAnimation(loactions[0], 0, 200));
			// System.out.println("1from :"+loactions[0]+",to:"+0);
			cursor = DBUtilities.getrecmByuid(phoneNumber, istemai);
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
			vp_list.setCurrentItem(0, true);
				loadmyEvalution(D.API_GET_MY_TEMAI_EVALUTE);
			break;
		case R.id.rb_rbtn2_ev:
			istemai = false;
			//underline.startAnimation(setAnimation(0, loactions[0], 200));
			// System.out.println("1from :"+0+",to:"+loactions[0]);
			cursor = DBUtilities.getrecmByuid(phoneNumber, istemai);
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
			vp_list.setCurrentItem(1, true);
				loadmyEvalution(D.API_GET_MY_NEAR_EVALUTE);
		default:
			break;
		}

	}

	/*private Animation setAnimation(int fromX, int toX, int time) {
		Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
		animation.setDuration(time);
		animation.setFillAfter(true);
		return animation;
	}*/

/*	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			downy = (int) event.getRawX();
			down = (int) event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			movey = (int) event.getRawX();
			dis = downy - movey;
			if ((dis + underline.getLeft() > 0)
					&& (dis + underline.getRight() < screenWidth)) {
				underline.getLayoutParams();
				underline.layout(dis + underline.getLeft(), underline.getTop(),
						underline.getRight() + dis, underline.getBottom());
			}
			downy = movey;
			break;
		case MotionEvent.ACTION_UP:
			int dis2 = movey - down;
			int time = 200 - 200 / loactions[0] * dis2;
			if (underline.getLeft() > width / 2) {
				underline.startAnimation(setAnimation(underline.getLeft()
						- loactions[0], 0, time));
				underline.layout(loactions[0], underline.getTop(),
						2 * loactions[0], underline.getBottom());
				if (dis > 0) {
					loadmyEvalution(D.API_GET_MY_NEAR_EVALUTE);
					istemai = false;
				}

			} else if (underline.getLeft() < width / 2) {
				underline.startAnimation(setAnimation(underline.getLeft(), 0,
						time));
				underline.layout(0, underline.getTop(), loactions[0],
						underline.getBottom());
				if (dis2 < 0) {
					loadmyEvalution(D.API_GET_MY_TEMAI_EVALUTE);
					istemai = true;
				}
			}
			break;

		default:
			break;
		}
		return false;
	}*/

	@Override
	public void onPageScrollStateChanged(int arg0) {
		switch (arg0) {
		case 0://表示什么都没做，就是停在那。

			break;
		case 1://表示正在滑动
			
			break;
		case 2://表示滑动完毕了
			
			break;
		default:
			break;
		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {//arg0 当前页面，及你点击滑动的页面,//arg1:当前页面偏移的百分比,//arg2:当前页面偏移的像素位置 
		if (arg2>0) {
		//	System.out.println("当前页面偏移的像素位置:"+arg2+"当前页面偏移的百分比:"+arg1);
			underline.layout(arg2/2, underline.getTop(),
					arg2/2+underline.getWidth(), underline.getBottom());
					//System.out.println("左："+arg2/2+",右："+arg2/2+underline.getWidth());
		}

	}

	@Override
	public void onPageSelected(int arg0) {
	switch (arg0) {
	case 0:
		rbtn1.setChecked(true);
		setunderlineparams(0);
		break;
	case 1:
		rbtn2.setChecked(true);
		setunderlineparams(loactions[0]);
		break;

	default:
		break;
	}
	}
	public void setunderlineparams(int x){
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) underline.getLayoutParams();
		params.leftMargin=x;
	}

}
