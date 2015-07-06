package com.iteambuysale.zhongtuan.activity.specialsale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class SearchResultShow extends BaseActivity implements OnClickListener, OnItemClickListener {
	private GridView gv_sale_lady_item;
	private ImageButton ib_special_sale_lady_return;
	private TemaiVerson2[] tmlists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lady_catgory);
		initview();
		initdata();
	}

	private void initview() {
		gv_sale_lady_item = (GridView) findViewById(R.id.gv_sale_lady_item);
		gv_sale_lady_item.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Button btn_scan_select = (Button) findViewById(R.id.btn_scan_select);
		btn_scan_select.setVisibility(View.INVISIBLE);
		ib_special_sale_lady_return = (ImageButton) findViewById(R.id.ib_special_sale_lady_return);
		gv_sale_lady_item.setOnItemClickListener(this);
		
	};

	private void initdata() {
		ib_special_sale_lady_return.setOnClickListener(this);
		 Parcelable[] parcelablelist = getIntent().getParcelableArrayExtra("tmlist");
		 tmlists=new TemaiVerson2 [parcelablelist.length];
	
		 for(int i=0;i<parcelablelist.length;i++){
			 tmlists[i]=(TemaiVerson2) parcelablelist[i];
		 }
		 gv_sale_lady_item.setAdapter(new Myadapter());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_special_sale_lady_return:
			finish();
			break;

		default:
			break;
		}

	};
	  private static class Viewholder{
	    	public ImageView iv_item_imageload;
	        public TextView	tv_item_product_introduce;
	        public TextView tv_item_nowprice;
	        public TextView tv_item_beforeprice;
	    }
	  private class Myadapter extends BaseAdapter{

		private String url;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tmlists.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return tmlists[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder holder;
			if(convertView==null){
				convertView=View.inflate(getApplicationContext(),R.layout.shop_product_adapterview, null);
				holder=new Viewholder();
				holder.iv_item_imageload=(ImageView) convertView.findViewById(R.id.iv_item_imageload);
				holder.tv_item_product_introduce=(TextView) convertView.findViewById(R.id.tv_item_product_introduce);
				holder.tv_item_nowprice=(TextView) convertView.findViewById(R.id.tv_item_nowprice);
				holder.tv_item_beforeprice=(TextView) convertView.findViewById(R.id.tv_item_beforeprice);
				convertView.setTag(holder);
			}else{
				holder=(Viewholder) convertView.getTag();
			}
			holder.tv_item_nowprice.setText(tmlists[position].getTmdj()+"元");
			holder.tv_item_beforeprice.setText(tmlists[position].getDj0()+"元");
			holder.tv_item_product_introduce.setText(tmlists[position].getTitle());
			holder.tv_item_beforeprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			url=tmlists[position].getPicurl();
			ImageUtilities.loadBitMap(url, holder.iv_item_imageload);
			return convertView;
		}
		  
	  }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
		intent.putExtra("productId", tmlists[position].getTmid());
	    startActivity(intent);
		
	}
}
