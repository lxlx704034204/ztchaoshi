package com.iteambuysale.zhongtuan.adapter;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.specialsale.LadyCatgory.Viewholder;
import com.iteambuysale.zhongtuan.model.DiscountQuan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiscountCouponAdapter extends BaseAdapter {
	DiscountQuan [] quan;
	private Context context;
	private DiscountQuan discountQuan; 
    public DiscountCouponAdapter(DiscountQuan[] quan,Context context){
    	this.quan=quan;
    	this.context=context;
    }
	@Override
	public int getCount() {
		return quan.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return quan[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView =View.inflate(context, R.layout.discount_quan_adapter, null);
			holder.tv_quan_type=(TextView) convertView.findViewById(R.id.tv_quan_type);
			holder.tv_quan_price=(TextView) convertView.findViewById(R.id.tv_quan_price);
			holder.tv_quan_num=(TextView) convertView.findViewById(R.id.tv_quan_num);
			holder.tv_min_price=(TextView) convertView.findViewById(R.id.tv_min_price);
			holder.tv_availabel_data=(TextView) convertView.findViewById(R.id.tv_availabel_data);
			//holder.tv_use_area=(TextView) convertView.findViewById(R.id.tv_use_area);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		discountQuan = quan[position];
		holder.tv_quan_type.setText(discountQuan.getTmqmc());
		holder.tv_quan_price.setText("￥"+discountQuan.getTmqje());
		holder.tv_quan_num.setText("编号："+discountQuan.getTmqno());
		holder.tv_availabel_data.setText("有效期:"+discountQuan.getSdate().split(" ")[0]+"至"+discountQuan.getEdate().split(" ")[0]);
		holder.tv_min_price.setText("所需消费金额："+discountQuan.getOrdje());
		return convertView;
	}
   private class ViewHolder{
	   TextView  tv_quan_type,tv_quan_price,
	   tv_quan_num,tv_min_price,
	   tv_availabel_data,tv_use_area;
   }
}
