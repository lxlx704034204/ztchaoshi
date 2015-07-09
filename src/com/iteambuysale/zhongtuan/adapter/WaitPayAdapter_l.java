package com.iteambuysale.zhongtuan.adapter;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.me.MeEvalutionBefore_l;
import com.iteambuysale.zhongtuan.activity.me.unpay.OrderDetailActivity_l;
import com.iteambuysale.zhongtuan.activity.me.waitgoods.WaitForGoods;
import com.iteambuysale.zhongtuan.activity.specialsale.Paymoney;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.me.OderDeleteListener;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class WaitPayAdapter_l extends BaseAdapter{
	private Context context;
	private Map<String, OrderTM> orderMap;
	OderDeleteListener listener;
	private int orderstatus;
	private final int UN_PAY=0;
	private final int PAYED=1;
	private final int WAIT_EVAL=2;
	private final int SALE_AFTER=3;
	private final int ALL_ORDER=4;
	private final int ENSURE_PACKAGE=9;
	private final int GO_EVALUTE=6;

	public WaitPayAdapter_l(Context context, Map<String, OrderTM> orderMap,OderDeleteListener listener,int orderstatus) {
		this.context = context;
		this.orderMap = orderMap;
		this.listener =listener;
		this.orderstatus=orderstatus;
	}

	@Override
	public int getCount() {
		return orderMap.size();
	}

	@Override
	public Object getItem(int position) {
		int i = 0;
		for (Map.Entry<String, OrderTM> entry : orderMap.entrySet()) {
			if (i == position) {
				return entry;
			}
			i++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.unpay_adapter_l, null);
			holder.tv_order_num = (TextView) convertView
					.findViewById(R.id.tv_order_num);
			holder.tv_product_price = (TextView) convertView
					.findViewById(R.id.tv_product_price);
			holder.tv_should_pay = (TextView) convertView
					.findViewById(R.id.tv_should_pay);
			holder.tv_buynum = (TextView) convertView
					.findViewById(R.id.tv_buynum);
			holder.tv_product_name = (TextView) convertView
					.findViewById(R.id.tv_product_name);
			holder.ll_img_album = (LinearLayout) convertView
					.findViewById(R.id.ll_img_album);
			holder.ll_only_one_show = (LinearLayout) convertView
					.findViewById(R.id.ll_only_one_show);
			holder.iv_img_one = (ImageView) convertView
					.findViewById(R.id.iv_img_one);
			holder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
			holder.tv_concel_order = (TextView) convertView
					.findViewById(R.id.tv_concel_order);
			holder.tv_orderstatus = (TextView) convertView
					.findViewById(R.id.tv_orderstatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		final Map.Entry<String, OrderTM> entry = (java.util.Map.Entry<String, OrderTM>) getItem(position);
		//System.out.println("订单号："+entry.getValue().getOrdno()+","+entry.getValue().getCpmx().length+",");
		if (entry.getValue().getCpmx().length > 1) {
			
			holder.ll_only_one_show.setVisibility(View.GONE);
			holder.ll_img_album.setVisibility(View.VISIBLE);
			showSumOrder(holder,entry.getValue().getCpmx());
			if(orderstatus==PAYED){
			holder.tv_concel_order.setText("确认收货");
			holder.tv_pay.setVisibility(View.GONE);
			}else if(orderstatus==UN_PAY){
				holder.tv_concel_order.setText("取消订单");
				holder.tv_pay.setVisibility(View.VISIBLE);
				holder.tv_pay.setText("付款");
			}else if(orderstatus==WAIT_EVAL){
				holder.tv_concel_order.setText("去评价");
				holder.tv_pay.setVisibility(View.GONE);
			}else if(orderstatus==SALE_AFTER){
				holder.tv_concel_order.setText("申请售后");
				holder.tv_pay.setVisibility(View.GONE);
			}else if(orderstatus==ALL_ORDER){
				holder.tv_concel_order.setVisibility(View.GONE);
				holder.tv_pay.setVisibility(View.GONE);
			}
		} else {
			holder.ll_only_one_show.setVisibility(View.VISIBLE);
			holder.ll_img_album.setVisibility(View.GONE);
			showOneOrder(holder,entry.getValue().getCpmx());
			if(orderstatus==PAYED){
			holder.tv_concel_order.setText("确认收货");
			holder.tv_pay.setVisibility(View.VISIBLE);
			holder.tv_pay.setText("查看物流");
			}else if(orderstatus==UN_PAY){
				holder.tv_concel_order.setText("取消订单");
				holder.tv_pay.setVisibility(View.VISIBLE);
				holder.tv_pay.setText("付款");
			}else if(orderstatus==WAIT_EVAL){
				holder.tv_concel_order.setText("去评价");
				holder.tv_pay.setVisibility(View.GONE);
			}else if(orderstatus==SALE_AFTER){
				holder.tv_concel_order.setText("申请售后");
				holder.tv_pay.setVisibility(View.GONE);
			}else if (orderstatus==ALL_ORDER){
				holder.tv_concel_order.setVisibility(View.GONE);
				holder.tv_pay.setVisibility(View.GONE);
			}
			
		}
		switch (entry.getValue().getOrdzt()) {
		case "0":
				holder.tv_orderstatus.setText("代付款");
			break;
		case "1":
			   holder.tv_orderstatus.setText("已支付,待发货");
			   break;
		case "2":
			holder.tv_orderstatus.setText("已收货");
			break;
		case  "3":
			holder.tv_orderstatus.setText("已评价");
			break;
		case "4":
			holder.tv_orderstatus.setText("已发货");
			break;
		case "5":
			holder.tv_orderstatus.setText("申请退款");
			break;
		case "6":
			holder.tv_orderstatus.setText("同意退款");
			break;
		default:
			break;
		}
	/*	if(==UN_PAY){
			holder.tv_orderstatus.setText("代付款");
		}else if(orderstatus==PAYED){
			holder.tv_orderstatus.setText("已支付，待发货");
		}else if(orderstatus==WAIT_EVAL){
			holder.tv_orderstatus.setText(text)
		}*/
		holder.tv_should_pay.setText("实付款：￥"+entry.getValue().getOrdje());
		holder.tv_order_num.setText(entry.getValue().getOrdno());
		holder.tv_pay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(orderstatus==UN_PAY){
					
				Intent intent =new Intent(context, Paymoney.class);
				intent.putExtra("cpje", entry.getValue().getOrdje());
				intent.putExtra("ordno", entry.getValue().getOrdno());
				context.startActivity(intent);
				}else if(orderstatus==PAYED){
					String url = "http://app.teambuy.com.cn/webc/m/tmlog/id/"
							+ entry.getValue().getLogid();
					Uri uri = Uri.parse(url);
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.setData(uri);
					context.startActivity(intent);
				}
				
			}
		});
		holder.tv_concel_order.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(orderstatus==UN_PAY){
					
					listener.deleteOrder(entry.getValue().getOrdno());
				}else if(orderstatus==PAYED){
					if(entry.getValue().getCpmx().length>1){
						Intent intent =new Intent(context,MeEvalutionBefore_l.class);
						intent.putExtra("orderno", entry.getValue().getOrdno());
						intent.putExtra("Fromstatus", ENSURE_PACKAGE);
						context.startActivity(intent);
					}else{
						ensurePackage(entry.getValue().getOrdno());
						
					}
				}else if(orderstatus==WAIT_EVAL){
					Intent intent =new Intent(context, MeEvalutionBefore_l.class);
				/*	OrderDetailsTM[] cpmx = entry.getValue().getCpmx();
					CharSequence [] orderChildren=new CharSequence[cpmx.length];
				    for(int i=0;i<cpmx.length;i++){
				    	orderChildren[i]=cpmx[i].getOrdnox();
				    }*/
				    intent.putExtra("orderno", entry.getValue().getOrdno());
				    intent.putExtra("Fromstatus",GO_EVALUTE );
				    intent.putExtra("time", entry.getValue().getDateandtime());
				    intent.putExtra("orderid", entry.getValue().getCoid());
					context.startActivity(intent);
				}else if(orderstatus==SALE_AFTER){
				  Toast.makeText(context , "售后服务功能暂时未完成，尽请期待", 0).show();
				}
			}
		});
		holder.ll_img_album.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, OrderDetailActivity_l.class);
				intent.putExtra("orderno", entry.getValue().getOrdno());
				intent.putExtra("ordje", entry.getValue().getOrdje());
				intent.putExtra("orderstatus", orderstatus);
				intent.putExtra("logistics", entry.getValue().getLogid());
			     context.startActivity(intent);
					
				
			}
		});
		holder.ll_only_one_show.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, OrderDetailActivity_l.class);
				intent.putExtra("orderno", entry.getValue().getOrdno());
				intent.putExtra("ordje", entry.getValue().getOrdje());
				intent.putExtra("orderstatus", orderstatus);
				intent.putExtra("logistics", entry.getValue().getLogid());
			     context.startActivity(intent);
			}
		});
		return convertView;
	}

	protected void ensurePackage(final String orderno) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				context);
		dialog.setTitle("提示");
		dialog.setMessage("是否确认收货？");
		dialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
						listener.ensureorder(orderno);

					}
				});
		dialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
						arg0.dismiss();
					}
				});
		dialog.create().show();
		
	}

	private void showOneOrder(ViewHolder holder, OrderDetailsTM[] orderDetailsTMs) {
       ImageUtilities.loadBitMap(orderDetailsTMs[0].getCppic(), holder.iv_img_one);
       holder.tv_product_name.setText(orderDetailsTMs[0].getCpmc());
       holder.tv_product_price.setText(orderDetailsTMs[0].getOje());
       holder.tv_buynum.setText("X"+orderDetailsTMs[0].getOsl());
	}

	private void showSumOrder(ViewHolder holder, OrderDetailsTM[] orderDetailsTMs) {
		holder.ll_img_album.removeAllViews();
         for(int i=0;i<orderDetailsTMs.length;i++){
        	ViewGroup viewgroup=(ViewGroup)View.inflate(context, R.layout.order_image_view, null);
        	ImageView img=(ImageView) viewgroup.getChildAt(0);
        	ImageUtilities.loadBitMap(orderDetailsTMs[i].getCppic(), img);
        	holder.ll_img_album.addView(viewgroup, i);
         }
	}

	private static class ViewHolder {
		public TextView tv_order_num, tv_product_price, tv_should_pay,
				tv_buynum,tv_product_name;
		public LinearLayout ll_img_album, ll_only_one_show;
		public ImageView iv_img_one;
		public TextView tv_pay, tv_concel_order,tv_orderstatus;
	}
   


}
