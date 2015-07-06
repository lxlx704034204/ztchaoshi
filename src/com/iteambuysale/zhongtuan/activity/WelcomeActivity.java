 package com.iteambuysale.zhongtuan.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.login.LoginActivity;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

public class WelcomeActivity extends SuperActivity {
	private ImageView imageview_welcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_main);
		//调试Debug开关（本地）

		LogUtilities.setDebugSwitch(true);
		//腾讯云分析Debug开关
		StatConfig.setDebugEnable(false);
		StatConfig.setAutoExceptionCaught(true);
		//设置腾讯云分析渠道，所有渠道均以D.PACKAGE_XXXX开头
		//StatConfig.setInstallChannel(D.PACKAGE_BAIDU);//百度手机助手
		//StatConfig.setInstallChannel(D.PACKAGE_91);//91手机助手
		StatConfig.setInstallChannel(D.PACKAGE_ANDROID);//安卓市场
		//StatConfig.setInstallChannel(D.PACKAGE_TENCENT);//腾讯应用宝
		//StatConfig.setInstallChannel(D.PACKAGE_360);//360手机助手
		//StatConfig.setInstallChannel(D.PACKAGE_TAOBAO);//淘宝手机助手
		//StatConfig.setInstallChannel(D.PACKAGE_WANDOUJIA);//豌豆荚
		//StatConfig.setInstallChannel(D.PACKAGE_XIAOMI);//小米
		//StatConfig.setInstallChannel(D.PACKAGE_HUAWEI);//华为
		//StatConfig.setInstallChannel(D.PACKAGE_LENOVO);//联想
		//StatConfig.setInstallChannel(D.PACKAGE_ANZHI);//安智
		//StatConfig.setInstallChannel(D.PACKAGE_JIFENG);//机锋
		//StatConfig.setInstallChannel(D.PACKAGE_MEIZU);//魅族
		//StatConfig.setInstallChannel(D.PACKAGE_OPPO);//OPPO
		//StatConfig.setInstallChannel(D.PACKAGE_NDUO);//n多市场
		//StatConfig.setInstallChannel(D.PACKAGE_MUMAYI);//木蚂蚁
		//StatConfig.setInstallChannel(D.PACKAGE_HONGMA);//红码
		//StatConfig.setInstallChannel(D.PACKAGE_TIANYI);//天翼
		//StatConfig.setInstallChannel(D.PACKAGE_YIDONG);//移动mm
		//StatConfig.setInstallChannel(D.PACKAGE_WOSHOP);//沃商店
		
		StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
		 try {
			   // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
			   StatService.startStatService(this, "AHWX7S9N1B8C",
			       com.tencent.stat.common.StatConstants.VERSION);
			 } catch (MtaSDkException e) {
			 }
		imageview_welcome = (ImageView) findViewById(R.id.imageView_welcome);

	}

	@Override
	protected void onStart() {
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
//		imageview_welcome.startAnimation(animation);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				boolean isFirstStart = ZhongTuanApp.getInstance().getAppSettings().isFistTime;
				if (isFirstStart) {
					// 第一次进入
					Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
					startActivity(intent);
				} else /*if ("".equals(ZhongTuanApp.getInstance().getAppSettings().ackToken)) {
					// token 为空（退出登陆状态）
					Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
					startActivity(intent);
				} else */{
					// 已经登陆
					Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
					startActivity(intent);
				}
				finish();

			}
		}, 1000);
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageUtilities.removeBitmaps();
	}
}
