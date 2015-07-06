package com.iteambuysale.zhongtuan.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.login.LoginActivity;
import com.iteambuysale.zhongtuan.activity.me.setting.SettingActivity;
import com.iteambuysale.zhongtuan.adapter.GuidePagerAdapter;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.views.MyPagetransformer;

public class GuideActivity extends SuperActivity {

	ViewPager pager_guide;
	ArrayList<View> viewList;
	LayoutInflater inflater;
	Button guide_button;
	RadioGroup rg_guide;
	View guideView_one, guideView_two, guideView_three, guideView_four,
			guideView_five;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Boolean isHome=getIntent().getBooleanExtra("IsHome", true);
		super.onCreate(savedInstanceState, isHome);
		setContentView(R.layout.guide_main);
		initialized();
//		putProviceTo();
	}
	@Override
	protected void onResume() {
		super.onResume();
		/*AnimationDrawable ad = (AnimationDrawable) guide_button.getBackground();
		ad.setFilterBitmap(false);
		ad.start();*/
	}

	/**
	 * load Views and push them into viewList initialize the button setAdapter
	 */
	private void initialized() {
		pager_guide = (ViewPager) findViewById(R.id.viewPager_guide);
		pager_guide.setPageTransformer(true, new MyPagetransformer());
		viewList = new ArrayList<View>();
		inflater = this.getLayoutInflater();
		loadViews();
		initButton();
		initData();
	}

	private void loadViews() {
		/*guideView_one = inflater.inflate(R.layout.guide_1,
				(ViewGroup) this.pager_guide, false);
		guideView_two = inflater.inflate(R.layout.guide_2,
				(ViewGroup) this.pager_guide, false);
		guideView_three = inflater.inflate(R.layout.guide_3,
				(ViewGroup) this.pager_guide, false);*/
		guideView_four = inflater.inflate(R.layout.guide_4,
				(ViewGroup) this.pager_guide, false);
		guideView_five = inflater.inflate(R.layout.guide_5,
				(ViewGroup) this.pager_guide, false);
		/*viewList.add(guideView_one);
		viewList.add(guideView_two);
		viewList.add(guideView_three);*/
		viewList.add(guideView_four);
		viewList.add(guideView_five);

	}

	private void initButton() {
		guide_button = (Button) guideView_five.findViewById(R.id.guide_button);
		guide_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					boolean isFirstStart = ZhongTuanApp.getInstance()
						.getAppSettings().isFistTime;
				if (isFirstStart) {
					ZhongTuanApp.getInstance().getAppSettings().isFistTime = false;
					ZhongTuanApp.getInstance().persistSave();
					/*Intent intent = new Intent(GuideActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();*/
				} 
					
					Boolean IsFromAbout=getIntent().getBooleanExtra("IsFromAbout",false);
					if(IsFromAbout){
						Intent intent0=new Intent(GuideActivity.this,SettingActivity.class);
						intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent0);
						finish();
						
					}
					else{
						//ZhongTuanApp.getInstance().getAppSettings().isFistTime = false;
						Intent intent = new Intent(GuideActivity.this,
								HomeActivity.class);
						intent.putExtra("tag", D.OPT_ME);
						startActivity(intent);
						finish();
					}
					
					
				//}
			}
		});
	}
	private void initData() {
		pager_guide.setAdapter(new GuidePagerAdapter(viewList));
		pager_guide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// docs[position].setChecked(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}
 
	
//	/**
//	 * 把asset文件夹中的数据库导入到手机中
//	 */
//	private void putProviceTo() {
//		InputStream inputStream = getResources().openRawResource(R.raw.china_province_city_zone);
//		BufferedInputStream bufferin=new BufferedInputStream(inputStream);
//		//String path="/data/data/com.teambuy.zhongtuan/databases/china_province_city_zone.db";
//		String path2 = getFilesDir().getPath();
//		path2=path2.substring(0, path2.lastIndexOf("/"))+"/databases/";
//		OutputStream out; 
//		byte [] buff=new byte [1024];
//		int len;
//		try {
//			out =new FileOutputStream(new File(path2, "china_province_city_zone.db"));
//			while((len=bufferin.read(buff))!=-1){
//				out.write(buff, 0, len);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}
