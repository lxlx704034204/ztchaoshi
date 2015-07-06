package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ShopProductStyle2Adapter extends BaseAdapter {

	//private Cursor cr;

	private Context context;



	private TemaiVerson2[] list;
	


	private Cursor cr2;


	
		
		
	public ShopProductStyle2Adapter(Context context , TemaiVerson2[] list){
		this.context=context;
		this.list=list;
		
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
		return list.length;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.shop_show_style_list, null);
			holder=new ViewHolder();
			holder.iv_shop_style=(ImageView) convertView.findViewById(R.id.iv_shop_style);
			holder.tv_shop_style_name=(TextView) convertView.findViewById(R.id.tv_shop_style_name);
			holder.tv_shop_style_num=(TextView) convertView.findViewById(R.id.tv_shop_style_num);
			holder.tv_shop_style_price=(TextView) convertView.findViewById(R.id.tv_shop_style_price);
			holder.tv_shop_style_title=(TextView) convertView.findViewById(R.id.tv_shop_style_title);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		TemaiVerson2 temai=list[position];
		ImageUtilities.loadBitMap(temai.getPicurl(), holder.iv_shop_style);
		holder.tv_shop_style_num.setText(temai.getSells());
		holder.tv_shop_style_price.setText(temai.getTmdj());
		holder.tv_shop_style_title.setText(temai.getTitle());
		cr2 = DBUtilities.getShopmsg(temai.getShopid());
		if(cr2.moveToFirst()){
			holder.tv_shop_style_name.setText(cr2.getString(cr2.getColumnIndex("_shopname")));
		}
		return convertView;
	}

	
	
	
	public static class ViewHolder {
		ImageView iv_shop_style;//商铺图片
		TextView tv_shop_style_title;//商品的名字
		TextView tv_shop_style_price;//商品的价格
		TextView tv_shop_style_num;//销量
		TextView tv_shop_style_name;//商铺的名字
		

	}

}