package com.iteambuysale.zhongtuan;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.iteambuysale.zhongtuan.activity.login.LoginActivity;
import com.iteambuysale.zhongtuan.background.DBHelper;
import com.iteambuysale.zhongtuan.define.AppSetting;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;

public class ZhongTuanApp extends Application {
	private static LocationClient mLocationClient = null;
	private static ZhongTuanApp mInstance;
	private AppSetting mAppSettings = null;
	private boolean mConnectState;
	private static DBHelper dbHelper = null;
	private static SQLiteDatabase rdb = null;
	private static SQLiteDatabase wdb = null;
	public static String cityName;
	private static ArrayList<Activity> mActivityStack = null;
	private static SQLiteDatabase pcdDB=null;
	private static Map<String, String> gvheight;
	private Intent intent;
	private static LruCache<String, Bitmap> memorycahe; 


	public SQLiteDatabase getPcdDB() {
		return pcdDB;
	}

	@Override
	public void onCreate() {
		mInstance = this;
		initialized();
		super.onCreate();
		
		
	}
	
	@Override
	public void onTerminate() {
		if (null != mLocationClient) {
			mLocationClient.stop();
			mLocationClient = null;
		}
		if (null != dbHelper) {
			rdb.close();
			wdb.close();
			dbHelper.close();
		}
		super.onTerminate();
	}

