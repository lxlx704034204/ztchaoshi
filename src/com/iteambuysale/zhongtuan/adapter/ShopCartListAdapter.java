package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.specialsale.ProductActivity;
import com.iteambuysale.zhongtuan.listener.shopcart.UpdataCartMsgListener;
import com.iteambuysale.zhongtuan.model.ShopCart;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ShopCartListAdapter extends BaseAdapter  {
	private ArrayList<ShopCart> list;
	private Context context;
	private ShopCart shopCart;
	ArrayList<String> shopids;
	ArrayList<Integer> pos;//记录第一次出现的商铺名的位置；
	ArrayList<Integer> shownum;// 记录修改产品数量的状态位置及时change_product_num的状态。
	ArrayList<Integer> selectnum;//记录选中购买产品的位置。
	ArrayList<Integer> selectShop;//记录选中状态的店铺的位置
	int[] shopselect;
	private UpdataCartMsgListener listener;
	private ListView listview;
	public void setonUpdataCartMsgListener(UpdataCartMsgListener listener){
		this.listener=listener;
	}

	public ShopCartListAdapter(Context context, ArrayList<ShopCart> list,ListView listview) {
		this.listview=listview;
		this.list = list;
		this.context = context;
		shopids = new ArrayList<String>();
		pos = new ArrayList<Integer>();
		shownum=new ArrayList<Integer>();
		selectnum=new ArrayList<Integer>(){};
		selectShop=new ArrayList<Integer>();
		
		
		for (int i = 0; i < list.size(); i++) {
			if (!shopids.contains(list.get(i).getShopid())) {
				pos.add(Integer.valueOf(i));
				shopids.add(list.get(i).getShopid());
			} else {
				continue;
			}
		}
		shopselect=new int[pos.size()]; 
		/*for(int i=0;i<pos.size();i++){
			System.out.println("i:"+i+",pos:"+pos.get(i));
		}*/
		/*SumMoneyPopwindow popwindow=new SumMoneyPopwindow(context);
		popwindow.createpopwindow();
		popwindow.setparents(listview);
		popwindow.showpopwindow();*/
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertview,final ViewGroup parents) {
		final ViewHolder holder;
			convertview = View.inflate(context, R.layout.shopcart_adapterview,
					null);
			holder = new ViewHolder();
			holder.iv_shop_selects = (ImageView) convertview
					.findViewById(R.id.iv_shop_selects);
			holder.iv_product_cart = (ImageView) convertview
					.findViewById(R.id.iv_product_cart);
			holder.change_product_num = (ImageView) convertview
					.findViewById(R.id.change_product_num);
			holder.tv_product_num = (TextView) convertview
					.findViewById(R.id.tv_product_num);
			holder.tv_pr_introduce_cart = (TextView) convertview
					.findViewById(R.id.tv_pr_introduce_cart);
			holder.size_and_color = (TextView) convertview
					.findViewById(R.id.size_and_color);
			holder.product_price_cart = (TextView) convertview
					.findViewById(R.id.product_price_cart);
			holder.tv_shop_name = (TextView) convertview
					.findViewById(R.id.tv_shop_name);
			holder.ll_shop_title = (LinearLayout) convertview
					.findViewById(R.id.ll_shop_title);
			holder.iv_shop_divider = (ImageView) convertview
					.findViewById(R.id.iv_shop_divider);
			holder.ll_change_num=(LinearLayout) convertview.findViewById(R.id.ll_change_num);
			holder.iv_sub_num=(ImageView) convertview.findViewById(R.id.iv_sub_num);
			holder.iv_add_num=(ImageView) convertview.findViewById(R.id.iv_add_num);
			holder.tv_change_buy_num=(TextView) convertview.findViewById(R.id.tv_change_buy_num);
			holder.iv_product_select_cart=(ImageView) convertview.findViewById(R.id.iv_product_select_cart);
			
			if(shownum.contains(position)){
				changeProductNum(holder);
			}else{
				finishProductNum(holder);
			}
		  if(selectnum.contains(Integer.valueOf(position))){
			  holder.iv_shop_selects.setImageResource(R.drawable.product_selecet);
			  holder.iv_product_select_cart.setImageResource(R.drawable.product_selecet);
		  }else{
			  holder.iv_shop_selects.setImageResource(R.drawable.product_unselect);
			  holder.iv_product_select_cart.setImageResource(R.drawable.product_unselect);
		  }
		shopCart = list.get(position);
		if (!pos.contains(position)) {
			holder.ll_shop_title.setVisibility(View.GONE);
			holder.iv_shop_divider.setVisibility(View.GONE);
		} else {
			holder.ll_shop_title.setVisibility(View.VISIBLE);
			holder.tv_shop_name.setText(shopCart.getShopname());
			if (position == 0) {
				holder.iv_shop_divider.setVisibility(View.GONE);
			} else {
				holder.iv_shop_divider.setVisibility(View.VISIBLE);
			}

		}
		holder.tv_change_buy_num.setText(shopCart.getSl());
		holder.tv_pr_introduce_cart.setText(shopCart.getCpmc());
		holder.tv_product_num.setText("￥"+shopCart.getDj()+"x" + shopCart.getSl());
		holder.product_price_cart.setText("￥" + shopCart.getJe());
		if(shopCart.getChima()==null){
			
			holder.size_and_color.setText("产品信息：");
		}else{
			holder.size_and_color.setText("产品信息：" + shopCart.getChima());
			
		}
		ImageUtilities.loadBitMap(shopCart.getCppic(), holder.iv_product_cart);
		holder.iv_product_cart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ProductActivity.class);
				intent.putExtra("productId", list.get(position).getMid());
				context.startActivity(intent);
				
			}
		});
		holder.iv_product_select_cart.setOnClickListener(new View.OnClickListener() {
			
			boolean productselect=false;
			@Override
			public void onClick(View v) {
				int index;
				int nextindex;
				ShopCart shopCart2;
				String shopid=list.get(position).getShopid();
				ImageView iv_shop;
				if(/*productselect&&*/selectnum.contains(Integer.valueOf(position))){
					 ((ImageView)v).setImageResource(R.drawable.product_unselect);
					 selectnum.remove(Integer.valueOf(position));
					 View view;
					 for(int i=0;i<pos.size();i++){
						 if(TextUtils.equals(list.get(pos.get(i)).getShopid(),shopid)){
							View view2=parents.getChildAt(pos.get(i)-((ListView)parents).getFirstVisiblePosition());
							if(view2==null){
								Toast.makeText(context, "null", 0).show();
								return ;
							}
							selectShop.remove(pos.get(i));
							iv_shop=(ImageView) view2.findViewById(R.id.iv_shop_selects);
							iv_shop.setImageResource(R.drawable.product_unselect);
							break;
						 }
					 }
				}else if(/*!productselect&&*/!selectnum.contains(Integer.valueOf(position))) {
					 ((ImageView)v).setImageResource(R.drawable.product_selecet);
					 selectnum.add(Integer.valueOf(position));
					 
					 productselect=true;
					 //index= selectnum.indexOf(Integer.valueOf(position));
					// nextindex=((index+1)==selectnum.size())?list.size()-1:selectnum.get(index+1)-1;
				}
				listener.changeprice(selectnum);
				
			}
		});
		holder.iv_shop_selects.setOnClickListener(new View.OnClickListener() {
			 boolean shopselect=false;
			@Override
			public void onClick(View v) {
				int index = pos.indexOf(position);
				if(!selectShop.contains(Integer.valueOf(position))){
					((ImageView)v).setImageResource(R.drawable.product_selecet);
					selectShop.add(Integer.valueOf(position));
					shopselect=true;
				}else{
					((ImageView)v).setImageResource(R.drawable.product_unselect);
					selectShop.remove(Integer.valueOf(position));
					shopselect=false;
				}
				ImageView iv = null;
				View view=null;
				int nextindex=((index+1)==pos.size())?list.size()-1:pos.get(index+1)-1;
				for(int i=position;i<=nextindex;i++){
					 view = (View) parents.getChildAt(i-((ListView)parents).getFirstVisiblePosition());
					 if(view!=null){
						iv = (ImageView) view.findViewById(R.id.iv_product_select_cart);
					 }else{
						 return ;
					 }
					 if(shopselect/*!selectnum.contains(Integer.valueOf(i))*/){
						 iv.setImageResource(R.drawable.product_selecet);
						 if(!selectnum.contains(Integer.valueOf(i))){
							 selectnum.add(Integer.valueOf(i));
						 }
					 }else if(!shopselect/*selectnum.contains(Integer.valueOf(i))*/){
						 iv.setImageResource(R.drawable.product_unselect);
						 //((ImageView)v).setBackgroundResource(R.drawable.product_unselect);
						 selectnum.remove(Integer.valueOf(i));
					 }
				}
				listener.changeprice(selectnum);
			}
		});
		holder.change_product_num.setOnClickListener(new View.OnClickListener() {
			 boolean isChangeNumState=false;
			@Override
			public void onClick(View v) {
				if(holder.ll_change_num.getVisibility()==View.INVISIBLE){
					changeProductNum(holder);
					shownum.add(position);
					isChangeNumState=false;
				}else if(holder.ll_change_num.getVisibility()==View.VISIBLE){
					finishProductNum(holder);
					isChangeNumState=true;
					shownum.remove(Integer.valueOf(position));
					ShopCart cart = list.get(position);
					String ctid=cart.getCtid();
					String sl=holder.tv_change_buy_num.getText().toString();
					listener.updataCartMsg(ctid,sl);
				}
				
			}
		});
		holder.iv_sub_num.setOnClickListener(new View.OnClickListener() {
			int num;				
			
			@Override
			public void onClick(View v) {
				num=Integer.parseInt(holder.tv_change_buy_num.getText().toString());
				if(num<=1){
					return ;
				}
				num--;
				holder.tv_change_buy_num.setText(num+"");
			}
		});
		holder.iv_add_num.setOnClickListener(new View.OnClickListener() {
			int num ;
			@Override
			public void onClick(View v) {
				num =Integer.parseInt(holder.tv_change_buy_num.getText().toString());
				num++;
				holder.tv_change_buy_num.setText(num+"");
				
			}
		});
		return convertview;
	}

	private void finishProductNum(final ViewHolder holder) {
		holder.tv_product_num.setVisibility(View.VISIBLE);
		holder.tv_pr_introduce_cart.setVisibility(View.VISIBLE);
		// holder.tv_change_buy_num.setVisibility(View.VISIBLE);
		holder.ll_change_num.setVisibility(View.INVISIBLE);
		holder.size_and_color.setVisibility(View.VISIBLE);
		holder.change_product_num
				.setBackgroundResource(R.drawable.cart_change_num);
		// holder.tv_change_buy_num.setText("1");
	}

	private void changeProductNum(final ViewHolder holder) {
		holder.tv_product_num.setVisibility(View.INVISIBLE);
		holder.tv_pr_introduce_cart.setVisibility(View.INVISIBLE);
		holder.ll_change_num.setVisibility(View.VISIBLE);
		// holder.tv_change_buy_num.setVisibility(View.INVISIBLE);
		// holder.tv_change_buy_num.setText("1");
		holder.size_and_color.setVisibility(View.INVISIBLE);
		holder.tv_change_buy_num.setVisibility(View.VISIBLE);
		holder.change_product_num
				.setBackgroundResource(R.drawable.cart_sure_num);
	}
	private class ViewHolder {
		ImageView iv_shop_selects, change_product_num, iv_product_cart,
				iv_shop_divider, iv_sub_num, iv_add_num,iv_product_select_cart;
		TextView tv_product_num, product_price_cart, tv_pr_introduce_cart,
				size_and_color, tv_shop_name, tv_change_buy_num;
		LinearLayout ll_shop_title, ll_change_num;
	}
	private void checkpopwindow(){
		
	}

	public void selectall() {
		View view;
		selectnum.clear();
		ImageView iv_shop,iv_product;
		
		for(int i=0;i<list.size();i++){
			selectnum.add(Integer.valueOf(i));
			/*view=listview.getChildAt(i-listview.getFirstVisiblePosition());
			iv_shop=(ImageView) view.findViewById(R.id.iv_shop_selects);
			iv_shop.setImageResource(R.drawable.product_selecet);
			iv_product=(ImageView) view.findViewById(R.id.iv_product_select_cart);
			iv_product.setImageResource(R.drawable.product_selecet);*/
		}
		listener.changeprice(selectnum);
	}

	public void unselectall() {
		selectnum.clear();
		listener.changeprice(selectnum);
		
	}

	/*@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
    View item;
    ImageView iv_s,iv_p;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		System.out.println("firstvisiableitem:"+firstVisibleItem);
		for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
			if(selectnum.contains(i)){
				item=view.getChildAt(i-firstVisibleItem);	
			    iv_s=(ImageView) view.findViewById(R.id.iv_shop_selects);
				iv_s.setImageResource(R.drawable.product_selecet);
				iv_p=(ImageView) view.findViewById(R.id.iv_product_select_cart);
				iv_p.setImageResource(R.drawable.product_selecet);
			}
			
		}
		
	}*/


}
