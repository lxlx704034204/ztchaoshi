package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.SuperActivity;
import com.iteambuysale.zhongtuan.activity.login.LoginActivity;
import com.iteambuysale.zhongtuan.activity.near.EvaluationListActivity;
import com.iteambuysale.zhongtuan.actor.HomeActor;
import com.iteambuysale.zhongtuan.actor.near.ProductActor;
import com.iteambuysale.zhongtuan.adapter.GuidePagerAdapter;
import com.iteambuysale.zhongtuan.adapter.ProductEvaluationAdapter;
import com.iteambuysale.zhongtuan.adapter.SellListAdapter;
import com.iteambuysale.zhongtuan.adapter.SizeAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.fragment.home.SaleFragmentVersion2;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.near.ProductListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ProductImgs;
import com.iteambuysale.zhongtuan.model.ProductSize;
import com.iteambuysale.zhongtuan.model.ShopMsg;
import com.iteambuysale.zhongtuan.model.TeMaiEvaluation;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class ProductActivity extends SuperActivity implements ProductListener,
		OnClickListener, NetAsyncListener, OnItemClickListener,
		OnPageChangeListener {

	CustomProgressDialog progressDialog;
	String pid;
	private Cursor productList;
	private ViewPager vp_picturl_product;
	private TextView tv_check_color_catgory;
	private TextView tv_photo_word_detial;
	private TextView tv_product_canshu;
	private TextView tv_product_detail_canshu;
	private TextView tv_product_evalute;
	private GridView gv_sale_lady_weinituijian;
	private Button bt_buyat_once;
	private Button bt_addto_factory;
	private ImageView iv_cpcs;
	private boolean toggle = true;
	private ImageView iv_detail_return;
	private TextView tv_product_detail_introduce;
	private String productId;
	private String shopId;
	private String beforPrice;
	private String nowprice;
	private TextView tv_shopname;
	private ListView lv_show_evalu;
	private ProductEvaluationAdapter evlutadapter;
	private Cursor evaluationCursor;
	private SQLiteDatabase db;
	private ScrollView sv_product_tetial;
	private TextView tv_price;
	private TextView tv_new_product_now_price;
	private boolean iscollection = false;
	private GuidePagerAdapter adapter;
	private ArrayList<ImageView> viewList = new ArrayList<ImageView>();
	private LinearLayout ll_dot_img_num;
	private int currentpage = 0;
	private TextView tv_product_param;
	private GridView gv_size_catgory;
	private String productSize = null;
	private SizeAdapter sizeadapter;
	private ImageView imgine;

	private LruCache<String, Bitmap> memorycahe;
	private String[] imgs;
	private int vpheight;
	private int vpwidth;
	private boolean traggle = false;
	private ImageView iv_color_catgory;
	private Map<String, NetAsync> asyncMap;
	protected String tmcid;
	private static final int NO_SIZE = 0;
	private static final int iS_LOADING = 1;
	private static final int HAS_SIZE = 2;
	private final static float TARGET_HEAP_UTILIZATION = 0.75f;
	private int haschima = 0;
	private String firstPicturl;
	private String buytimes;
	private ImageView iv_call;
	private TextView tv_tuanbi_price;
	private String stbmoney = " ";
	private String productTitle;
	private Button bt_product_to_shopcart;
	private HomeActor mActor;
	private int TAG_SALE=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, false);
		setContentView(R.layout.near_product);
		progressDialog = CustomProgressDialog.createDialog(this);
		// dalvik.system
		// VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		setMinHeapSize(30 * 1024 * 1024);
		asyncMap = new HashMap<String, NetAsync>();
		productId = (String) getIntent().getCharSequenceExtra("productId");
		productId = productId.split(",")[0];
		productList = DBUtilities.getProductList(productId);
		initview();
		loadProductsImgs();// 加载viewpager的图片
		showProductParamter();// 加载产品的参数
		if (productList.moveToFirst()) {
			initdata();
			loadEvaluation(D.API_SPECIAL_ORDER_GETTMRECM);
		}
		//checkiscollection();
		memorycahe = ZhongTuanApp.getLruCache();
		// createLurCache();

	}

	/**
	 * 写一个图片的缓存
	 */
	/*
	 * private void createLurCache() { int size =(int)
	 * Runtime.getRuntime().maxMemory(); memorycahe =new LruCache<String,
	 * Bitmap>(size/16){
	 * 
	 * @Override protected int sizeOf(String key, Bitmap value) { // TODO
	 * Auto-generated method stub return value.getRowBytes()*value.getHeight();
	 * } };
	 * 
	 * }
	 */
	/**
	 * 查看此商品是否已加入收藏
	 */
	/*private void checkiscollection() {

		NetAsync collectionAsync = new NetAsync(D.API_CHECK_COLLECT, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("uflb", "cpmx-tm"));
				params.add(new BasicNameValuePair("lbid", productId));

			}
		};
		asyncMap.put(D.API_CHECK_COLLECT, collectionAsync);
		// add(D.API_CHECK_COLLECT,collectionAsync);
		collectionAsync.execute();
	}
*/
	private void initdata() {
		db = ZhongTuanApp.getInstance().getRDB();
		evaluationCursor = db
				.rawQuery(
						"select * from TEMAIEVALUATION_LIST where _shopid=? and _cpid=? order by _dateandtime desc limit 0 ,2 ",
						new String[] { shopId, productId });

		evlutadapter = new ProductEvaluationAdapter(evaluationCursor, this);
		int count = evaluationCursor.getCount();
		if (count > 0) {
			lv_show_evalu.getLayoutParams().height = measureAdapterHeight(evlutadapter);
			lv_show_evalu.setAdapter(evlutadapter);
		}
		// 为你推荐
		String[] from = new String[] { "_picurl", "_title", "_tmword", "_dj0",
				"_tmdj", "_tbmoney" };
		int[] to = new int[] { R.id.iv_item_imageload,
				R.id.tv_item_product_title, R.id.tv_item_product_introduce,
				R.id.tv_item_beforeprice, R.id.tv_item_nowprice, R.id.iv_sendtb };
		Cursor tjcursor = db.rawQuery(
				"select * from TEMAI_LIST_VERSON order by _collects limit 0,4",
				null);
		SellListAdapter selladapter = new SellListAdapter(this, tjcursor, from,
				to, null, null);
		gv_sale_lady_weinituijian.getLayoutParams().height = SaleFragmentVersion2
				.measureAdapterviewheight(this);
		gv_sale_lady_weinituijian.setAdapter(selladapter);
		gv_sale_lady_weinituijian.setOnItemClickListener(this);
		loadshopProduct();
	}

	private int measureAdapterHeight(ProductEvaluationAdapter adapter) {
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View view = adapter.getView(i, null, lv_show_evalu);
			view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			view.measure(0, 0);
			totalHeight += view.getMeasuredHeight();
		}
		return totalHeight;
	}

	/**
	 * 加载评价
	 */
	private void loadEvaluation(String url) {
		NetAsync loadEvaluation = new NetAsync(url, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TeMaiEvaluation[]>() {
				}.getType();
				TeMaiEvaluation[] evaluation = JsonUtilities.parseModelByType(
						elData, type);
				Model.save(evaluation);
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("shopid", shopId));
				params.add(new BasicNameValuePair("cpid", productId));
				params.add(new BasicNameValuePair("page", "0"));

			}
		};
		asyncMap.put(url, loadEvaluation);
		// asynclist.add(loadEvaluation);
		loadEvaluation.execute();
	}

	private void initview() {
		vp_picturl_product = (ViewPager) findViewById(R.id.vp_picturl_product);
		iv_detail_return = (ImageView) findViewById(R.id.iv_detail_return);
		tv_price = (TextView) findViewById(R.id.tv_price);
		sv_product_tetial = (ScrollView) findViewById(R.id.sv_product_tetial);
		tv_price.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// setFlags(Paint.
		tv_new_product_now_price = (TextView) findViewById(R.id.tv_new_product_now_price);

		tv_check_color_catgory = (TextView) findViewById(R.id.tv_check_color_catgory);// 颜色分类

		tv_photo_word_detial = (TextView) findViewById(R.id.tv_photo_word_detial);//

		tv_product_canshu = (TextView) findViewById(R.id.tv_product_canshu);

		tv_product_detail_canshu = (TextView) findViewById(R.id.tv_product_detail_canshu);
		iv_cpcs = (ImageView) findViewById(R.id.iv_cpcs);
		tv_product_evalute = (TextView) findViewById(R.id.tv_product_evalute);
		gv_sale_lady_weinituijian = (GridView) findViewById(R.id.gv_sale_lady_weinituijian);
		bt_buyat_once = (Button) findViewById(R.id.bt_buyat_once);
		bt_addto_factory = (Button) findViewById(R.id.bt_addto_factory);
		tv_product_detail_introduce = (TextView) findViewById(R.id.tv_product_detail_introduce);
		tv_shopname = (TextView) findViewById(R.id.tv_shopname);
		ll_dot_img_num = (LinearLayout) findViewById(R.id.ll_dot_img_num);
		tv_product_param = (TextView) findViewById(R.id.tv_product_detail_canshu);
		tv_tuanbi_price = (TextView) findViewById(R.id.tv_tuanbi_price);

		iv_call = (ImageView) findViewById(R.id.iv_call);
		iv_detail_return.setOnClickListener(this);
		vp_picturl_product.setOnPageChangeListener(this);
		// vp_picturl_product.setOnClickListener(this);
		gv_size_catgory = (GridView) findViewById(R.id.gv_size_catgory);
		iv_color_catgory = (ImageView) findViewById(R.id.iv_color_catgory);
		// gv_size_catgory.setSelector(null);
		ImageView iv_share = (ImageView) findViewById(R.id.iv_share_tm);
		bt_product_to_shopcart=(Button) findViewById(R.id.bt_product_to_shopcart);
		bt_product_to_shopcart.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_call.setOnClickListener(this);
		if (productList.moveToFirst()) {
			showproductMsg();
		} else {// 重新从网上加载数据
			loadaproductMx(productId);
		}
		tv_price.setText("￥" + beforPrice);
		tv_new_product_now_price.setText("￥" + nowprice);
		/*
		 */
		lv_show_evalu = (ListView) findViewById(R.id.lv_show_evalu);

	}

	private void loadaproductMx(String productId2) {
		NetAsync loadaNetAsync = new NetAsync(D.API_SPECIAL_GETATEMAI, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2>() {
				}.getType();
				TemaiVerson2 temai = JsonUtilities.parseModelByType(elData,
						type);
				Model.save(new TemaiVerson2[] { temai });
				return temai;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));

			}
		};
		asyncMap.put(D.API_SPECIAL_GETATEMAI, loadaNetAsync);
		// asynclist.add(loadaNetAsync);
		loadaNetAsync.execute();
	}

	/**
	 * 加入购物车
	 */
	public void addtocart(String url) {
		NetAsync asyncaddtocard = new NetAsync(url, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				/*
				 * Type type = new TypeToken<TeMaiEvaluation[]>() { }.getType();
				 * TeMaiEvaluation[] evaluation =
				 * JsonUtilities.parseModelByType( elData, type);
				 * Model.save(evaluation);
				 */
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));
				params.add(new BasicNameValuePair("tmsl", "1"));
				params.add(new BasicNameValuePair("cmid", tmcid));
				params.add(new BasicNameValuePair("btimes", buytimes));// 限购次数
				params.add(new BasicNameValuePair("bnums", buytimes));// 限购数量

			}
		};
		asyncaddtocard.execute();
	}

	public void loadshopProduct() {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_GETSHOP, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ShopMsg>() {
				}.getType();
				ShopMsg shopmsg = JsonUtilities.parseModelByType(elData, type);
				Model.save(new ShopMsg[] { shopmsg });
				return shopmsg;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("shopid", shopId));
			}
		};
		asyncMap.put(D.API_SPECIAL_GETSHOP, task_loadTemai);
		// asynclist.add(task_loadTemai);
		task_loadTemai.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_check_color_catgory:
			if (traggle) {
				gv_size_catgory.setVisibility(View.VISIBLE);
				iv_color_catgory
						.setImageResource(R.drawable.brand_more_selected);
				traggle = false;
			} else {
				gv_size_catgory.setVisibility(View.GONE);
				iv_color_catgory.setImageResource(R.drawable.brand_more);
				traggle = true;
			}
			break;
		case R.id.tv_photo_word_detial:
			Intent intent2 = new Intent(getApplicationContext(),
					PhotoWordDtetial.class);
			intent2.putExtra("shopId", shopId);
			intent2.putExtra("productId", productId);
			intent2.putExtra("beforPrice", beforPrice);
			intent2.putExtra("nowprice", nowprice);
			startActivity(intent2);
			break;
		case R.id.tv_product_canshu:
			if (toggle) {
				tv_product_detail_canshu.setVisibility(View.VISIBLE);
				iv_cpcs.setImageResource(R.drawable.brand_more_selected);
				toggle = false;
			} else {
				tv_product_detail_canshu.setVisibility(View.GONE);
				iv_cpcs.setImageResource(R.drawable.brand_more);
				toggle = true;
			}
			break;
		case R.id.tv_product_evalute:
			Intent intent1 = new Intent(getApplicationContext(),
					EvaluationListActivity.class);
			intent1.putExtra("cpid", productId);
			intent1.putExtra("shop", shopId);
			intent1.putExtra("istemai", true);
			startActivity(intent1);
			break;
		case R.id.bt_buyat_once:
		
			if(ensurelogin()){
				return ;
			};
			if (haschima != NO_SIZE && TextUtils.isEmpty(tmcid)) {
				if (haschima == iS_LOADING) {
					Toast.makeText(this, "正在加载尺码信息，请稍后", 1).show();

				} else {
					Toast.makeText(this, "您还没有选择尺码", 1).show();
				}
				return;
			}
			if (TextUtils.equals(buytimes, "0")) {
				Intent intent = new Intent(getApplicationContext(),
						SpecialSaleBuyAtOnce.class);
				intent.putExtra("productId", productId);
				intent.putExtra("chima", productSize);
				intent.putExtra("tmcid", tmcid);
				startActivity(intent);
			} else {

				loadcanbuy();
			}
			/*
			 * Intent intent = new Intent(getApplicationContext(),
			 * SpecialSaleBuyAtOnce.class); intent.putExtra("productId",
			 * productId); intent.putExtra("chima", productSize);
			 * intent.putExtra("tmcid", tmcid); startActivity(intent);
			 */// TODO
			break;
		case R.id.bt_addto_factory:
			if(ensurelogin()){
				return ;
			};
			if (haschima != NO_SIZE && TextUtils.isEmpty(tmcid)) {
				if (haschima == iS_LOADING) {
					Toast.makeText(this, "正在加载尺码信息，请稍后", 1).show();

				} else {
					Toast.makeText(this, "您还没有选择尺码", 1).show();
				}
				return;
			}
			//if (TextUtils.equals(buytimes, "0")) {
				addtocart(D.API_SPECIAL_ADD_TO_CART);
			//}
			/*
			 * if (!iscollection) { addtofactory(D.API_ADD_COLLECT);
			 * Toast.makeText(getBaseContext(), "加入收藏成功", Toast.LENGTH_SHORT)
			 * .show(); bt_addto_factory.setText("取消收藏"); iscollection = true;
			 * 
			 * } else {
			 * 
			 * addtofactory(D.API_CANCEL_COLLECT);
			 * Toast.makeText(getBaseContext(), "您已取消收藏此产品",
			 * Toast.LENGTH_SHORT).show(); bt_addto_factory.setText("加入收藏");
			 * iscollection = false; }
			 */
			break;
		case R.id.iv_detail_return:
			iv_detail_return.setAlpha((float) 0.25);
			finish();
			break;
		case R.id.tv_shopname:
			Intent intent3 = new Intent(getApplication(), SpecialShop.class);
			intent3.putExtra("shopId", shopId);
			startActivity(intent3);
			break;
		case R.id.iv_share_tm:
			share();
			break;

		case R.id.iv_call:
			Intent intent = new Intent(this, SpecialShop.class);
			intent.putExtra("shopId", shopId);
			intent.putExtra("showleft", true);
			startActivity(intent);
			break;
		case R.id.bt_product_to_shopcart:

			
			Intent intents=new Intent(this, HomeActivity.class);
			mActor = HomeActivity.getactor();
			mActor.changeTab(2);
			startActivity(intents);
			finish();
			break;
		default:
			break;
		}

	}
   
	private boolean ensurelogin(){
		/*ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(10);
		for(RunningTaskInfo info :runningTasks){
			System.out.println(info.hashCode());//;
		}
		System.out.println("-----------------");*/
		String uid = ZhongTuanApp.getInstance()
				.getAppSettings().uid;
		if (TextUtils.isEmpty(uid)) {
			/*ZhongTuanApp.getInstance().getAppSettings().isFistTime = false;
			ZhongTuanApp.getInstance().persistSave();*/
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			intent.putExtra("isHome", false);
			startActivity(intent);
			return true;
		} else{
			return false;
		}
	}
	private void loadcanbuy() {
		progressDialog.show();
		NetAsync collectTask = new NetAsync(D.API_SPECIAL_BUY_NUM, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));
				params.add(new BasicNameValuePair("buytimes", buytimes));
			}
		};
		// asynclist.add(collectTask);
		collectTask.execute();

	}

	private void share() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://app.teambuy.com.cn/tmdetail.php?id="
				+ productId);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("【中团特卖】http://app.teambuy.com.cn/tmdetail.php?id="
				+ productId);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(FileUtilities.getImagePath() + getimagename());// 确保SDcard下面存在此张图片
		oks.setUrl("http://app.teambuy.com.cn/tmdetail.php?id=" + productId);
		// oks.setImageUrl(firstPicturl);
		// url仅在微信（包括好友和朋友圈）中使用
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("中团APP下载链接  \n想省钱，找中团");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("中团APP下载链接  \n想省钱，找中团");
		// 启动分享GUI
		oks.show(this);
	}

	private String getimagename() {
		// System.out.println("picturlname:"+firstPicturl.substring(firstPicturl.lastIndexOf("/")+1));
		return firstPicturl.substring((firstPicturl.lastIndexOf("/") + 1));

	}

	/*
	 * public void addtofactory(final String path) { final SharedPreferences pre
	 * = getSharedPreferences( "zhongtuan_preference", Context.MODE_PRIVATE);
	 * NetAsync collectTask = new NetAsync(path, this) {
	 * 
	 * @Override public Object processDataInBackground(JsonElement elData) {
	 * return elData; }
	 * 
	 * @Override public void beforeRequestInBackground(List<NameValuePair>
	 * params) { params.add(new BasicNameValuePair("uflb", "cpmx-tm"));
	 * params.add(new BasicNameValuePair("lbid", productId)); params.add(new
	 * BasicNameValuePair("lngo", pre.getString("lgn", ""))); params.add(new
	 * BasicNameValuePair("lato", pre.getString("lat", ""))); } };
	 * asyncMap.put(path, collectTask); // asynclist.add(collectTask);
	 * collectTask.execute();
	 * 
	 * }
	 */

	public void loadProductsImgs() {
		NetAsync productAsyn = new NetAsync(D.API_SPECIAL_GET_PRODUCT_IMGS,
				this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ProductImgs>() {
				}.getType();
				ProductImgs productimgs = JsonUtilities.parseModelByType(
						elData, type);
				return productimgs;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));

			}
		};
		asyncMap.put(D.API_SPECIAL_GET_PRODUCT_IMGS, productAsyn);
		// asynclist.add(productAsyn);
		productAsyn.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_SPECIAL_GET_PRODUCT_CHIMA:
			haschima = NO_SIZE;
			break;
		case D.API_SPECIAL_BUY_NUM:
			Toast.makeText(this, errMsg, 0).show();
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		default:
			break;
		}
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		NetAsync netAsync;
		switch (reqUrl) {
		case D.API_SPECIAL_GETSHOP:
			asyncCancel(D.API_SPECIAL_GETSHOP);
			break;
		case D.API_SPECIAL_ORDER_GETTMRECM:
			evaluationCursor = db
					.rawQuery(
							"select * from TEMAIEVALUATION_LIST where _shopid=? and _cpid=? order by _dateandtime desc limit 0 ,2 ",
							new String[] { shopId, productId });
			evlutadapter = new ProductEvaluationAdapter(evaluationCursor, this);
			lv_show_evalu.getLayoutParams().height = measureAdapterHeight(evlutadapter);
			lv_show_evalu.setAdapter(evlutadapter);
			sv_product_tetial.scrollTo(0, 0);
			asyncCancel(D.API_SPECIAL_ORDER_GETTMRECM);
			break;
		case D.API_SPECIAL_GETATEMAI:
			// TemaiVerson2 temai = (TemaiVerson2) data;
			productList = DBUtilities.getProductList(productId);
			if (productList.moveToFirst()) {
				showproductMsg();
				initdata();
			}
			sv_product_tetial.scrollTo(0, 0);
			asyncCancel(D.API_SPECIAL_GETATEMAI);
			break;
		/*case D.API_CHECK_COLLECT:
			iscollection = true;
			bt_addto_factory.setText("取消收藏");
			sv_product_tetial.scrollTo(0, 0);
			asyncCancel(D.API_CHECK_COLLECT);
			break;*/
		case D.API_SPECIAL_GET_PRODUCT_IMGS:
			ProductImgs productimgs = (ProductImgs) data;
			String img = productimgs.getPhotos();
			String param = productimgs.getParam();
			String utbmoney = productimgs.getUtbmoney();
			stbmoney = productimgs.getStbmoney();
			if (TextUtils.isEmpty(stbmoney) || TextUtils.equals(stbmoney, "0")) {
				stbmoney = " ";
			} else {
				stbmoney = ",送" + stbmoney + "团币";
			}
			tv_product_detail_introduce.setText(productTitle + stbmoney);
			if (TextUtils.isEmpty(utbmoney) || TextUtils.equals(utbmoney, "0")) {
				tv_tuanbi_price.setVisibility(View.GONE);
			} else {
				tv_tuanbi_price.setText("可使用团币:" + productimgs.getUtbmoney());
				tv_tuanbi_price.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(param)) {
				tv_product_param.setText(param);
			}
			setviewpager(img);
			asyncCancel(D.API_SPECIAL_GET_PRODUCT_IMGS);
			break;
		case D.API_SPECIAL_BUY_NUM:
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Intent intent = new Intent(getApplicationContext(),
					SpecialSaleBuyAtOnce.class);
			intent.putExtra("productId", productId);
			intent.putExtra("chima", productSize);
			intent.putExtra("tmcid", tmcid);
			startActivity(intent);
			break;
		case D.API_SPECIAL_GET_PRODUCT_CHIMA:
			haschima = HAS_SIZE;
			final ArrayList<ProductSize> sizelist = (ArrayList<ProductSize>) data;
			// productSize = sizelist.get(0).getChima();取消默认
			// tmcid = sizelist.get(0).getTmcid();
			sizeadapter = new SizeAdapter(sizelist, this);
			gv_size_catgory.setVisibility(View.VISIBLE);
			gv_size_catgory.setAdapter(sizeadapter);
			setcolorcatagoryheight(sizelist);
			gv_size_catgory.setOnItemClickListener(new OnItemClickListener() {
      
				private int beforeition = 0;

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					sizelist.get(position);
					gv_size_catgory.getChildAt(position)
							.findViewById(R.id.tv_size_show).setEnabled(true);
					if (position != beforeition) {
						gv_size_catgory.getChildAt(beforeition)
								.findViewById(R.id.tv_size_show)
								.setEnabled(false);
					}
					beforeition = position;
					productSize = sizelist.get(position).getChima();
					tmcid = sizelist.get(position).getTmcid();
				}
			});
			asyncCancel(D.API_SPECIAL_GET_PRODUCT_CHIMA);
			break;
		case D.API_SPECIAL_ADD_TO_CART:
			Toast.makeText(this, "加入购物车成功", 0).show();
			break;
		default:
			break;
		}

	}

	public void asyncCancel(String url) {
		NetAsync netAsync;
		netAsync = asyncMap.get(url);
		if(netAsync==null){return; }
		netAsync.cancel(true);
		asyncMap.remove(url);
	}

	private void setcolorcatagoryheight(ArrayList<ProductSize> sizelist) {
		// View view =gv_size_catgory.getChildAt(0);
		View view = View.inflate(this, R.layout.size_num_button, null);
		view.measure(0, 0);
		// view.getHeight();
		LayoutParams params = gv_size_catgory.getLayoutParams();
		int verheight = ZhongTuanApp.getInstance().getDensity() * 10;
		// int verheight=gv_size_catgory.getVerticalSpacing();
		int height = (sizelist.size() % 3 == 0 ? sizelist.size() / 3
				: (sizelist.size() / 3) + 1)
				* (view.getMeasuredHeight() + verheight) + 3 * verheight;
		params.height = height;
	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) { int
	 * height=gv_size_catgory.getLayoutParams().height;
	 * gv_size_catgory.getLayoutParams().height=height+2;
	 * super.onWindowFocusChanged(hasFocus); }
	 */

	private void setviewpager(String img) {
		imgs = img.split("\\|\\|");
		String imgine = new String();
		viewList.clear();
		viewList.add(this.imgine);
		ll_dot_img_num.removeAllViews();
		ll_dot_img_num.addView(getdotimg(0, this));
		vpheight = vp_picturl_product.getHeight();
		vpwidth = vp_picturl_product.getWidth();
		// System.out.println(height);
		// System.out.println(width);
		for (int i = 1; i <= imgs.length; i++) {
			imgine = imgs[i - 1].split("\\|")[0];
			// System.out.println(imgine);
			if (!TextUtils.isEmpty(imgine)) {
				ImageView view = new ImageView(this);
				view.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				// ImageUtilities.loadBitMap(imgine, view,width,height);
				ImageUtilities.loadBitMap(imgine, view, null, vpwidth,
						vpheight, memorycahe);
				viewList.add(view);
				ll_dot_img_num.addView(getdotimg(i, this));
			}
		}
		adapter.notifyDataSetChanged();
		ll_dot_img_num.getChildAt(0).setEnabled(true);

	}

	private void showProductParamter() {
		NetAsync paramtersAsync = new NetAsync(D.API_SPECIAL_GET_PRODUCT_CHIMA,
				this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ArrayList<ProductSize>>() {
				}.getType();
				ArrayList<ProductSize> sizeList = JsonUtilities
						.parseModelByType(elData, type);
				return sizeList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));

			}
		};
		haschima = iS_LOADING;
		asyncMap.put(D.API_SPECIAL_GET_PRODUCT_CHIMA, paramtersAsync);
		// asynclist.add(paramtersAsync);
		paramtersAsync.execute();
	}

	/**
	 * 给页面加载数据
	 */
	private void showproductMsg() {
		tv_check_color_catgory.setOnClickListener(this);
		tv_photo_word_detial.setOnClickListener(this);
		tv_product_canshu.setOnClickListener(this);
		tv_product_evalute.setOnClickListener(this);
		bt_buyat_once.setOnClickListener(this);
		bt_addto_factory.setOnClickListener(this);
		tv_shopname.setOnClickListener(this);
		productTitle = productList.getString(productList
				.getColumnIndex("_title"));
		tv_product_detail_introduce.setText(productTitle + stbmoney);
		shopId = productList.getString(productList.getColumnIndex("_shopid"));
		beforPrice = productList.getString(productList.getColumnIndex("_dj0"));
		nowprice = productList.getString(productList.getColumnIndex("_tmdj"));
		firstPicturl = productList.getString(productList
				.getColumnIndex("_picurl"));
		buytimes = productList
				.getString(productList.getColumnIndex("_buynums"));
		imgine = new ImageView(this);
		imgine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		/*
		 * ImageUtilities.loadBitMap(
		 * productList.getString(productList.getColumnIndex("_picurl")),
		 * imgine);
		 */
		measureVpwidth();
		ImageUtilities.loadBitMap(firstPicturl, imgine, null, vpwidth,
				vpheight, memorycahe);
		// TODO
		viewList.add(imgine);
		adapter = new GuidePagerAdapter(viewList, this);
		adapter.setparentPopVIew(vp_picturl_product);
		vp_picturl_product.setAdapter(adapter);
		ll_dot_img_num.addView(getdotimg(0, this));
		tv_price.setText("￥" + beforPrice);
		tv_new_product_now_price.setText("￥" + nowprice);
	}

	private void measureVpwidth() {
		ZhongTuanApp tuanApp = ZhongTuanApp.getInstance();
		vpwidth = tuanApp.getScreenWidths();
		vpheight = 335 * tuanApp.getDensity();
		// System.out.println("width:"+vpwidth+",vpheght:"+vpheight);
	}

	/**
	 * 画一个圆点
	 */
	public static ImageView getdotimg(int i, Context context) {
		ImageView dot = new ImageView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		if (i != 0) {

			params.leftMargin = ZhongTuanApp.getInstance().getDensity() * 5;
			dot.setEnabled(false);
		} else {
			dot.setEnabled(true);

		}
		dot.setLayoutParams(params);
		dot.setImageResource(R.drawable.point_bg);
		return dot;
	}

	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(this,false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ProductActivity.class);
		intent.putExtra("productId", id + "");
		startActivity(intent);
		finish();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		ll_dot_img_num.getChildAt(currentpage).setEnabled(false);
		ll_dot_img_num.getChildAt(position).setEnabled(true);
		currentpage = position;

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_IDLE:
			// ImageUtilities.loadBitMap(memorycahe,
			// imgs[currentpage].split("\\|")[0], viewList.get(currentpage));
			if (currentpage != 0) {
				if (memorycahe.get(imgs[currentpage].split("\\|")[0]) != null) {
					// System.out.println("from lurcache!" );
					viewList.get(currentpage).setImageBitmap(
							memorycahe.get(imgs[currentpage].split("\\|")[0]));
				} else {
					ImageUtilities.loadBitMap(
							imgs[currentpage].split("\\|")[0],
							viewList.get(currentpage), null, vpwidth, vpheight,
							memorycahe);
					// System.out.println("from net");
				}
			}

			break;

		default:
			break;
		}
	}

	public static void setMinHeapSize(long size) {
		try {
			Class<?> cls = Class.forName("dalvik.system.VMRuntime");
			Method getRuntime = cls.getMethod("getRuntime");
			Object obj = getRuntime.invoke(null);// obj就是Runtime
			if (obj == null) {
				System.err.println("obj is null");
			} else {
				// System.out.println(obj.getClass().getName());
				Class<?> runtimeClass = obj.getClass();
				Method setMinimumHeapSize = runtimeClass.getMethod(
						"setMinimumHeapSize", long.class);
				setMinimumHeapSize.invoke(obj, size);
				Method setTargetHeapUtilization = runtimeClass.getMethod(
						"setTargetHeapUtilization", Float.class);
				setTargetHeapUtilization.invoke(obj, TARGET_HEAP_UTILIZATION);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && adapter != null
				&& adapter.ispopopen()) {
			adapter.closePopwindow();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		memorycahe.evictAll();
		productList.close();
		for (Entry<String, NetAsync> entry : asyncMap.entrySet()) {
			entry.getValue().cancel(true);
		}
		System.gc();
		finish();
		super.onDestroy();
	}
}