	/* init methods */
	private void initialized() {
		// 初始化db
		if (null == dbHelper)dbHelper = new DBHelper(this, D.DB_VERSION);

		try {
			rdb = dbHelper.getWritableDatabase();
			wdb = dbHelper.getReadableDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String path2 = getApplicationContext().getFilesDir().getPath();
		path2=path2.substring(0, path2.lastIndexOf("/"))+"/databases/";
		putProviceTo();
		putimagrfromassats();
		File file=new File(path2, D.DB_NAME_PROVINCE);
    	pcdDB = SQLiteDatabase.openOrCreateDatabase(file,null); 

		// 初始化百度定位
		mLocationClient = getmLocationClient();
		BDLocationListener locationListener =  new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				ZhongTuanApp.getInstance().setLocation(location);
				mLocationClient.stop();
			}
		};
		mLocationClient.registerLocationListener(locationListener);
		mLocationClient.start();
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		ZhongTuanApp.cityName = cityName;
	}

	/**
	 *  获取appSetting 对象
	 * @return Appsetting obj
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public AppSetting getAppSettings() {
		if (null == mAppSettings) {
			mAppSettings = new AppSetting();
			persistLoad();
		}
		return this.mAppSettings;
	}

	/** 
	 * 获取Application对象
	 * @return Application obj
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public static ZhongTuanApp getInstance() {
		return mInstance;
	}

	/**
	 *  获取定位Client
	 * @return locationClient
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public LocationClient getmLocationClient() {
		if (null == mLocationClient) {
			SDKInitializer.initialize(getApplicationContext());
			mLocationClient = new LocationClient(getInstance()); 	// 声明LocationClient类
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Battery_Saving);	// 设置定位模式
			option.setCoorType("bd09ll");							// 返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(20000);								// 设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(true);							// 返回的定位结果包含地址信息
			option.setNeedDeviceDirect(false);						// 返回的定位结果包含手机机头的方向
			mLocationClient.setLocOption(option);
		}
		return mLocationClient;
	}
	
	public LocationClient getLocationClient(){
		if(mLocationClient==null){
			mLocationClient=getmLocationClient();
			mLocationClient.start();
		}
		return mLocationClient;
	}

	/**
	 *  获取网络链接状态
	 * @return true if networkState is Connected; vice versa
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public Boolean getNetWorkState() {
		updateNetworkState();
		return this.mConnectState;
	}

	/**
	 *  获取session
	 * @return
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public String getSession() {
		return getAppSettings().session;
	}

	/**
	 *  保存session
	 * @param session
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public void saveSession(String session) {
		if (!"".equals(session)) {
			getAppSettings().session = session;
			persistSave();
		}
	}

	/**
	 *  保存定位信息
	 * @param location
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public void setLocation(BDLocation location) {
		//TODO: 去掉两次查询操作，优化成只保存定位原始信息，cityID和cityCode等在需要的时候再查询使用。
		String city = location.getCity();
		if (null != city) {
			cityName=city;
			getAppSettings().cityCode = DBUtilities.getcityCodeByName(getApplicationContext(),city);
			getAppSettings().cityId = DBUtilities.getcityIdByName(city);
		}
		getAppSettings().lat = String.valueOf(location.getLatitude());
		getAppSettings().lgn = String.valueOf(location.getLongitude());
		persistSave();
	}

	// 获取纬度
	public String getLngo() {
		return getAppSettings().lgn;
	}

	// 获取经度
	public String getLato() {
		return getAppSettings().lat;
	}

	// 获取城市id
	public String getCityId() {
		return getAppSettings().cityId;
	}

	// 获取城市编号
	public String getCityCode() {
		return getAppSettings().cityCode;
	}

	// load本地数据
	public void persistLoad() {
		SharedPreferences prefers = this.getSharedPreferences(
				D.PREFERENCE_NAME, MODE_PRIVATE);
		mAppSettings.persistGet(prefers);
	}

	// 存本地数据
	public void persistSave() {
		SharedPreferences prefers = this.getSharedPreferences(
				D.PREFERENCE_NAME, MODE_PRIVATE);
		mAppSettings.persistSave(prefers);
	}

	// 清本地数据
	public void persistClear() {
		SharedPreferences prefers = this.getSharedPreferences(
				D.PREFERENCE_NAME, MODE_PRIVATE);
		mAppSettings.persistClear(prefers);
	}
	
	// 获取屏幕density参数
	public  int getDensity(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.densityDpi/160;
	}
	
	// 获取屏幕宽度 dip
	public int getScreenWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels/metrics.densityDpi;
	}
	//获得屏幕的=宽度的像素
	public int getScreenWidthPixels(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	public int getScreenWidths(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
		 
	}
	public int getScreenHeights(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
		
	}
	
//	/**
//	 * Activity 入栈
//	 * @param aty
//	 * @author Anddward.Liao <Anddward@gmail.com>
//	 */
//	public void pushActivity(Activity aty){
//		mActivityStack  = mActivityStack == null?new ArrayList<Activity>():mActivityStack;
//		mActivityStack.add(aty);
//	}
//	
//	/**
//	 * Activity 清栈
//	 * @author Anddward.Liao <Anddward@gmail.com>
//	 */
//	public void pureActivityStack(){
//		if(null == mActivityStack)return;
//		if(mActivityStack.isEmpty())return;
//		for(Activity aty : mActivityStack){
//			if(null != aty)aty.finish();
//		}
//	}

	/**
	 *  退出登录
	 * @param context
	 * @author Anddward.Liao <Anddward@gmail.com>
	 * 已改：2015-3-7 lforxeverc
	 */
	public void logout(Context context,boolean goLoginActivity) {
		mAppSettings = getAppSettings();
		mAppSettings.ackToken = "";
		mAppSettings.uid = "";
		persistSave();
		mAppSettings = null;
		if (null != context) {
			if(goLoginActivity){
				intent=getintent(context);
				intent.putExtra("isHome", false);
				/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
				
				context.startActivity(intent);
			}
//			pureActivityStack();
		}
	}
	public void logout(Context context) {
		mAppSettings = getAppSettings();
		mAppSettings.ackToken = "";
		mAppSettings.uid = "";
		persistSave();
		mAppSettings = null;
		if (null != context) {
				intent=getintent(context);
				intent.putExtra("isHome", false);
				intent.putExtra("isfromme", true);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
//			pureActivityStack();
		}
	}
	private Intent getintent(Context context ){
		if(intent==null){
			intent  =new Intent(context, LoginActivity.class);
		}
		return intent;
	}

	// 获取读数据库
	public SQLiteDatabase getRDB() {
		return rdb;
	}

	// 获取写数据库
	public SQLiteDatabase getWDB() {
		return wdb;
	}

	/**
	 *  更新网络状态 
	 *  @author Anddward.Liao <Anddward@gmail.com>
	 */
	private void updateNetworkState() {
		boolean connected = false;
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (State.CONNECTED == state) {
			connected = true;
		}
		if (!connected) {
			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();
			if (State.CONNECTED == state) {
				connected = true;
			}
		}
		mConnectState = connected;
	}


	// 选择城市进行定位
	public void setLocation() {
		getmLocationClient().stop();
		ZhongTuanApp zhongTuanApp=getInstance();
		zhongTuanApp.getAppSettings().cityCode = DBUtilities
				.getcityCodeByName(getApplicationContext(),ZhongTuanApp.cityName);
		zhongTuanApp.getAppSettings().cityId = DBUtilities
				.getcityIdByName(ZhongTuanApp.cityName);
		zhongTuanApp.getAppSettings().cityName = ZhongTuanApp.cityName;
		SharedPreferences prefers = this.getSharedPreferences(
				D.PREFERENCE_NAME, MODE_PRIVATE);
		Editor editor = prefers.edit();
		editor.putString("cityId", getAppSettings().cityId);
		editor.putString("cityCode", getAppSettings().cityCode);
		editor.putString("cityName", getAppSettings().cityName);
		editor.commit();
	}
	/**
	 * 把asset文件夹中的数据库导入到手机中
	 */
	private void putProviceTo() {
		InputStream inputStream = getResources().openRawResource(R.raw.china_province_city_zone);
		BufferedInputStream bufferin=new BufferedInputStream(inputStream);
		String path2 = getFilesDir().getPath();
		path2=path2.substring(0, path2.lastIndexOf("/"))+"/databases/";
		OutputStream out; 
		byte [] buff=new byte [1024];
		int len;
		try {
			File file=new File(path2, D.DB_NAME_PROVINCE);
			if(!file.exists())
			{
				file.createNewFile(); 
			}
			out =new FileOutputStream(file);
			while((len=bufferin.read(buff))!=-1){
				out.write(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 把assets文件夹中的图片放在缓存中
	 */
	private void putimagrfromassats(){
		try {
			InputStream inputStream = getResources().getAssets().open("ic.png");
			FileUtilities.saveImage(inputStream, "ic.png");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private static void createLurCache() {
		int size =(int) Runtime.getRuntime().maxMemory();
		memorycahe =new LruCache<String, Bitmap>(size/16){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
		
	}
	public static LruCache<String , Bitmap> getLruCache(){
		if(memorycahe==null){
			createLurCache();
		}
			return memorycahe;
	}
	
}