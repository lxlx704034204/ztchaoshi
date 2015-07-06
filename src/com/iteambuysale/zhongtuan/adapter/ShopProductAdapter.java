package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.specialsale.ProductActivity;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ShopProductAdapter extends BaseAdapter {

	private Cursor cr;

	private Context context;

	//HashMap<String, String> set1 = new HashMap<String, String>();
	//HashMap<String, String> set2 = new HashMap<String, String>();


	private ArrayList<TemaiVerson2> list;
	private ArrayList<TemaiVerson2> list1=new ArrayList<TemaiVerson2>();
	private ArrayList<TemaiVerson2> list2=new ArrayList<TemaiVerson2>();

	private boolean ismeasure = true;

	private boolean havain=false;


	public ShopProductAdapter(Context baseContext, Cursor cr) {
		this.cr = cr;
		this.context = baseContext;
		list = crtoObj(cr);
		
		for(int i=0;i<list.size();i++){
			if(i%2==0){
				list1.add(list.get(i));
			}else{
				list2.add(list.get(i));
			}
		}
		
		
	}

	public ShopProductAdapter(Context applicationContext,
			TemaiVerson2[] temaiList) {
		this.context=applicationContext;
		//this.list=list;
		for(int i=0;i<temaiList.length;i++){
			if(i%2==0){
				list1.add(temaiList[i]);
			}else{
				list2.add(temaiList[i]);
			}
		}
	}
	public ArrayList<TemaiVerson2> crtoObj(Cursor cr) {
		ArrayList<TemaiVerson2> list = new ArrayList<TemaiVerson2>();
		int i=0;
		this.cr=cr;
		list1.clear();
		list2.clear();
		while (cr.moveToNext()) {
			TemaiVerson2 temai = new TemaiVerson2();
			temai.setPicurl(getcrstr("_picurl"));
			temai.setDj0(getcrstr("_dj0"));
			temai.setTmdj(getcrstr("_tmdj"));
			temai.setTitle(getcrstr("_title"));
			temai.setTmid(getcrstr("_id"));
			temai.setTbmoney(getcrstr("_tbmoney"));
			temai.setTmword(getcrstr("_tmword"));
			if(i%2==0){
				list1.add(temai);
			}else{
				list2.add(temai);
			}
			i++;
		}

		return list;
	}

	@Override
	public int getCount() {
		/*if (list.size()%2==1) {
			return list.size() / 2+1;
		}else if(list.size()==2)
		{
			return 1;
		}
		return list.size() / 2;
*/
		return list1.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder viewholder;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.sale_atagory_lady_adapterview, null);
			viewholder.iv_item_imageload1 = (ImageView) convertView
					.findViewById(R.id.iv_item_imageload1);
			viewholder.tv_item_beforeprice1 = (TextView) convertView
					.findViewById(R.id.tv_item_beforeprice1);
			viewholder.tv_item_beforeprice1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); 
			viewholder.tv_item_nowprice1 = (TextView) convertView
					.findViewById(R.id.tv_item_nowprice1);
			viewholder.tv_item_product_introduce1 = (TextView) convertView
					.findViewById(R.id.tv_item_product_introduce1);
			viewholder.iv_item_imageload2 = (ImageView) convertView
					.findViewById(R.id.iv_item_imageload2);
			viewholder.tv_item_beforeprice2 = (TextView) convertView
					.findViewById(R.id.tv_item_beforeprice2);
			viewholder.tv_item_beforeprice2.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); 
			viewholder.tv_item_nowprice2 = (TextView) convertView
					.findViewById(R.id.tv_item_nowprice2);
			viewholder.tv_item_product_introduce2 = (TextView) convertView
					.findViewById(R.id.tv_item_product_introduce2);
			viewholder.position = position;
			viewholder.rl1 = (RelativeLayout) convertView
					.findViewById(R.id.rl_shop_product1);
			viewholder.rl2 = (RelativeLayout) convertView
					.findViewById(R.id.rl_shop_product2);
			viewholder.tv_item_product_title1=(TextView) convertView.findViewById(R.id.tv_item_product_title1);
			viewholder.tv_item_product_title2=(TextView) convertView.findViewById(R.id.tv_item_product_title2);
			viewholder.iv_sendtb1=(TextView) convertView.findViewById(R.id.iv_sendtb1);
			viewholder.iv_sendtb2=(TextView) convertView.findViewById(R.id.iv_sendtb2);
		//	viewholder.rl2
			
			
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.rl1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ProductActivity.class);
				//int positions=positions+((ListView)parent).getFirstVisiblePosition();
				intent.putExtra("productId", list1.get(position).getTmid());
				//System.out.println(list1.get(position).getTmid()+","+"position:"+position);
				context.startActivity(intent);
			}
		});
		viewholder.rl2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    
				Intent intent = new Intent(context, ProductActivity.class);
				//int positions=position+((ListView)parent).getFirstVisiblePosition();
				intent.putExtra("productId", list2.get(position).getTmid());
				//System.out.println(list2.get(position).getTmid()+","+"position:"+position);
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		setvalue(position, viewholder);
		
		return convertView;
	}

	/*
	 * private void measureHeight(ViewGroup v) { v.measure(0, 0); int height =
	 * v.getMeasuredHeight(); LayoutParams params = listview.getLayoutParams();
	 * params.height=list.size()*height/2; listview.setLayoutParams(params);
	 * }
	 */
	 private void setvalue(int position,ViewHolder viewholder){
		    String picturl1 = list1.get(position).getPicurl();
			String bprice1 = list1.get(position).getDj0();
			String nprice1 = list1.get(position).getTmdj();
			String title1 = list1.get(position).getTitle();
			String temaiid1 = list1.get(position).getTmid();
			String tbmoney1=list1.get(position).getTbmoney();
			String tword1 =list1.get(position).getTmword();
			if(TextUtils.isEmpty(tbmoney1)||TextUtils.equals(tbmoney1, "0")){
				viewholder.iv_sendtb1.setVisibility(View.GONE);
			}else{
				viewholder.iv_sendtb1.setVisibility(View.VISIBLE);
			}
			ImageUtilities.loadBitMap(picturl1, viewholder.iv_item_imageload1);
			viewholder.tv_item_beforeprice1.setText("￥"+bprice1);
			viewholder.tv_item_nowprice1.setText("￥"+nprice1);
			viewholder.tv_item_product_introduce1.setText(tword1);
			viewholder.tv_item_product_title1.setText(title1);
			//set1.put(position + "", temaiid1);
			if(list2!=null&&position<list2.size()){
				viewholder.rl2.setVisibility(View.VISIBLE);
				String picturl2 = list2.get(position).getPicurl();
				String bprice2 = list2.get(position).getDj0();
				String nprice2 = list2.get(position).getTmdj();
				String title2 = list2.get(position).getTitle();
				String temaiid2 = list2.get(position).getTmid();
				String tword2 =list2.get(position).getTmword();
				ImageUtilities.loadBitMap(picturl2, viewholder.iv_item_imageload2);
				viewholder.tv_item_beforeprice2.setText("￥"+bprice2);
				viewholder.tv_item_nowprice2.setText("￥"+nprice2);
				viewholder.tv_item_product_introduce2.setText(tword2);
				viewholder.tv_item_product_title2.setText(title2);
				String tbmoney2=list2.get(position).getTbmoney();
				if(TextUtils.isEmpty(tbmoney2)||TextUtils.equals(tbmoney2, "0")){
					viewholder.iv_sendtb2.setVisibility(View.GONE);
				}else{
					//viewholder.tv_item_product_title1.setVisibility(View.VISIBLE);
					viewholder.iv_sendtb2.setVisibility(View.VISIBLE);
				}
				//set2.put(position + "", temaiid2);
			}else{
				viewholder.rl2.setVisibility(View.INVISIBLE);
			}
	 }
	

	private String getcrstr(String str) {
		//try {

			return cr.getString(cr.getColumnIndex(str));
		/*} catch (Exception e) {
			
		}*/
		//return null;
	}

	public static class ViewHolder {
		ImageView iv_item_imageload1;
		TextView tv_item_beforeprice1;
		TextView tv_item_nowprice1;
		TextView tv_item_product_introduce1;
		ImageView iv_item_imageload2;
		TextView tv_item_beforeprice2;
		TextView tv_item_nowprice2;
		TextView tv_item_product_introduce2;
		TextView tv_item_product_title1;
		TextView tv_item_product_title2;
		TextView iv_sendtb1,iv_sendtb2;
		RelativeLayout rl2;
		RelativeLayout rl1;
		public int position;

	}

}