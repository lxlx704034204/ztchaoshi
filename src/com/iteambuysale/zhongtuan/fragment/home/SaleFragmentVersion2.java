package com.iteambuysale.zhongtuan.fragment.home;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.specialsale.LadyCatgory;
import com.iteambuysale.zhongtuan.activity.specialsale.MoreCategoryLoad;
import com.iteambuysale.zhongtuan.activity.specialsale.ProductActivity;
import com.iteambuysale.zhongtuan.activity.specialsale.SaleMainSearch;
import com.iteambuysale.zhongtuan.activity.specialsale.ShowProductV2;
import com.iteambuysale.zhongtuan.activity.specialsale.SpecialShop;
import com.iteambuysale.zhongtuan.activity.specialsale.TMAdvShow;
import com.iteambuysale.zhongtuan.adapter.GuidePagerAdapter;
import com.iteambuysale.zhongtuan.adapter.SellListAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Spl_sale_adv_msg;
import com.iteambuysale.zhongtuan.model.Spl_sale_v2_adv;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.FixedSpeedScroller;

public class SaleFragmentVersion2 extends Fragment implements
		OnPageChangeListener, OnTouchListener, NetAsyncListener,
		OnItemClickListener, OnClickListener {

	protected static final int PAGECHANGE = 0;
	private static final String MIDDLE_ADV = "cxzad";
	private static final String BUTTON_ADV = "zspad";
	private static final String TOP_ADV = "topad";
	private static final String LARGE_LIST = "largelist";
	private static final String SMALL_LIST = "smalllist";
	private static final String TM_SHOP = "tmshop";
	private static final String TM_ITEM = "tmitem";
	private static final String URL = "url";
	private static int itemHeight;
	private ViewPager vp_adver;
	private ArrayList<View> adsLists = new ArrayList<View>();
	private ArrayList<ImageView> dotlist1 = new ArrayList<ImageView>();
	private int beforposition = 0;
	private int beforepage = -1;
	private String[] from = new String[] { "_picurl", "_title","_tmword", "_dj0", "_tmdj","_tbmoney" };
	private int[] to = new int[] { R.id.iv_item_imageload,R.id.tv_item_product_title,
			R.id.tv_item_product_introduce, R.id.tv_item_beforeprice,
			R.id.tv_item_nowprice ,R.id.iv_sendtb};
	private SellListAdapter adapter;
	private int freshHeader;
	private TextView tv_refresh_show;
	private int page = 0;
	private ArrayList<TextView> midTvList;
	private int i = 0;
	private LinearLayout ll_dots;
	private LruCache<String, Bitmap> memorycahe;
	private ImageView iv_search;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor cur = DBUtilities.getSplAdvMsg(TOP_ADV, null);
		addAdvertisments(cur);
		addDottoList(cur);
		int size =(int) Runtime.getRuntime().maxMemory();
		memorycahe =new LruCache<String, Bitmap>(size/8){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};

	}
  
	/**
	 * 把所有的点放入到集合中便于管理
	 * 
	 * @param cur
	 */
	public void addDottoList(Cursor cur) {
		dotlist1.clear();
		for (int i = 0; i < cur.getCount(); i++) {
			ImageView img = getdotimg(getActivity());
			dotlist1.add(img);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// View view =View.inflate(get, R.layout.sale_fragment_v2, null);
		View view = View.inflate(getActivity(), R.layout.sale_fragment_v2, null);//(R.layout.sale_fragment_v2, null);
		vp_adver = (ViewPager) view.findViewById(R.id.vp_adver);
		/*
		 * dot1 = (ImageView) view.findViewById(R.id.iv_dot1_v2); dot2 =
		 * (ImageView) view.findViewById(R.id.iv_dot2_v2); dot3 = (ImageView)
		 * view.findViewById(R.id.iv_dot3_v2); dot4 = (ImageView)
		 * view.findViewById(R.id.iv_dot4_v2); dotlist1.clear();
		 * dotlist1.add(dot1); dotlist1.add(dot2); dotlist1.add(dot3);
		 * dotlist1.add(dot4); dot1.setEnabled(false); dot2.setEnabled(false);
		 * dot3.setEnabled(false); dot4.setEnabled(false);
		 */
		ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
		rightTitle = (ImageView) view.findViewById(R.id.iv_left_title);
		rightTitle.setVisibility(View.VISIBLE);
		iv_search=(ImageView)view.findViewById(R.id.iv_right_title);
		ll_half_price = (LinearLayout) view.findViewById(R.id.ll_half_price);
		ll_buyone_sendone = (LinearLayout) view
				.findViewById(R.id.ll_buyone_sendone);
		ll_tweenty_nine_nine = (LinearLayout) view
				.findViewById(R.id.ll_tweenty_nine_nine);
		ll_special_select = (LinearLayout) view
				.findViewById(R.id.ll_special_select);
		/*
		 * llList = new ArrayList<LinearLayout>(); llList.add(ll_half_price);
		 * llList.add(ll_buyone_sendone); llList.add(ll_tweenty_nine_nine);
		 * llList.add(ll_special_select);
		 */
		midImgList = new ArrayList<ImageView>();
		midTvList = new ArrayList<TextView>();
		ImageView iv_today_half_price = (ImageView) view
				.findViewById(R.id.iv_today_half_price);
		TextView tv_today_half_price = (TextView) view
				.findViewById(R.id.tv_today_half_price);
		midImgList.add(iv_today_half_price);
		midTvList.add(tv_today_half_price);
		ImageView iv_buy_one_send_one = (ImageView) view
				.findViewById(R.id.iv_buy_one_send_one);
		TextView tv_buy_one_send_one = (TextView)view
				.findViewById(R.id.tv_buy_one_send_one);
		midImgList.add(iv_buy_one_send_one);
		midTvList.add(tv_buy_one_send_one);
		ImageView iv_tweenty_nine_nine = (ImageView) view
				.findViewById(R.id.iv_tweenty_nine_nine);
		TextView tv_tweenty_nine_nine = (TextView) view
				.findViewById(R.id.tv_tweenty_nine_nine);
		midImgList.add(iv_tweenty_nine_nine);
		midTvList.add(tv_tweenty_nine_nine);
		ImageView iv_special_sale_select = (ImageView) view
				.findViewById(R.id.iv_special_sale_select);
		TextView tv_special_sale_select = (TextView) view
				.findViewById(R.id.tv_special_sale_select);
		midImgList.add(iv_special_sale_select);
		midTvList.add(tv_special_sale_select);
		iv_sale_adv_part1 = (ImageView) view
				.findViewById(R.id.iv_sale_adv_part1);
		iv_sale_adv_part2 = (ImageView) view
				.findViewById(R.id.iv_sale_adv_part2);
		iv_sale_adv_part3 = (ImageView) view
				.findViewById(R.id.iv_sale_adv_part3);
		iv_sale_adv_part4 = (ImageView) view
				.findViewById(R.id.iv_sale_adv_part4);
		imgList = new ArrayList<ImageView>();
		imgList.add(iv_sale_adv_part1);
		imgList.add(iv_sale_adv_part2);
		imgList.add(iv_sale_adv_part3);
		imgList.add(iv_sale_adv_part4);
		ll_sale_v2 = (LinearLayout) view.findViewById(R.id.ll_sale_v2);
		gv_gridview_tm = (GridView) view.findViewById(R.id.gv_gridview_tm);
		scroll = (ScrollView) view.findViewById(R.id.sv_version2);
		rl_refresh = (RelativeLayout) view.findViewById(R.id.rl_refresh);
		iv_anim1 = (ImageView) view.findViewById(R.id.zhuanjuhua);
		footerview = (LinearLayout) view.findViewById(R.id.ll_footer);
		// ll_fouritem = (LinearLayout) view.findViewById(R.id.ll_fouritem);
		iv_anim = (ImageView) view.findViewById(R.id.iv_specail_anim);
		tv_refresh_show = (TextView) view.findViewById(R.id.tv_refresh_show);
		scroll.setVerticalScrollBarEnabled(false);
		scroll.setOnTouchListener(new myonTouchListener());
		//gv_gridview_tm.setOnScrollListener(this);
		/*if(!isfristloadadv){
			handleadvershow();
		}*/
		return view;
	}

	private boolean isrefresh = false;
	private FixedSpeedScroller mScroller;
	private int gridViewheight;
	private boolean isnowloadmore = false;
	private boolean isfristloadadv = true;

	private class myonTouchListener implements OnTouchListener {

		private boolean isfirstgetlocation = true;
		private int rllocation;
		private int locations;
		private boolean isfirstdown = true;
		private int downy;
		private int dis;
		private int[] locs = new int[2];

		// private int locsy;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isfirstgetlocation) {
				rllocation = getlocation(vp_adver);
				locations = rllocation;
				isfirstgetlocation = false;
			}
			int locations2 = 0;
			// System.out.println("event:"+event.getAction());
			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				int movey = (int) event.getRawY();
				if (isfirstdown) {
					downy = movey;
					isfirstdown = false;
					locations2 = getlocation(vp_adver);
				}
				locations = getlocation(vp_adver);
				dis = movey - downy;
				if (locations >= rllocation && !isrefresh) {
					/*
					 * if (locations2<locations) { dis = movey - downy; }else{
					 * dis = movey - downy; }
					 */
					// if (dis > 0 && dis < rl_height && dis < rl_height * 1.2)
					// {
					if (dis > 0 && dis < freshHeader * 2) {
						startanim();
						ll_sale_v2.setPadding(
								0,
								-freshHeader
										+ MeFragment.getscrolldis(dis,
												freshHeader * 2), 0, 0);
						if (dis > freshHeader * 1.3) {
							tv_refresh_show.setText("释放刷新");
						} else {
							tv_refresh_show.setText("下拉刷新");
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				isfirstdown = true;
				if (locations2 + freshHeader >= rllocation) {
					if (locations > rllocation) {
						if (dis > freshHeader) {
							tv_refresh_show.setText("正在刷新中");
							// ll_refresh.setPadding(0, -rl_height, 0, 0);
							ll_sale_v2.setPadding(0,
									(int) (-freshHeader * 0.3), 0, 0);
							isrefresh = true;
							if (!ZhongTuanApp.getInstance().getNetWorkState()) {
								ll_sale_v2.setPadding(0, -freshHeader, 0, 0);
								isrefresh = false;
							}
							dorefresh();
							// loadCatgoryproduct();
						} else {
							ll_sale_v2.setPadding(0, -freshHeader, 0, 0);
							isrefresh = false;
						}
					}
				} else {
					ll_sale_v2.setPadding(0, -freshHeader, 0, 0);
					isrefresh = false;
				}
				// gridview 的预加载
				gv_loadmore();
				detetctFlyaction();// 处理scrollview的快速滑动事件
				break;
			default:
				break;
			}
			return false;
		}

		public void gv_loadmore() {
			gv_gridview_tm.getLocationOnScreen(locs);
			// System.out.println("locationy:"+locs+",height:"+gv_gridview_tm.getHeight());
			if (dis < 0 && locs[1] < 0
					&& (locs[1] + gridViewheight) < (itemHeight * 4)
					&& !isnowloadmore && beforepage < page) {
				loadSellProduct(page);
				isnowloadmore = true;
				beforepage++;
				// Toast.makeText(getActivity(), "加载页数："+page, 1).show();

			}
		}

		private void detetctFlyaction() {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					gv_loadmore();
				}
			}, 1000);

		}

	}

	private void dorefresh() {
		page = 0;
		beforepage = -1;
		isnowloadmore = false;
		footerview.setVisibility(View.VISIBLE);
		loadSellProduct(page);
		Model.delete(Spl_sale_v2_adv.class);//Spl_sale_v2_adv
		loadAdverPicturl();
	}

	private int getlocation(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[1];
	}

	public void startanim() {
		iv_anim.setBackgroundResource(R.anim.special_sale_refresh);
		AnimationDrawable animationdrawable = (AnimationDrawable) iv_anim
				.getBackground();
		animationdrawable.start();
	}

	@Override
	public void onStart() {
		super.onStart();
		initdata();
		myhandle.postDelayed(new Chengeviewpager(), 3);
	}

	private void initdata() {
		adapter2 = new GuidePagerAdapter(adsLists);
		vp_adver.setAdapter(adapter2);
		vp_adver.setCurrentItem(beforposition);
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(getActivity(),
					new AccelerateInterpolator());
			mField.set(vp_adver, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}

		vp_adver.setOnPageChangeListener(this);
		vp_adver.setOnTouchListener(this);
		rightTitle.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		initGridviewadapter();// 初始化gridview的数据，并联网请求数据
		setviewlistener();
		measureLinearHeight();
		ll_sale_v2.setPadding(0, -freshHeader, 0, 0);

		// System.out.println("执行了："+i+++"次");
		if (isfristloadadv) {
			handleadvershow();
			loadAdverPicturl();
			isfristloadadv = false;
		}
		 else {
			handleadvershow();// 处理广告栏的显示
		}

	}

	/**
	 * 把点加入到线性布局里面
	 */
	public void adddotToLinearLayout() {
		ll_dots.removeAllViews();
		for (ImageView img : dotlist1) {
			ll_dots.addView(img);
		}
	}

	public void startanimm() {
		iv_anim1.setBackgroundResource(R.anim.special_sale_loadmore_v2);
		AnimationDrawable animationdrawable = (AnimationDrawable) iv_anim1
				.getBackground();
		animationdrawable.start();
	}

	/**
	 * 测量下拉刷新的高度 product_footer_v2
	 */
	private void measureLinearHeight() {
		rl_refresh.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		rl_refresh.measure(0, 0);
		freshHeader = rl_refresh.getMeasuredHeight();

	}

	/**
	 * 把广告的图像加入到adapter要使用的集合中
	 * 
	 * @param cur
	 */
	private void addAdvertisments(Cursor cur) {
		adsLists.clear();
		while (cur.moveToNext()) {
			if(getActivity()==null){
				return;
			}
			ImageView img = new ImageView(getActivity());
			img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			ImageUtilities.loadBitMap(
					cur.getString(cur.getColumnIndex("_picturl")), img);
			adsLists.add(img);
			// for(ImageView img:dotlist1){//把点加入到线性布局里面
			// ll_dots.addView(dotlist1.get(i));
			// }

		}
		// }
	}

	private void initGridviewadapter() {
		Cursor c = DBUtilities.getTeamVersonList();
		int count = c.getCount();
		adapter = new SellListAdapter(getActivity(), c, from, to, null,memorycahe);
		gv_gridview_tm.setAdapter(adapter);
		gv_gridview_tm.setFocusable(false);
		gv_gridview_tm.setSelector(R.color.transparent);
		gridViewheight = count % 2 == 0 ? count / 2
				* measureAdapterviewheight(getActivity()) : (count / 2 + 1)
				* measureAdapterviewheight(getActivity());
		gv_gridview_tm.getLayoutParams().height = (gridViewheight);
		gv_gridview_tm.setOnItemClickListener(this);
		loadSellProduct(page);
		startanimm();

	}

	/**
	 * @param context
	 * @return 测量gridview的item的高度
	 */
	public static int measureAdapterviewheight(Context context) {
		if(context !=null){
		
		View view = View.inflate(context, R.layout.shop_product_adapterview,
				null);
		view.measure(0, 0);
		itemHeight = view.getMeasuredHeight();
		}
		return itemHeight;
	}

	// 加载特卖商品列表
	private void loadSellProduct(final int page) {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_SELL, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2[]>() {
				}.getType();
				TemaiVerson2[] temaiList = JsonUtilities.parseModelByType(
						elData, type);
				if (page == 0) {
					Model.delete(TemaiVerson2.class);
				}
				Model.save(temaiList);
				return temaiList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_TM_PAGE, page + ""));
			}
		};
		task_loadTemai.execute();
	}

	private void loadAdverPicturl() {
		NetAsync adverAsync = new NetAsync(D.TM_ADVER_PIC, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Spl_sale_adv_msg>() {
				}.getType();
				Spl_sale_adv_msg adv_msg = JsonUtilities.parseModelByType(
						elData, type);
				ArrayList<String[]> list_cxzad = adv_msg.getAdv().get("cxzad");
				ArrayList<String[]> list_zspad = adv_msg.getAdv().get("zspad");
				ArrayList<String[]> list_topad = adv_msg.getAdv().get("topad");
				savecxzad(list_cxzad);
				savecxzad(list_zspad);
				savecxzad(list_topad);
				
				// savezspad(list_zspad);
				return adv_msg;
			}

			/*
			 * private void savezspad(ArrayList<String[]> list_zspad) {
			 * Spl_sale_v2_adv [] list =new Spl_sale_v2_adv[list_zspad.size()];
			 * for (int i=0;i<list_zspad.size();i++){ Spl_sale_v2_adv adv=new
			 * Spl_sale_v2_adv(); String [] strs=list_zspad.get(i);
			 * adv.setPicturl(strs[0]); adv.setReqType(strs[1]);
			 * adv.setReqdata(strs[2]); adv.setReqname(strs[3]);
			 * adv.setAdvtype(strs[4]); adv.setId(strs[5]); //
			 * adv.setId("zspad"+i); list[i]=adv; } Model.save(list); }
			 */

			private void savecxzad(ArrayList<String[]> list_cxzad) {
				Spl_sale_v2_adv[] list = new Spl_sale_v2_adv[list_cxzad.size()];
				for (int i = 0; i < list_cxzad.size(); i++) {
					Spl_sale_v2_adv adv = new Spl_sale_v2_adv();
					String[] strs = list_cxzad.get(i);
					adv.setPicturl(strs[0]);
					adv.setReqType(strs[1]);
					adv.setReqdata(strs[2]);
					adv.setReqname(strs[3]);
					adv.setAdvtype(strs[4]);
					adv.setId(strs[5]);
					// adv.setId("cxzad"+i);
					list[i] = adv;
				}
				Model.save(list);
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("pagea", "tmindex"));

			}
		};
		adverAsync.execute();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		dotlist1.get(arg0).setEnabled(true);
		dotlist1.get(beforposition).setEnabled(false);
		beforposition = arg0;

	}

	private boolean rightroll = true;
	Handler myhandle = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PAGECHANGE:
				if (beforposition + 1 == adsLists.size() || beforposition == 0) {
					rightroll = !rightroll;
				}
				if (rightroll) {
					vp_adver.setCurrentItem((beforposition - 1));
					/*
					 * dotlist.get(beforposition).setEnabled(true);
					 * dotlist.get(beforposition+1).setEnabled(false);
					 */
				} else {
					vp_adver.setCurrentItem(beforposition + 1);
					/*
					 * dotlist.get(beforposition).setEnabled(true);
					 * dotlist.get(beforposition-1).setEnabled(false);
					 */
				}
				mScroller.setmDuration(600);
				myhandle.removeCallbacksAndMessages(null);
				postDelayed(new Chengeviewpager(), 3000);
				break;

			default:
				break;
			}
		};
	};
	private GridView gv_gridview_tm;
	private ImageView iv_sale_adv_part1;
	private ImageView iv_sale_adv_part2;
	private ImageView iv_sale_adv_part3;
	private ImageView iv_sale_adv_part4;
	private LinearLayout ll_half_price;
	private LinearLayout ll_buyone_sendone;
	private LinearLayout ll_tweenty_nine_nine;
	private LinearLayout ll_special_select;
	private ImageView rightTitle;
	private LinearLayout ll_sale_v2;
	private RelativeLayout rl_refresh;
	// private LinearLayout ll_fouritem;
	private ImageView iv_anim;
	private ImageView iv_anim1;
	private ScrollView scroll;
	private LinearLayout footerview;
	private ImageView dot1;
	private ImageView dot2;
	private ImageView dot3;
	private ImageView dot4;
	private ArrayList<ImageView> imgList;
	private ArrayList<ImageView> midImgList;
	private GuidePagerAdapter adapter2;

	// private ArrayList<LinearLayout > llList;
	private class Chengeviewpager implements Runnable {

		@Override
		public void run() {
			myhandle.sendEmptyMessage(PAGECHANGE);
		}

	}
     int downX=0;
	//private int midpicHeight=0;
	//private int midpicwidth=0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX=(int) event.getRawX();
			myhandle.removeCallbacksAndMessages(null);
			break;
		case MotionEvent.ACTION_UP:// intent =new Intent(getActivity(),
									// TMAdvShow.class);
			Intent intent = new Intent(getActivity(), TMAdvShow.class);
			
			//Cursor cursor = DBUtilities.getSplAdvMsg(TOP_ADV, null);
			/*if (cursor.moveToPosition(beforposition)) {
				String url = cursor
						.getString(cursor.getColumnIndex("_reqdata"));
				String[] urls = url.split("\\|");
				if (urls.length > 1) {
					intent.putExtra("url", urls[1]);
					System.out.println(url);
					startActivity(intent);
				}
			}*/
			//TODO
			if (Math.abs(event.getRawX()-downX)<25) {
				
				handleReqdata(TOP_ADV, beforposition+"");
			}
			myhandle.postDelayed(new Chengeviewpager(), 3000);
			//cursor.close();
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_SPECIAL_SELL:
			Cursor c = DBUtilities.getTeamVersonList();
			int count = c.getCount();
			int position = gv_gridview_tm.getFirstVisiblePosition();
			gridViewheight = count % 2 == 0 ? count / 2
					* measureAdapterviewheight(getActivity()) : (count / 2 + 1)
					* measureAdapterviewheight(getActivity());
			gv_gridview_tm.getLayoutParams().height = (gridViewheight);
			adapter.changeCursor(c);
			adapter.notifyDataSetChanged();
			gv_gridview_tm.setSelection(position);
			ll_sale_v2.setPadding(0, -freshHeader, 0, 0);
			isrefresh = false;
			isnowloadmore = false;
			beforepage = page;
			page++;
			if (c.getCount() < D.LOAD_EVERY_PAGE_NUMBER) {
				footerview.setVisibility(View.GONE);
			}
			break;
		case D.TM_ADVER_PIC:
			// Spl_sale_v2_adv advmsg=(Spl_sale_v2_adv) data;
			handleadvershow();
		default:
			break;
		}

	}

	/**
	 * 处理返回的广告栏的信息
	 */
	private void handleadvershow() {
		String picturl, name, pic;
		Cursor cursor = null, cur = null,sor=null;
		for (int i = 0; i < midImgList.size(); i++) {// 中间广告栏的处理
			cursor = DBUtilities.getSplAdvMsg(MIDDLE_ADV, i + "");
			if (cursor.moveToFirst()) {
				picturl = cursor.getString(cursor.getColumnIndex("_picturl"));
				name = cursor.getString(cursor.getColumnIndex("_reqname"));
					ImageUtilities.loadBitMap(picturl, midImgList.get(i));
				midTvList.get(i).setText(name);
			}
		}

		for (int i = 0; i < imgList.size(); i++) {// 底部广告栏的处理
			cur = DBUtilities.getSplAdvMsg(BUTTON_ADV, i + "");
			if (cur.moveToFirst()) {

				pic = cur.getString(cur.getColumnIndex("_picturl"));
				ImageUtilities.loadBitMap(pic, imgList.get(i));
			}
		}
		sor = DBUtilities.getSplAdvMsg(TOP_ADV, null);
		//cur.getCount();
		// while(cur.moveToNext()){
		addAdvertisments(sor);
		addDottoList(sor);
		adddotToLinearLayout();
		
		adapter2.notifyDataSetChanged();
		vp_adver.setCurrentItem(beforposition);

		// }
		cur.close();
		cursor.close();
		sor.close();

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_SPECIAL_SELL:
			ll_sale_v2.setPadding(0, -freshHeader, 0, 0);
			isrefresh = false;
			isnowloadmore = false;
			footerview.setVisibility(View.GONE);
			//Toast.makeText(getActivity(), errMsg, 0).show();
			break;

		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		rl_refresh.setPadding(0, -freshHeader, 0, 0);
		isnowloadmore = false;
		footerview.setVisibility(View.GONE);
		// Toast.makeText(getActivity(), "连接超时", 0).show();
		ZhongTuanApp.getInstance().logout(getActivity(),false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (Model.isRecoredExists(TemaiVerson2.class, id + "")) {
			Intent intent = new Intent(getActivity(), ProductActivity.class);
			intent.putExtra("productId", id + "");
			startActivity(intent);
		}

	}


	private void setviewlistener() {
		iv_sale_adv_part1.setOnClickListener(this);
		iv_sale_adv_part2.setOnClickListener(this);
		iv_sale_adv_part3.setOnClickListener(this);
		iv_sale_adv_part4.setOnClickListener(this);
		ll_half_price.setOnClickListener(this);
		ll_buyone_sendone.setOnClickListener(this);
		ll_tweenty_nine_nine.setOnClickListener(this);
		ll_special_select.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_sale_adv_part1:
			handleReqdata(BUTTON_ADV, "0");

			break;
		case R.id.iv_sale_adv_part2:
			handleReqdata(BUTTON_ADV, "1");
			break;
		case R.id.iv_sale_adv_part3:
			handleReqdata(BUTTON_ADV, "2");
			break;
		case R.id.iv_sale_adv_part4:
			handleReqdata(BUTTON_ADV, "3");
			break;
		case R.id.ll_half_price:
			handleReqdata(MIDDLE_ADV, "0");
			break;
		case R.id.ll_buyone_sendone:
			handleReqdata(MIDDLE_ADV, "1");
			break;
		case R.id.ll_tweenty_nine_nine:
			handleReqdata(MIDDLE_ADV, "2");
			break;
		case R.id.ll_special_select:
			handleReqdata(MIDDLE_ADV, "3");
			break;
		case R.id.iv_left_title:
			intent = new Intent(getActivity(), MoreCategoryLoad.class);
			startActivity(intent);
			break;
		case R.id.iv_right_title:
			intent =new Intent(getActivity(), SaleMainSearch.class);
			startActivity(intent);
		default:
			break;
		}

	}

	private void handleReqdata(String advtype, String idname) {
		Cursor cur = DBUtilities.getSplAdvMsg(advtype, idname);
		String data = null;
		String intentType = null;
		if (cur.moveToFirst()) {
			data = cur.getString(cur.getColumnIndex("_reqdata"));
			intentType = cur.getString(cur.getColumnIndex("_reqtype"));
		}
		if (data == null || intentType == null) {
			Toast.makeText(getActivity(), "没数据", 0).show();
			return;
		}
		;
		// System.out.println("adata:"+data);
		String[] split = data.split("\\|");
		Intent intent;
		switch (intentType) {
		case LARGE_LIST:
			intent = new Intent(getActivity(), ShowProductV2.class);
			if (split.length > 1) {

				intent.putExtra("data", split[1]);
				
			}
			startActivity(intent);
			break;
		case SMALL_LIST:
			intent = new Intent(getActivity(), LadyCatgory.class);
			if (split.length > 1) {
				intent.putExtra("tmid", split[1]);
			}
			startActivity(intent);
			break;
		case TM_SHOP:
			intent = new Intent(getActivity(), SpecialShop.class);
			if (split.length > 1) {
				intent.putExtra("shopId", split[1]);
			}
			startActivity(intent);
			break;
		case TM_ITEM:
			intent = new Intent(getActivity(), ProductActivity.class);
			if (split.length > 1) {
				intent.putExtra("productId", split[1]);
			}
			startActivity(intent);
			break;
		case URL:
			intent = new Intent(getActivity(), TMAdvShow.class);
			if (split.length > 1) {
				intent.putExtra("url", split[1]);
			}
			startActivity(intent);

			break;

		default:
			break;
		}
		cur.close();

	}

	/**
	 * 画一个圆点
	 */
	public static ImageView getdotimg(Context context ) {
		 ImageView dot =new ImageView(context);
		 LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			 
			 params.leftMargin=ZhongTuanApp.getInstance().getDensity()*5;
			 dot.setEnabled(false);
		 dot.setLayoutParams(params);
		 dot.setImageResource(R.drawable.point_bg);
		 return dot;
	}
}
