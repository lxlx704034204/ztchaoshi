package com.iteambuysale.zhongtuan.activity.specialsale;


import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.ProductStyleLsAdapter;
import com.iteambuysale.zhongtuan.adapter.ShopProductAdapter;
import com.iteambuysale.zhongtuan.adapter.ShopProductStyle2Adapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SearchToList extends Activity implements OnCheckedChangeListener, OnClickListener, OnItemClickListener, OnScrollListener, OnFocusChangeListener, NetAsyncListener {
        private ListView shopList;
		private EditText  et_search;
		private ImageView iv_anim;
		private int pri=1;
		private int pos=1;
		private int width;
		private int y;
		
		private RadioButton rb_shop_zh;
		private RadioButton rb_shop_jg;
		private RadioButton rb_shop_xp;
		private Cursor cr;
		private String shopId;
		private ShopProductAdapter adapter;
		private ImageView iv_style;
		private RadioGroup rg_shop_ls;
		
		private String [] from=new String[]{"_picurl","_title","_tmdj","_sells"};
		private int[] to=new int[]{R.id.iv_shop_style,R.id.tv_shop_style_title,R.id.tv_shop_style_price,R.id.tv_shop_style_num};
		private ProductStyleLsAdapter styleadapter;
		private boolean currentstate=true;
		private ImageView iv_return;
		private int [] location =new int[2];
		private ImageView header;
		private LinearLayout ll_shop_ls;
		private LinearLayout ll_visiable;
		private int height;
		private TextView tv_search;
		private String cup;//产品的大类
		private TemaiVerson2[] temaiLists;
		private CustomProgressDialog mProgressDialog;
		private boolean isfromsearch=false;
		private boolean rb_shop_jgSelect=false;
		
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.shop_all_item);
        	initview();
        	initdata();
        }
        private void initview() {
        	iv_return = (ImageView) findViewById(R.id.iv_shop_ls_return);
        	et_search = (EditText ) findViewById(R.id.et_shop_ls_search);
        	iv_style = (ImageView) findViewById(R.id.iv_shop_ls_style);
        	ll_shop_ls = (LinearLayout) findViewById(R.id.ll_shop_ls);
        	ll_shop_ls.getBackground().setAlpha(200);
        	rb_shop_zh = (RadioButton) findViewById(R.id.rb_shop_zh);
        	rb_shop_jg = (RadioButton) findViewById(R.id.rb_shop_jg);
        	rb_shop_xp = (RadioButton) findViewById(R.id.rb_shop_xp);
        	rb_shop_zh.setChecked(true);
        	rg_shop_ls = (RadioGroup) findViewById(R.id.rg_shop_ls);
        	shopList = (ListView) findViewById(R.id.lv_shop_ls);
            iv_anim = (ImageView) findViewById(R.id.iv_anim);
            ll_visiable = (LinearLayout) findViewById(R.id.ll_style_showversible);
            tv_search = (TextView) findViewById(R.id.tv_shop_text_search);
        }
		private void initdata() {
			et_search.setOnClickListener(this);
			et_search.setOnFocusChangeListener(this);
			rg_shop_ls.setOnCheckedChangeListener(this);
			shopId = getIntent().getStringExtra("shopId");
			cup = getIntent().getStringExtra("cup");
			getCursor(null);
			if (cr!=null) {
				adapter = new ShopProductAdapter(this, cr);
			}
           rb_shop_jg.setOnClickListener(this);	
           iv_style.setOnClickListener(this);
           iv_return.setOnClickListener(this);
           shopList.setOnItemClickListener(this);
           //测量ll_visiable的高度
           ll_visiable.measure(0, 0);
           height = ll_visiable.getMeasuredHeight();
           header = new ImageView(this);
           LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, height+ZhongTuanApp.getInstance().getDensity()*4);
           header.setLayoutParams(params);
           shopList.addHeaderView(header);
           shopList.setAdapter(adapter);
           shopList.setOnScrollListener(this);
           tv_search.setOnClickListener(this);
           mProgressDialog = CustomProgressDialog.createDialog(this);
		}
		public void getCursor(String order) {
			if(shopId!=null&&!TextUtils.equals(shopId, "")){
				cr = DBUtilities.getShopProductOrderBy(shopId, order);
				/*while(cr.moveToNext()){
					System.out.println(cr.getString(cr.getColumnIndex("_id")));
				}
				cr.moveToFirst();*/
			}else{
				cr=DBUtilities.getTeamVersonList(cup,order);
			}
				//TODO 按照别的方式搜素数据
				
		}
		
		/**
		 * 计算两个radiobutton之间的kuandu
		 * 并给iv_animi设置宽度；
		 */
		private void caluwidth() {
			int [] location1=new int[2];
			int [] location2=new int[2];
			int [] location3=new int[2];
			rb_shop_zh.getLocationOnScreen(location1);
			rb_shop_xp.getLocationOnScreen(location2);
			iv_anim.getLocationOnScreen(location3);
			y=location3[1];
			width=location2[0]-location1[0];
			ViewGroup.LayoutParams params = iv_anim.getLayoutParams();
			params.width=width;
			iv_anim.setLayoutParams(params);
		}
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		 caluwidth();
		}
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int startx;
			int endx;
			switch (checkedId) {
			case R.id.rb_shop_zh://综合
				pos =1;
				startx=(pri-1)*width;
				endx=(pos-1)*width;
				iv_anim.startAnimation(initanmi(startx, endx,0, 0,  Math.abs(pos-pri)));
					showstyle();
				pri=pos;
				break;
			case R.id.rb_shop_xp://新品
				pos=2;
				startx=(pri-1)*width;
				endx=(pos-1)*width;
				iv_anim.startAnimation(initanmi(startx, endx,0, 0, Math.abs(pos-pri)));
				isfromsearch=false;
				showstyle();
				pri=pos;
				break;
			case R.id.rb_shop_xl://销量
				pos=3;
				startx=(pri-1)*width;
				endx=(pos-1)*width;
				iv_anim.startAnimation(initanmi(startx, endx,0, 0,  Math.abs(pos-pri)));
				isfromsearch=false;
				showstyle();
				pri=pos;
				break;
			case R.id.rb_shop_jg://价格
				pos=4;
				startx=(pri-1)*width;
				endx=(pos-1)*width;
				iv_anim.startAnimation(initanmi(startx, endx,0, 0, Math.abs(pos-pri)));
				isfromsearch=false;
				//showstyle();
				pri=pos;
				break;
			default:
				break;
			}
			
		}
		/** 按照综合，新品，价格排列商品
		 * @param pos2
		 */
		private void showstyle() {
				switch (pos) {
				case 1:
					if(isfromsearch){
					zhSearchList();
						break;
					}
				case 2:
					 //cr=DBUtilities.getShopProductAllList(shopId);
					 getCursor(D.ORDER_BY_DATE);
					 if(cr==null) break;
					 if (currentstate) {
						 adapter =new ShopProductAdapter(this,  cr);
						 shopList.setAdapter(adapter);
					}else{
						  styleadapter = new ProductStyleLsAdapter(this, R.layout.shop_show_style_list, cr, from, to);
						   shopList.setDividerHeight(ZhongTuanApp.getInstance().getDensity());
						   shopList.setAdapter(styleadapter);
					}
					break;
				case 3:
					getCursor(D.ORDER_BY_SELL);
					//cr=DBUtilities.getpopularproductAlllist(shopId);
					 if(cr==null) break;
					 if (currentstate) {
						 adapter =new ShopProductAdapter(this,  cr);
						 shopList.setAdapter(adapter);
					}else{
						  styleadapter = new ProductStyleLsAdapter(this, R.layout.shop_show_style_list, cr, from, to);
						   shopList.setDividerHeight(ZhongTuanApp.getInstance().getDensity());
						   shopList.setAdapter(styleadapter);
					}
					break;
				case 4:
					 getCursor(D.ORDER_BY_PRICE_DESC);
					 //cr=DBUtilities.getpopularproductAllpriceasd(shopId);
					 if(cr==null) break;
					 if (currentstate) {
						 adapter =new ShopProductAdapter(this,  cr);
						 shopList.setAdapter(adapter);
					}else{
						   styleadapter = new ProductStyleLsAdapter(this, R.layout.shop_show_style_list, cr, from, to);
						   shopList.setDividerHeight(ZhongTuanApp.getInstance().getDensity());
						   shopList.setAdapter(styleadapter);
					}
                   // rb_shop_jg.setSelected(true);
					break;

				default:
					break;
				
			}
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.et_shop_ls_search:
				break;
			case R.id.rb_shop_jg:
				if(pri==pos){
					rb_shop_jg.setSelected(!rb_shop_jg.isSelected());
					rb_shop_jgSelect=rb_shop_jg.isSelected();
					if (rb_shop_jgSelect) {
						
						//cr=DBUtilities.getpopularproductAllpricedecs(shopId);
						getCursor(D.ORDER_BY_PRICE_DESC);
					}else{
						getCursor(D.ORDER_BY_PRICE_ASC);
						//cr=DBUtilities.getpopularproductAllpriceasd(shopId);
					}
					if (cr==null) {
						break;
					}
					 if (currentstate) {
						 System.out.println("执行了");
						 adapter =new ShopProductAdapter(this,  cr);
						 shopList.setAdapter(adapter);
					}else{
						   styleadapter = new ProductStyleLsAdapter(this, R.layout.shop_show_style_list, cr, from, to);
						   shopList.setDividerHeight(ZhongTuanApp.getInstance().getDensity());
						   shopList.setAdapter(styleadapter);
					}
				}
				break;
			case R.id.iv_shop_ls_style:
				iv_style.setSelected(!iv_style.isSelected());
				currentstate=!currentstate;
				showstyle();
				break;
			case R.id.iv_shop_ls_return:
				finish();
				break;
			case R.id.tv_shop_text_search:
				String str=et_search.getText().toString();
				//此处数据库查询改为联网查询数据；
				loadSearchResult(str);
				try {
					if(mProgressDialog!=null&&!mProgressDialog.isShowing()){
					mProgressDialog.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				isfromsearch=true;
				rb_shop_zh.setChecked(true);
				//Cursor cursor = DBUtilities.getsearchmsg(str);//此处要删除修改的
				
			default:
				break;
			}
			
		}
private void loadSearchResult(final String key) {
	    NetAsync searchAsync=new NetAsync(D.API_SPECIAL_SELL,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2[]>() {
				}.getType();
				TemaiVerson2[] temaiList = JsonUtilities.parseModelByType(
						elData, type);
				return temaiList;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("cpdl",cup));
				params.add(new BasicNameValuePair("key",key));
				params.add(new BasicNameValuePair("shopid",shopId));
				
				
			}
		};
			searchAsync.execute();
		}
