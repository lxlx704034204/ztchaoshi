package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.adapter.ProductShowV2Adapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.TimeToloadDataListener;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class ShowProductV2 extends Activity implements OnItemClickListener, TimeToloadDataListener, NetAsyncListener {
	private TextView title;
	private Button back;
	private Button setting;
	private ListView list;
	private String[] froms;
	private int [] tos;
	private ProductShowV2Adapter adapter;
	private int page=0;
	private int beforepage=-1;
	private Cursor c;
	private int listFirstvisibleItem;
	private View footerView;
	private int footerheight;
	private ImageView iv_anim;
	private String cpid;
	private int topy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tuangouhui_fregment);
		super.onCreate(savedInstanceState);
		initview();
		initdata();
	}

	

	private void initview() {
		title = (TextView) findViewById(R.id.tv_header_tittle);
		back = (Button) findViewById(R.id.back);
		setting = (Button) findViewById(R.id.setting);
		list = (ListView) findViewById(R.id.list);
		footerView = View.inflate(getApplicationContext(), R.layout.product_footer_v2, null);
		list.addFooterView(footerView);
		iv_anim = (ImageView) footerView.findViewById(R.id.zhuanjuhua);
	}
	

	private void initdata() {
		title.setText("产品推荐");
		cpid = getIntent().getStringExtra("data");
		//System.out.println("cpid:"+cpid);
		if (cpid==null||TextUtils.equals(cpid, "")) {
			return ;
		}
		back.setVisibility(View.INVISIBLE);
		setting.setVisibility(View.INVISIBLE);
		 froms = new String[] { "_picurl", "_title", "_dj0", "_tmdj","_sells" };
		 tos = new int []{R.id.iv_product_image,R.id.tv_product_introduce,R.id.tv_before_discount,R.id.tv_discount,R.id.tv_sell_num};
		// c = DBUtilities.getTeamVersonList();
		 c=DBUtilities.getsomeTmproduct(cpid,null);
		 adapter =new ProductShowV2Adapter(this, R.layout.showproduct_version2, c, froms, tos, list);
		 adapter.setLoadDataListener(this);
		
		 list.setAdapter(adapter);
		 footerheight = measurefootview();
		 list.setVerticalScrollBarEnabled(false);
		 list.setOnItemClickListener(this);
		 loadSellProduct(page);
		 startanim();
	}
   
	private int measurefootview() {
		footerView.measure(0, 0);
		return footerView.getMeasuredHeight();
	}
	public void startanim(){
		iv_anim.setBackgroundResource(R.anim.special_sale_loadmore_v2);
		AnimationDrawable animationdrawable=(AnimationDrawable) iv_anim.getBackground();
		animationdrawable.start();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			Intent intent = new Intent(this, ProductActivity.class);
			intent.putExtra("productId", id+"");
			startActivity(intent);
		
	}

	@Override
	public void loadNextPageData() {
		if (beforepage<page) {
			loadSellProduct(page);
			beforepage++;
		}
	}
	@Override
	public void getcurrentvisiableItem(int firstVisibaleItem,int y) {
         listFirstvisibleItem=firstVisibaleItem;
         topy=y;
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
						Model.save(temaiList);
						return temaiList;
					}

					@Override
					public void beforeRequestInBackground(List<NameValuePair> params) {
						params.add(new BasicNameValuePair(D.ARG_TM_PAGE, page + ""));
						params.add(new BasicNameValuePair("tmid", cpid));
					}
				};
				task_loadTemai.execute();
			}

			@Override
			public void onResultError(String reqUrl, String errMsg) {
               Toast.makeText(this, errMsg, 0).show();
               switch (reqUrl) {
			case D.API_SPECIAL_SELL:
				footerView.setPadding(0, -footerheight, 0, 0);
				break;

			default:
				break;
			}
 
				
			}

			@Override
			public void onResultSuccess(String reqUrl, Object data) {
				switch (reqUrl) {
				case D.API_SPECIAL_SELL:
					//if (page!=0) {
					//	View view=list.getChildAt(position-1);
						//int y=view.getTop();
						c = DBUtilities.getsomeTmproduct(cpid,null);
						//System.out.println("c.getcount():"+c.getCount());
						//System.out.println("cpid:"+cpid);
						adapter.changeCursor(c);
						adapter.putValuetolist(c);
						adapter.setflag(false);
						adapter.notifyDataSetChanged();
						//list.setSelection(listFirstvisibleItem);
						//int position=list.getFirstVisiblePosition();//可能存在问题啊
						//View view=list.getChildAt(listFirstvisibleItem);
						if(page==0){
							list.setSelection(0);
						}else{
							list.setSelectionFromTop(listFirstvisibleItem+1, topy);
						}
						adapter.setflag(true);
					//}
					page++;
					beforepage=page-1;
					/*c = DBUtilities.getsomeTmproduct(cpid);
					adapter.notifyDataSetChanged();*/
					
					break;

				default:
					break;
				}
				
			}

			@Override
			public void onTokenTimeout() {
				// TODO Auto-generated method stub
				
			}

			
} 
