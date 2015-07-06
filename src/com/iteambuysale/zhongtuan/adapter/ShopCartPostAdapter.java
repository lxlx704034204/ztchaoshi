package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.model.ShopCart;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ShopCartPostAdapter extends BaseAdapter {
    
	private Context context;
	private ArrayList<ShopCart> list;
	private ShopCart cart;
	private ArrayList<Integer> shopshowposition;

	public ShopCartPostAdapter(Context context,ArrayList<ShopCart > list){
	 	this.context=context;
	 	this.list=list;
	 	ArrayList<String> shopids = new ArrayList<String>();
	 	shopshowposition=new ArrayList<Integer>();
	 	for (int i = 0; i < list.size(); i++) {
			if (!shopids.contains(list.get(i).getShopid())) {
				shopshowposition.add(Integer.valueOf(i));
				shopids.add(list.get(i).getShopid());
			} else {
				continue;
			}
		}
	}
	public int getshowshopnamenum(){
		return shopshowposition.size();
	};
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if(convertView==null){
			holder=new viewHolder();
			convertView =View.inflate(context, R.layout.cart_profuct_list_view, null);
		    holder.tv_shop_name=(TextView) convertView.findViewById(R.id.tv_shop_name);
		    holder.tv_pr_introduce_cart=(TextView) convertView.findViewById(R.id.tv_pr_introduce_cart);
		    holder.product_price_cart=(TextView) convertView.findViewById(R.id.product_price_cart);
		    holder.size_and_color=(TextView) convertView.findViewById(R.id.size_and_color);
		    holder.tv_product_num=(TextView) convertView.findViewById(R.id.tv_product_num);
		    holder.tv_shop_divider=(TextView)convertView.findViewById(R.id.tv_shop_divider);
		    holder.iv_product_cart=(ImageView)convertView.findViewById(R.id.iv_product_cart);
		    
		    convertView.setTag(holder);
		}else{
		      holder=(viewHolder) convertView.getTag();
		}
		if(shopshowposition.contains(Integer.valueOf(position))){
			holder.tv_shop_name.setVisibility(View.VISIBLE);
			holder.tv_shop_divider.setVisibility(View.VISIBLE);
		}else{
			holder.tv_shop_name.setVisibility(View.GONE);
			holder.tv_shop_divider.setVisibility(View.GONE);
		}
		cart = list.get(position);
		holder.tv_shop_name.setText(cart.getShopname());
		holder.tv_pr_introduce_cart.setText(cart.getCpmc());
		holder.tv_product_num.setText("￥"+cart.getDj()+"x" + cart.getSl());
		holder.product_price_cart.setText("￥" + cart.getJe());
		if(cart.getChima()==null){
			
			holder.size_and_color.setText("产品信息：");
		}else{
			holder.size_and_color.setText("产品信息：" + cart.getChima());
			
		}
		ImageUtilities.loadBitMap(cart.getCppic(), holder.iv_product_cart);
		return convertView;
	}
	private  class viewHolder{
		TextView tv_shop_divider,tv_shop_name,tv_pr_introduce_cart,product_price_cart,size_and_color,tv_product_num;
		ImageView iv_product_cart;
	}

}