private Animation initanmi(int startx,int endx,int starty, int endy ,int time){
	   TranslateAnimation anim=new TranslateAnimation( startx, endx, starty, endy);
	   anim.setDuration(time*300);
	   anim.setFillAfter(true);
	   return anim;
   }

@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	if (!currentstate) {
		/*TemaiVerson2 temai = Model.load(new TemaiVerson2(), String.valueOf(id));
		if (TextUtils.equals(temai.getTmlb(), "dp")) {
			String pId = temai.getTmid();
			if (Model.isRecoredExists(TemaiVerson2.class, pId)) {
				Intent intent = new Intent(this, ProductActivity.class);
				intent.putExtra("productId", PId);
				startActivity(intent);
			}
	}*/
		Intent intent = new Intent(this, ProductActivity.class);
		intent.putExtra("productId", id+"");
		startActivity(intent);
		}
	
}
@Override
public void onScrollStateChanged(AbsListView view, int scrollState) {
	
}
@Override
public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	try {
		header.getLocationOnScreen(location);
		if (location[1]<-height*0.7) {
			ll_visiable.setVisibility(View.GONE);
		}else {
			ll_visiable.setVisibility(View.VISIBLE);
			
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
@Override
public void onFocusChange(View v, boolean hasFocus) {
     switch (v.getId()) {
	case R.id.et_shop_ls_search:
		if (hasFocus) {
			iv_style.setVisibility(View.GONE);
			tv_search.setVisibility(View.VISIBLE);
		}else{
			iv_style.setVisibility(View.VISIBLE);
			tv_search.setVisibility(View.GONE);
		}
		break;

	default:
		break;
	}	
}
@Override
public void onResultError(String reqUrl, String errMsg) {
	if (mProgressDialog.isShowing()) {
		mProgressDialog.dismiss();
	}
	Toast.makeText(this, errMsg, 0).show();
	
	
}
@Override
public void onResultSuccess(String reqUrl, Object data) {
	switch (reqUrl) {
	case D.API_SPECIAL_SELL:
		mProgressDialog.dismiss();
		temaiLists = (TemaiVerson2[]) data;
		 zhSearchList();
		tv_search.setVisibility(View.GONE);
		iv_style.setVisibility(View.VISIBLE);
		et_search.clearFocus();
		break;

	default:
		break;
	}
	
}
public void zhSearchList() {
	if (currentstate) {
		 adapter =new ShopProductAdapter(getApplicationContext(), temaiLists);
		 shopList.setAdapter(adapter);
	}else{
		   ShopProductStyle2Adapter style2Adapter = new ShopProductStyle2Adapter(getApplicationContext(), temaiLists);
		  // shopList.setDividerHeight(ZhongTuanApp.getInstance().getDensity());
		   shopList.setAdapter(style2Adapter);
	}
}
@Override
public void onTokenTimeout() {
	mProgressDialog.dismiss();
	
}
}
