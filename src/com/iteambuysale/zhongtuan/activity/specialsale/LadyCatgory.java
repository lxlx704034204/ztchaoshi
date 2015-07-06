package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.SaleListener;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Product;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class LadyCatgory extends BaseActivity implements OnCheckedChangeListener, OnClickListener, NetAsyncListener, OnItemClickListener, SaleListener {
	private RadioButton rb_sale_lady_zonghe;
	private RadioButton rb_sale_lady_xiaoliang;
	private EditText et_special_sale_lady_search;
	private GridView gv_sale_lady_item;
	private Myadapter adapter;
	private SaleListener mListener;
	private Cursor cr;
	private String cup;
	private String lbid;
	private CustomProgressDialog mProgressDialog;
	private String tmid;
	@Override
	protected void onDestroy() {
		cr.close();
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lady_catgory);
		cup = getIntent().getStringExtra("cup");
		tmid = getIntent().getStringExtra("tmid");
	
		//datamodel();
		mListener=this;
		if (cup==null&&tmid==null) {
			return ;
		}
		loadeveryproductdetail(cup, tmid);
		RadioGroup rg_sale_lady_detial=(RadioGroup) findViewById(R.id.rg_sale_lady_detial);
		rg_sale_lady_detial.setOnCheckedChangeListener(this);
		rb_sale_lady_zonghe = (RadioButton) findViewById(R.id.rb_sale_lady_zonghe);
		rb_sale_lady_xiaoliang = (RadioButton) findViewById(R.id.rb_sale_lady_xiaoliang);
		ImageButton ib_special_sale_lady_return=(ImageButton) findViewById(R.id.ib_special_sale_lady_return);
		et_special_sale_lady_search = (EditText) findViewById(R.id.et_special_sale_lady_search);
		et_special_sale_lady_search.setOnClickListener(this);
		gv_sale_lady_item = (GridView) findViewById(R.id.gv_sale_lady_item);
		gv_sale_lady_item.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Button btn_scan_select =(Button) findViewById(R.id.btn_scan_select);
		btn_scan_select.setOnClickListener(this);
		/*if(cup==null){
			DBUtilities.getsomeTmproduct(tmid);
		}else{
			cr = DBUtilities.getTeamVersonList(cup);
		}*/
		getCursor(null);
	    if (cr.getCount()>0) {
	    	adapter = new Myadapter(getApplicationContext(),cr);
	    	gv_sale_lady_item.setAdapter(adapter);
	    	gv_sale_lady_item.setOnItemClickListener(this);
		}
		
		
	//	gv_sale_lady_item.setAdapter();
		
		ib_special_sale_lady_return.setOnClickListener(this);
	}
	@Override
	protected void onRestart() {
		if (mProgressDialog!=null) {
			mProgressDialog.dismiss();
		}
		
		super.onRestart();
	}
    private class Myadapter extends CursorAdapter {
        String url=null;
		private String tbMoney;
        
		public Myadapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public void bindView(final View view, Context context, Cursor cursor) {
			final Viewholder holder=(Viewholder) view.getTag();
			holder.tv_item_nowprice.setText(cursor.getString(cursor.getColumnIndex("_tmdj"))+"元");
			holder.tv_item_beforeprice.setText(cursor.getString(cursor.getColumnIndex("_dj0"))+"元");
			holder.tv_item_product_introduce.setText(cursor.getString(cursor.getColumnIndex("_tmword")));
			holder.tv_item_product_title.setText(cursor.getString(cursor.getColumnIndex("_title")));
			holder.tv_item_beforeprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			url=cursor.getString(cursor.getColumnIndex("_picurl"));
			tbMoney = cursor.getString(cursor.getColumnIndex("_tbmoney"));
			if(TextUtils.isEmpty(tbMoney)||TextUtils.equals(tbMoney, "0")){
				holder.iv_sendtb.setVisibility(View.GONE);
			}else{
				holder.iv_sendtb.setVisibility(View.VISIBLE);
			}
			ImageUtilities.loadBitMap(url, holder.iv_item_imageload);
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup viewgroup) {
			View view=View.inflate(getApplicationContext(), R.layout.shop_product_adapterview, null);
			Viewholder holder=new Viewholder();
			holder.iv_item_imageload=(ImageView) view.findViewById(R.id.iv_item_imageload);
			holder.tv_item_product_introduce=(TextView) view.findViewById(R.id.tv_item_product_introduce);
			holder.tv_item_nowprice=(TextView) view.findViewById(R.id.tv_item_nowprice);
			holder.tv_item_beforeprice=(TextView) view.findViewById(R.id.tv_item_beforeprice);
			holder.iv_sendtb=(TextView)view.findViewById(R.id.iv_sendtb);
			holder.tv_item_product_title=(TextView)view.findViewById(R.id.tv_item_product_title);
			view.setTag(holder);
			return view;
		}
    }
    public static class Viewholder{
    	public ImageView iv_item_imageload;
        public TextView	tv_item_product_introduce;
        public TextView tv_item_nowprice,tv_item_product_title;
        public TextView tv_item_beforeprice,iv_sendtb;
    }
		
    private void loadeveryproductdetail(final String cup,final String tmid){
    	NetAsync everyproduct =new NetAsync(D.API_SPECIAL_SELL, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2[]>() {
				}.getType();
				TemaiVerson2[] temaiList = JsonUtilities
						.parseModelByType(elData, type);
				TemaiVerson2.save(temaiList);
				return temaiList;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				if(cup!=null||!TextUtils.equals(cup, "")){
					params.add(new BasicNameValuePair("cpdl", cup));//大类
				}
				if(tmid!=null||!TextUtils.equals(tmid, "")){
				 params.add(new BasicNameValuePair("tmid", tmid));//小类
				}
				
			}
		};
		everyproduct.execute();
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {	
		switch (checkedId) {
		case R.id.rb_sale_lady_zonghe:
			getCursor(null);
			break;
		case R.id.rb_sale_lady_xiaoliang:
			getCursor(D.ORDER_BY_SELL);
			break;
		case R.id.rb_sale_lady_price:
			getCursor(D.ORDER_BY_PRICE_DESC);
			break;
        
		default:
			break;
		}
		if (cr!=null&& adapter!=null) {
			
			adapter.changeCursor(cr);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.ib_special_sale_lady_return:
			finish();
			break;
		case R.id.et_special_sale_lady_search:
		case R.id.btn_scan_select:
			Intent intent =new Intent(getApplicationContext(), SearchToList.class);
			intent.putExtra("cup", cup);
			startActivity(intent);
		default:
			break;
		}
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if(TextUtils.equals(reqUrl, D.API_SPECIAL_SELL)){
			getCursor(null);
			if (adapter==null) {
				adapter = new Myadapter(getApplicationContext(),cr);
		    	gv_sale_lady_item.setAdapter(adapter);
		    	gv_sale_lady_item.setOnItemClickListener(this);
			}else{
				adapter.changeCursor(cr);
				adapter.notifyDataSetChanged();
			}
		}else if(TextUtils.equals(reqUrl, D.API_SPECIAL_GETATEMAI)){
			String Tmid=(String) data;
			Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
			intent.putExtra("productId", Tmid);
		    startActivity(intent);
		}
	}
	public void getCursor(String order) {
		if(cup!=null){
			
			cr = DBUtilities.getTeamVersonList(cup,order);
		}else{
			cr = DBUtilities.getsomeTmproduct(tmid,order);
			
		}
	}
	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(this,false);		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TemaiVerson2 temai = Model.load(new TemaiVerson2(), String.valueOf(id));
		temai.getTmid();
		//temai.get
		//int opt = Integer.valueOf(temai.getTmlb());
		if(TextUtils.equals(temai.getTmlb(), "dp")){
			String pId = temai.getTmid();
			if (!Model.isRecoredExists(Product.class, pId)) {
				//loadProduct(pId);
				Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
				intent.putExtra("productId", pId);
			    startActivity(intent);
				return;
			}
			mListener.onSelectProductItem(pId);
			
		}
		else if(TextUtils.equals(temai.getTmlb(), "zt")){
			String url = temai.getTmurl();
			mListener.onSelectWebviewItem(url);
		}
		
	}
	/*private void loadProduct(final String pid) {
		mListener.onSelectProductItem(null);

		NetAsync task_getproduct = new NetAsync(D.API_SPECIAL_GETATEMAI, mListener) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				
				 TemaiVerson2  temai= JsonUtilities.parseModelByType(elData,
						TemaiVerson2.class);
				//product.save(new Product []{product}, true);
				return temai.getTmid();
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_PRODUCT_TMID, pid));
			}
		};
		task_getproduct.execute();
	}*/
	@Override
	public void onLoadPicSuccess() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,false);
		
	}
	@Override
	public void onSelectProductItem(String pid) {
		if (null == pid) {
			mProgressDialog = CustomProgressDialog.createDialog(this);
			mProgressDialog.show();
			return;
		}
		
	}
	@Override
	public void onSelectWebviewItem(String url) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
